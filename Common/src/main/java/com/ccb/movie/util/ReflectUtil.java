package com.ccb.movie.util;

import com.ccb.movie.anno.Convert;
import com.ccb.movie.exception.BizException;

import java.lang.reflect.Field;
import java.util.Optional;

public class ReflectUtil {

    /**
     * 通过反射，将一个对象同名的属性付给另一个对象，省去大量set操作
     * 也可以用Convert注解标注属性，指定赋值给目标对象的那个属性
     * @param object        源对象
     * @param targetClass   目标对象类
     * @param <T>           目标对象
     * @return 返回赋值完成的目标对象
     */
    public static <T> T convertTo(Object object, Class<T> targetClass) {
        Field[] fields = object.getClass().getDeclaredFields();
        String fieldName = "";
        try {
            T result = targetClass.newInstance();
            for (Field field : fields) {
                fieldName = Optional.ofNullable(field.getAnnotation(Convert.class))
                        .map(Convert::field).orElse(field.getName());
                Object value = field.get(object);
                Field targetField = targetClass.getField(fieldName);
                targetField.set(result, value);
            }
            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BizException(String.format("初始化%s对象失败", targetClass.getName()));
        } catch (NoSuchFieldException e) {
            throw new BizException(String.format("转换失败%s类中没有%s属性", targetClass.getName(), fieldName));
        }
    }
}
