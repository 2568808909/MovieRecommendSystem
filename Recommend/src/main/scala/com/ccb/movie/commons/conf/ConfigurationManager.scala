/*
 * Copyright (c) 2017. Atguigu Inc. All Rights Reserved.
 * Date: 10/31/17 3:42 PM.
 * Author: wuyufei.
 */

package com.ccb.movie.commons.conf

import java.util.Properties

import com.ccb.movie.recommend.offline.OfflineRecommendTrainer
import org.apache.commons.configuration2.{FileBasedConfiguration, PropertiesConfiguration}
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters

/**
  * 配置工具类
  */
object ConfigurationManager {

  val properties:Properties=new Properties()
  properties.load(OfflineRecommendTrainer.getClass.getResourceAsStream("/recommend.properties"))

//  private val params = new Parameters()
//  private val builder = new FileBasedConfigurationBuilder[FileBasedConfiguration](classOf[PropertiesConfiguration])
//    .configure(params.properties().setFileName("recommend.properties"))
  //配置对象
  //val config = builder.getConfiguration()



}
