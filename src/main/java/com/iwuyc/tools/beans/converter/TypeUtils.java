package com.iwuyc.tools.beans.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型相关工具类
 *
 * @author Neil
 */
public class TypeUtils {
    /**
     * 基础类型 跟 包装类型 的映射关系。
     */
    public final static Map<Class<?>, Class<?>> PRIMITIVE_TYPES_MAPPING_WRAPPED_TYPES;

    static {

        Map<Class<?>, Class<?>> temp = new HashMap<>();
        temp.put(void.class, Void.class);

        temp.put(byte.class, Byte.class);
        temp.put(short.class, Short.class);
        temp.put(int.class, Integer.class);
        temp.put(long.class, Long.class);

        temp.put(float.class, Float.class);
        temp.put(double.class, Double.class);

        temp.put(boolean.class, Boolean.class);
        temp.put(char.class, Character.class);

        PRIMITIVE_TYPES_MAPPING_WRAPPED_TYPES = Collections.unmodifiableMap(temp);
    }

    /**
     * 用于判断是否可以将clazzA转换成clazzB，即clazzB的类型是否跟clazzA的类型是一致的，或者前者是后者的父类。
     *
     * @param clazzA 类型A，通常是同一个类，或者是子类
     * @param clazzB 类型B，通常是同一个类，或者是父类
     * @return 如果类型B是类型A的同类、或者父类，则返回true，否则返回false
     */
    public static boolean canBeCase(Class<?> clazzA, Class<?> clazzB) {
        if (clazzA == clazzB) {
            return true;
        }
        final Class<?> innerClazzA;
        if (clazzA.isPrimitive()) {
            innerClazzA = primitiveTypeTranslator(clazzA);
        } else {
            innerClazzA = clazzA;
        }
        Class<?> innerClazzB;
        if (clazzB.isPrimitive()) {
            innerClazzB = primitiveTypeTranslator(clazzB);
        } else {
            innerClazzB = clazzB;
        }

        return innerClazzB.isAssignableFrom(innerClazzA);
    }

    /**
     * 将基础类型的类对象转换成封装类型的类对象。如果targetClass不为基础类型，则直接返回targetClass
     *
     * @param targetClass 基础类型
     * @param <T> 真实类型
     * @return 对应的封装类型的类实例，如果targetClass不是基础类型，则返回 targetClass。
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> primitiveTypeTranslator(Class<?> targetClass) {
        if (!targetClass.isPrimitive()) {
            return (Class<T>) targetClass;
        }
        return (Class<T>) PRIMITIVE_TYPES_MAPPING_WRAPPED_TYPES.get(targetClass);
    }

}
