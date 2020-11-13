package com.iwuyc.tools.beans.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PrimitiveTypeConstants {
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
}
