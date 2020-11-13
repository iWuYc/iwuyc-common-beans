package com.iwuyc.tools.beans.converter.string;

import com.google.common.collect.Sets;
import com.iwuyc.tools.beans.converter.PrimitiveTypeConstants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class String2IntegerConverter extends StringConverter<Number> {
    private static final Set<Class<? extends Number>> SUPPORT_TARGET_TYPE =
            Sets.newConcurrentHashSet(Arrays.asList(Float.class, BigDecimal.class, AtomicLong.class, Long.class,
                    Double.class, AtomicInteger.class, Short.class, BigInteger.class, Byte.class, Integer.class));


    @Override
    public Number convert(String source, Class<? extends Number> targetClass) {
        final Class<? extends Number> innerTargetClass = primitiveTypeTranslator(targetClass);
        if (Float.class.isAssignableFrom(innerTargetClass)) {
            return floatConvert(source, innerTargetClass);
        } else if (BigDecimal.class.isAssignableFrom(innerTargetClass)) {
            return bigDecimalConverter(source, innerTargetClass);
        } else if (AtomicLong.class.isAssignableFrom(innerTargetClass)) {
            return atomicLongConverter(source, innerTargetClass);
        } else if (Long.class.isAssignableFrom(innerTargetClass)) {
            return longConverter(source, innerTargetClass);
        } else if (Double.class.isAssignableFrom(innerTargetClass)) {
            return doubleConverter(source, innerTargetClass);
        } else if (AtomicInteger.class.isAssignableFrom(innerTargetClass)) {
            return atomicIntegerConverter(source, innerTargetClass);
        } else if (Short.class.isAssignableFrom(innerTargetClass)) {
            return shortConverter(source, innerTargetClass);
        } else if (BigInteger.class.isAssignableFrom(innerTargetClass)) {
            return bigIntegerConverter(source, innerTargetClass);
        } else if (Byte.class.isAssignableFrom(innerTargetClass)) {
            return byteConverter(source, innerTargetClass);
        }
        return integerConverter(source, innerTargetClass);
    }

    @SuppressWarnings("unchecked")
//    private Class<? extends Number> primitiveTypeTranslator(Class<? extends Number> targetClass) {
//        final Class<? extends Number> innerTargetClass;
//        if (targetClass.isPrimitive()) {
//            innerTargetClass = (Class<? extends Number>) PrimitiveTypeConstants.PRIMITIVE_TYPES_MAPPING_WRAPPED_TYPES.get(targetClass);
//        } else {
//            innerTargetClass = targetClass;
//        }
//        return innerTargetClass;
//    }

    private Integer integerConverter(String source, Class<? extends Number> targetClass) {
        return Integer.parseInt(source);
    }

    private Byte byteConverter(String source, Class<? extends Number> targetClass) {
        return Byte.parseByte(source);
    }

    private BigInteger bigIntegerConverter(String source, Class<? extends Number> targetClass) {
        return new BigInteger(source);
    }

    private Short shortConverter(String source, Class<? extends Number> targetClass) {
        return Short.parseShort(source);
    }

    private AtomicInteger atomicIntegerConverter(String source, Class<? extends Number> targetClass) {
        final Integer integer = integerConverter(source, Integer.class);
        return new AtomicInteger(integer);
    }

    private Number doubleConverter(String source, Class<? extends Number> targetClass) {
        return Double.parseDouble(source);
    }

    private AtomicLong atomicLongConverter(String source, Class<? extends Number> targetClass) {
        long initVal = longConverter(source, Long.class);
        return new AtomicLong(initVal);
    }

    private long longConverter(String source, Class<? extends Number> longClass) {
        return Long.parseLong(source);
    }

    private Number bigDecimalConverter(String source, Class<? extends Number> targetClass) {
        return new BigDecimal(source);
    }

    private float floatConvert(String source, Class<? extends Number> targetClass) {
        return Float.parseFloat(source);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean support(Class<? extends Number> targetClass) {
        final Class<? extends Number> innerTargetClass = primitiveTypeTranslator(targetClass);
        if (SUPPORT_TARGET_TYPE.contains(innerTargetClass)) {
            return true;
        }
        synchronized (SUPPORT_TARGET_TYPE) {
            if (SUPPORT_TARGET_TYPE.contains(innerTargetClass)) {
                return true;
            }
            for (Class<? extends Number> clazz : SUPPORT_TARGET_TYPE) {
                if (clazz.isAssignableFrom(innerTargetClass)) {
                    SUPPORT_TARGET_TYPE.add(innerTargetClass);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected Set<Class<? extends Number>> getSupportClass() {
        return SUPPORT_TARGET_TYPE;
    }
}
