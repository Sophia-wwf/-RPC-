package com.smgeek.gkrpc.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 芳芳
 * @create 2022-03-22 20:39
 * @address
 * @desc 反射
 **/
public class ReflectionUtils {
    /**
     * 根据class创建对象
     * @param clazz 待创建对象的类
     * @param <T> 对象类型
     * @return 创建好的对象
     */
    public static <T> T newInstance(Class<T> clazz){
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 获取某个class的共有方法
     * 用 public 修饰的成员变量和方法称为共有变量和共有方法。
     * @param clazz
     * @return 当前类声明的共有方法
     */
    public static Method[] getPublicMethods(Class clazz){
        // getMethods()：获取本类以及父类或者父接口中所有的公共方法(public修饰符修饰的)
        // getDeclaredMethods()：获取本类中的所有方法，包括私有的(private、protected、默认以及public)的方法。
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> pmethods = new ArrayList<Method>();
        for (Method m : methods) {
            // java.lang.reflect.Modifier.isPublic(int mod)：如果mod包含public修饰符，则为true; 否则为：false。
            //getModifiers()方法返回int类型值表示该字段的修饰符。
            if(Modifier.isPublic(m.getModifiers())){
                pmethods.add(m);
            }
        }
        // 将pmethods转换成Method数组，toArray方法的可以用一个零长度的数组作为参数。
        return pmethods.toArray(new Method[0]);
    }

    /**
     * 调用指定对象的指定方法
     * @param obj 被调用方法的对象
     * @param method 被调用的方法
     * @param args 方法的参数
     * @return 返回结果
     */
    public static Object invoke(Object obj,
                                Method method,
                                Object... args){
        try {
            // 就是调用类中的方法，最简单的用法是可以把方法参数化
            //invoke(class, method)
            //比如你Test类里有一系列名字相似的方法setValue1、setValue2等等
            //可以把方法名存进数组v[]，然后循环里invoke(test,v[i])，就顺序调用了全部setValue
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
