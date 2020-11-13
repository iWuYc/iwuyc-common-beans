package com.iwuyc.tools.beans.converter;

import com.google.common.base.Stopwatch;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class ConverterUtilsTest {
    @Test
    public void stringToNumber() {
        final Number convert = ConverterUtils.convert(1, Number.class);
        System.out.println(convert);
        //Float.class, BigDecimal.class, AtomicLong.class, Long.class, Double.class, AtomicInteger.class, Short.class, BigInteger.class, Byte.class, Integer.class
        String src = "1";
        System.out.println(ConverterUtils.convert(src, float.class));
        System.out.println(ConverterUtils.convert(src, Float.class));
        System.out.println(ConverterUtils.convert(src, BigDecimal.class));
        System.out.println(ConverterUtils.convert(src, AtomicLong.class));
        System.out.println(ConverterUtils.convert(src, Long.class));
        System.out.println(ConverterUtils.convert(src, long.class));
        System.out.println(ConverterUtils.convert(src, Double.class));
        System.out.println(ConverterUtils.convert(src, double.class));
        System.out.println(ConverterUtils.convert(src, AtomicInteger.class));
        System.out.println(ConverterUtils.convert(src, Short.class));
        System.out.println(ConverterUtils.convert(src, short.class));
        System.out.println(ConverterUtils.convert(src, BigInteger.class));
        System.out.println(ConverterUtils.convert(src, Byte.class));
        System.out.println(ConverterUtils.convert(src, byte.class));
        System.out.println(ConverterUtils.convert(src, Integer.class));
        System.out.println(ConverterUtils.convert(src, int.class));

    }

    @Test
    public void objectToString() {

        String src = "1";
        final Stopwatch started1 = Stopwatch.createStarted();
        single(src);
        System.out.println(started1.stop());
        final Stopwatch started = Stopwatch.createStarted();
        for (int i = 0; i < 100_0000; i++) {
            single(src);
        }
        System.out.println(started.stop());
    }

    private final void single(String src) {
        final String convert = ConverterUtils.convert(123, String.class);
        Assert.assertEquals(convert, "123");
        ConverterUtils.convert(src, float.class);
        ConverterUtils.convert(src, Float.class);
        ConverterUtils.convert(src, BigDecimal.class);
        ConverterUtils.convert(src, AtomicLong.class);
        ConverterUtils.convert(src, Long.class);
        ConverterUtils.convert(src, long.class);
        ConverterUtils.convert(src, Double.class);
        ConverterUtils.convert(src, double.class);
        ConverterUtils.convert(src, AtomicInteger.class);
        ConverterUtils.convert(src, Short.class);
        ConverterUtils.convert(src, short.class);
        ConverterUtils.convert(src, BigInteger.class);
        ConverterUtils.convert(src, Byte.class);
        ConverterUtils.convert(src, byte.class);
        ConverterUtils.convert(src, Integer.class);
        ConverterUtils.convert(src, int.class);
    }
}