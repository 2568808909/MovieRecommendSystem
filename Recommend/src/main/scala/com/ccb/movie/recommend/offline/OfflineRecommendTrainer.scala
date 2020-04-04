package com.ccb.movie.recommend.offline

import java.text.DecimalFormat

import com.ccb.movie.commons.conf.ConfigurationManager
import com.ccb.movie.commons.constant.Constants
import com.ccb.movie.commons.model.{MovieRecs, MySqlConfig, Recommendation, UserRecs}
import com.ccb.movie.commons.pool.CreateRedisPool
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}
import org.jblas.DoubleMatrix

import scala.collection.JavaConversions._

object OfflineRecommendTrainer {
  //用于规范小数，保留两位有效数字
  val df = new DecimalFormat("#.00")

  def main(args: Array[String]): Unit = {
    var sparkMaster = "local[*]"

    if (args.length != 1) {
      System.out.println("Usage: alsRecommender <master>\n" +
        "  ===================================\n" +
        "  Using Default Values\n")
    } else {
      sparkMaster = args(0)
    }

    //创建全局配置
    val params = scala.collection.mutable.Map[String, String]()
    // Spark配置
    params += "spark.cores" -> sparkMaster
    // MySQL配置
    params += "mysql.url" -> ConfigurationManager.config.getString(Constants.JDBC_URL)
    params += "mysql.user" -> ConfigurationManager.config.getString(Constants.JDBC_USER)
    params += "mysql.password" -> ConfigurationManager.config.getString(Constants.JDBC_PASSWORD)
    // Redis配置
    params += "redis.host" -> ConfigurationManager.config.getString(Constants.REDIS_HOST)
    params += "redis.port" -> ConfigurationManager.config.getString(Constants.REDIS_PORT)

    // 定义MySqlDB的配置对象，用implicit修饰，当函数调用有隐藏参数时会默认传入
    implicit val mySqlConfig: MySqlConfig = MySqlConfig(params("mysql.url"), params("mysql.user"), params("mysql.password"))

    // 创建SparkConf
    val conf = new SparkConf().setAppName("RecommenderApp").setMaster(params("spark.cores")).set("spark.executor.memory", "6G")

    // 创建SparkSession
    implicit val spark: SparkSession = SparkSession.builder()
      .config(conf)
      .getOrCreate()

    // 计算推荐数据
    calculateRecs(Constants.MAX_RECOMMENDATIONS)

    spark.close()

  }

  /**
    * 计算推荐数据
    *
    * @param spark       sparkSession
    * @param maxRecs     推荐数目
    * @param mySqlConfig mysql配置
    */
  def calculateRecs(maxRecs: Int)(implicit spark: SparkSession, mySqlConfig: MySqlConfig): Unit = {
    import spark.implicits._

    // 通过SparkSQL从MySQL中读取打分数据
    val ratings = spark.read
      .format("jdbc")
      .option("url", mySqlConfig.url)
      .option("dbtable", Constants.DB_RATING)
      .option("user", mySqlConfig.user)
      .option("password", mySqlConfig.password)
      .load()
      .select($"mid", $"uid", $"score")
      .cache

    // 通过select选择rating表中的uid列，去重，将Row类型转化为Int类型，缓存
    val users = ratings
      .select($"uid")
      .distinct
      .map(r => r.getAs[Int]("uid"))
      .cache


    // 通过select选择rating表中的mid列，去重，将Row类型转化为Int类型，缓存
    val movies = spark.read
      .format("jdbc")
      .option("url", mySqlConfig.url)
      .option("dbtable", Constants.DB_MOVIE)
      .option("user", mySqlConfig.user)
      .option("password", mySqlConfig.password)
      .load()
      .select($"mid")
      .distinct
      .map(r => r.getAs[Int]("mid"))
      .cache

    // 将从MySQL获取到的rating数据，转化为训练数据格式
    //    val trainData = ratings.map { line =>
    //      Rating(line.getAs[Int]("uid"), line.getAs[Int]("mid"), line.getAs[Double]("score"))
    //    }.rdd.cache()
    //
    //    // rank：要使用的特征个数，即维度
    //    // iterations：ALS算法迭代次数
    //    // lambda：正则化参数
    //    val (rank, iterations, lambda) = (50, 5, 0.01)
    //
    //    // 将训练数据和参数传入ALS模型进行训练，返回结果模型
    //    val model = ALS.train(trainData, rank, iterations, lambda)
    //
    //    // 计算为用户推荐的电影集合矩阵 RDD[UserRecommendation(id: Int, recs: Seq[Rating])]
    //    calculateUserRecs(maxRecs, model, users, movies)
    //    // 计算与某个电影最相似的N个电影
    //    calculateProductRecs(maxRecs, model, movies)
    //
    //    // 将缓存的数据从内存中移除
    //    ratings.unpersist()
    //    users.unpersist()
    //    movies.unpersist()
    //    trainData.unpersist()

  }


  /**
    * 计算为用户推荐的电影集合矩阵 RDD[UserRecommendation(id: Int, recs: Seq[Rating])]
    *
    * 分析：
    * 输入所有的用户id和产品id，通过笛卡尔积将用户与产品联系在一起 => RDD[(userId,productId)]
    * 将此RDD传入模型中，模型会预测用户对每一个产品的评分
    * 获取到所有用户对所有产品的预测评分后，按照用户对评分进行排序，获取前maxRes个产品，记录起来进行推荐
    *
    * @param maxRecs 最大推荐数目
    * @param model ALS训练出来的模型
    * @param users 所有的用户id
    * @param products 所有的产品(电影)id
    */
  private def calculateUserRecs(maxRecs: Int, model: MatrixFactorizationModel, users: Dataset[Int], products: Dataset[Int])(implicit mySqlConfig: MySqlConfig): Unit = {

    import users.sparkSession.implicits._

    // 对userId和mid执行笛卡尔积
    val userProductsJoin = users.crossJoin(products)

    // 对userProductsJoin的每一行进行操作，每一行数据为：(userId, mid)，转化为RDD[(userId,mid)]
    val userRating = userProductsJoin.map { row => (row.getAs[Int](0), row.getAs[Int](1)) }.rdd

    // 定义RatingOrder类，用于排序
    object RatingOrder extends Ordering[Rating] {
      def compare(x: Rating, y: Rating): Int = y.rating compare x.rating
    }

    // 根据已经训练完成后的模型对userRating进行预测
    // 获取到的预测结果数据是：Rating(userId, productId, rating)
    val recommendations = model.predict(userRating)
      // 去除分数小于0的数据
      .filter(_.rating > 0)
      // 按照userId进行分组：(userId, Iterable(Rating))
      .groupBy(p => p.user)
      // (userId, Iterable(Rating))
      .map { case (uid, predictions) =>
      // 按照RatingOrder中的compare方法对评分进行排序
      // sorted()需要传入一个继承Ordering的类，并使用这个类的compare方法进行排序
      val recommendations = predictions.toSeq.sorted(RatingOrder)
        // 取前maxRecs个数据
        // Seq(Rating)
        .take(maxRecs)
        // 将前maxRecs个数据转换为Recommendation类
        // Seq(Recommendation)
        .map(p => Recommendation(p.product, df.format(p.rating).toDouble))

      // 以(userId, (mid1, rate1)|(mid2, rate2)|(mid3, rate3)|...)格式返回结果
      UserRecs(uid, recommendations.mkString("|"))
    }.toDF()

    // 将推荐结果写入MySQL
    recommendations.write
      .format("jdbc")
      .option("url", mySqlConfig.url)
      .option("dbtable", Constants.DB_USER_RECS)
      .option("user", mySqlConfig.user)
      .option("password", mySqlConfig.password)
      .mode(SaveMode.Overwrite)
      .save()
  }

  /**
    * 计算每个电影的前N个最相似电影
    *
    * 分析：
    * 通过模型获取产品的特征矩阵，并进行自身的笛卡尔积
    * 方便进行产品之间的相似度(余弦相似度)计算
    * 保留相似度大于0.6的结果，并对相似度排序
    * 取前maxRes的产品
    *
    * @param maxRecs 最大推荐数目
    * @param model ALS训练出来的模型
    * @param products 产品id
    * @param mySqlConfig mysql配置
    */
  private def calculateProductRecs(maxRecs: Int, model: MatrixFactorizationModel, products: Dataset[Int])(implicit mySqlConfig: MySqlConfig): Unit = {

    import products.sparkSession.implicits._

    // 用于sorted()排序
    object RatingOrder extends Ordering[(Int, Int, Double)] {
      def compare(x: (Int, Int, Double), y: (Int, Int, Double)): Int = y._3 compare x._3
    }

    // 获取电影特征矩阵
    // model.productFeatures的返回结果是(mid, factor:Array[Double])
    //如果获取用户特征矩阵 model.userFeatures，然后传入的参数为用户id，后续的计算结果将是用户的相似程度
    val productsVectorRdd = model.productFeatures
      .map { case (movieId, factor) =>
        // 将Array[Double]类型的factor转化为行向量
        val factorVector = new DoubleMatrix(factor)
        // 返回mid和行向量形式的特征向量
        (movieId, factorVector)
      }

    // 最低相似度
    val minSimilarity = 0.6

    // 对电影特征矩阵RDD进行笛卡儿积，获得每个电影与任意其他电影的特征向量对应关系：((movieId1, vector1), (movieId2, vector2))
    val movieRecommendation = productsVectorRdd.cartesian(productsVectorRdd)
      // 过滤掉相同电影之间的对应关系
      .filter { case ((movieId1, vector1), (movieId2, vector2)) => movieId1 != movieId2 }
      // 计算两步电影之间的相似度
      .map { case ((movieId1, vector1), (movieId2, vector2)) =>
      val sim = cosineSimilarity(vector1, vector2)
      (movieId1, movieId2, sim)
      // 过滤掉相似度小于最低相似度的电影相似度项
      // (movieId1, movieId2, sim)
    }.filter(_._3 >= minSimilarity)
      // 按照movieId1进行分组聚合
      // (movieId1, Iterable(movieId1, movieId2, sim))
      .groupBy(p => p._1)
      // 对分组数据进行处理，取最相似的N个电影
      // (movieId1, Iterable(movieId1, movieId2, sim))
      .map { case (mid: Int, predictions: Iterable[(Int, Int, Double)]) =>
      // 首先按照相似度大小排序
      val sortedRecs = predictions.toSeq.sorted(RatingOrder)

      // 获取Redis连接
      val redisPool = CreateRedisPool()
      val client = redisPool.borrowObject()

      // 向Redis插入某个电影对应的50个最相似的电影
      // 实时推荐算法中会用到此数据
      client.del("set:" + mid)
      // 插入的数据格式为：movieId2:similarity
      client.sadd("set:" + mid, sortedRecs.take(50).map(item => item._2 + ":" + item._3): _*)

      // 将所有的电影相似度对应关系都保存到redis中
      client.del("map:" + mid)
      client.hmset("map:" + mid, sortedRecs.map(item => (item._2.toString, item._3.toString)).toMap)

      // 将client返回redis连接池
      redisPool.returnObject(client)

      // 选取最相似的maxRecs个数据转换为Recommendation类，再以(mid1, (mid2,sim1_2)|(mid6,sim1_6)|(mid4,sim1_4)|...)
      val recommendations = sortedRecs.take(maxRecs).map(p => Recommendation(p._2, df.format(p._3).toDouble))
      MovieRecs(mid, recommendations.mkString("|"))
    }.toDF().cache()

    // 将数据插入到MySQL
    movieRecommendation.write
      .format("jdbc")
      .option("url", mySqlConfig.url)
      .option("dbtable", Constants.DB_MOVIE_RECS)
      .option("user", mySqlConfig.user)
      .option("password", mySqlConfig.password)
      .mode(SaveMode.Overwrite)
      .save()

    // 将缓存数据从内存中移除
    movieRecommendation.unpersist()
  }

  /**
    * 计算两个向量的余弦相似度
    * 有两个向量a，b,则ab之间的余弦相似度cosθ为：
    *  a·b =||a||*||b|| cosθ => cosθ =a·b / ||a||*||b||
    *  ||a|| 为a的第二范数(欧式范数)，也就是其长度
    *
    * @param vec1 向量1
    * @param vec2 向量2
    * @return
    */
  private def cosineSimilarity(vec1: DoubleMatrix, vec2: DoubleMatrix): Double = {
    vec1.dot(vec2) / (vec1.norm2() * vec2.norm2())
  }

  //private def updateRedis()
}
