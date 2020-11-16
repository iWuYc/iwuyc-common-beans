package com.iwuyc.tools.beans.converter.string;

import com.google.common.collect.Sets;
import com.iwuyc.tools.beans.converter.ConverterUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class String2IntegerConverter extends StringConverter<Number> {
    private static final Set<Class<?>> SUPPORT_TARGET_TYPE =
            Sets.newConcurrentHashSet(Arrays.asList(Float.class, BigDecimal.class, AtomicLong.class, Long.class,
                    Double.class, AtomicInteger.class, Short.class, BigInteger.class, Byte.class, Integer.class));


    @SuppressWarnings("unchecked")
    @Override
    public Number convert(String source, Class<? extends Number> targetClass) {
        final Class<? extends Number> innerTargetClass = (Class<? extends Number>) ConverterUtils.primitiveTypeTranslator(targetClass);
        if (Float.class.isAssignableFrom(innerTargetClass)) {
            return floatConvert(source);
        } else if (BigDecimal.class.isAssignableFrom(innerTargetClass)) {
            return bigDecimalConverter(source);
        } else if (AtomicLong.class.isAssignableFrom(innerTargetClass)) {
            return atomicLongConverter(source);
        } else if (Long.class.isAssignableFrom(innerTargetClass)) {
            return longConverter(source);
        } else if (Double.class.isAssignableFrom(innerTargetClass)) {
            return doubleConverter(source);
        } else if (AtomicInteger.class.isAssignableFrom(innerTargetClass)) {
            return atomicIntegerConverter(source);
        } else if (Short.class.isAssignableFrom(innerTargetClass)) {
            return shortConverter(source);
        } else if (BigInteger.class.isAssignableFrom(innerTargetClass)) {
            return bigIntegerConverter(source);
        } else if (Byte.class.isAssignableFrom(innerTargetClass)) {
            return byteConverter(source);
        }
        return integerConverter(source);
    }

    private Integer integerConverter(String source) {
        return Integer.parseInt(source);
    }

    private Byte byteConverter(String source) {
        return Byte.parseByte(source);
    }

    private BigInteger bigIntegerConverter(String source) {
        return new BigInteger(source);
    }

    private Short shortConverter(String source) {
        return Short.parseShort(source);
    }

    private AtomicInteger atomicIntegerConverter(String source) {
        final Integer integer = integerConverter(source);
        return new AtomicInteger(integer);
    }

    private Number doubleConverter(String source) {
        return Double.parseDouble(source);
    }

    private AtomicLong atomicLongConverter(String source) {
        long initVal = longConverter(source);
        return new AtomicLong(initVal);
    }

    private long longConverter(String source) {
        return Long.parseLong(source);
    }

    private Number bigDecimalConverter(String source) {
        return new BigDecimal(source);
    }

    private float floatConvert(String source) {
        return Float.parseFloat(source);
    }

    @Override
    public Set<Class<?>> getSupportClass() {
        return SUPPORT_TARGET_TYPE;
    }
}
