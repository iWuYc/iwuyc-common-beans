package com.iwuyc.tools.beans.converter;

import com.google.common.base.Stopwatch;
import com.iwuyc.tools.beans.converter.exception.UnknownUnitException;
import com.iwuyc.tools.commons.basic.type.TimeTuple;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

public class ConverterUtilsTest {
    @Test
    public void stringToNumber() {
        final Number convert = ConverterUtils.convert(1, Number.class);
        assertEquals(convert, 1);
        //Float.class, BigDecimal.class, AtomicLong.class, Long.class, Double.class, AtomicInteger.class, Short.class, BigInteger.class, Byte.class, Integer.class
        String src = "1";
        Float floatNum = ConverterUtils.convert(src, float.class);
        assertEquals(1f, floatNum, 0.0);

        floatNum = ConverterUtils.convert(src, Float.class);
        assertEquals(1f, floatNum, 0.0);

        BigDecimal bigDecimalNum = ConverterUtils.convert(src, BigDecimal.class);
        assertEquals(bigDecimalNum, new BigDecimal("1"));
        bigDecimalNum = ConverterUtils.convert(src, MyBigDecimal.class);
        assertEquals(bigDecimalNum, new BigDecimal("1"));
        final BigInteger bigInteger = ConverterUtils.convert(src, BigInteger.class);
        assertEquals(bigInteger, new BigInteger("1"));

        Long longNum = ConverterUtils.convert(src, Long.class);
        assertEquals(longNum, 1L, 0);
        longNum = ConverterUtils.convert(src, long.class);
        assertEquals(longNum, 1L, 0);

        Double doubleNum = ConverterUtils.convert(src, Double.class);
        assertEquals(doubleNum, 1d, 0.0);
        doubleNum = ConverterUtils.convert(src, double.class);
        assertEquals(doubleNum, 1d, 0.0);

        final AtomicLong atomicLong = ConverterUtils.convert(src, AtomicLong.class);
        assertEquals(atomicLong.get(), 1);
        final AtomicInteger atomicInteger = ConverterUtils.convert(src, AtomicInteger.class);
        assertEquals(atomicInteger.get(), 1);

        Short shortNum = ConverterUtils.convert(src, Short.class);
        assertEquals(shortNum, 1, 0.0);
        shortNum = ConverterUtils.convert(src, short.class);
        assertEquals(shortNum, 1, 0.0);

        Byte byteNum = ConverterUtils.convert(src, Byte.class);
        assertEquals(byteNum, 1, 0);
        byteNum = ConverterUtils.convert(src, byte.class);
        assertEquals(byteNum, 1, 0);

        Integer integer = ConverterUtils.convert(src, Integer.class);
        assertEquals(integer, 1, 0);
        integer = ConverterUtils.convert(src, int.class);
        assertEquals(integer, 1, 0);
    }

    @Test
    @Ignore("Skip performance test!!")
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

    private void single(String src) {
        final String convert = ConverterUtils.convert(123, String.class);
        assertEquals(convert, "123");
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

    @Test
    public void string2TimeTuple() {
        TimeTuple timeUnit = ConverterUtils.convert("1", TimeTuple.class);
        assertEquals(timeUnit, TimeTuple.create(1, TimeUnit.SECONDS));

        timeUnit = ConverterUtils.convert("1ns", TimeTuple.class);
        assertEquals(timeUnit, TimeTuple.create(1, TimeUnit.NANOSECONDS));

        timeUnit = ConverterUtils.convert("1ms", TimeTuple.class);
        assertEquals(timeUnit, TimeTuple.create(1, TimeUnit.MILLISECONDS));

        timeUnit = ConverterUtils.convert("1s", TimeTuple.class);
        assertEquals(timeUnit, TimeTuple.create(1, TimeUnit.SECONDS));

        timeUnit = ConverterUtils.convert("1m", TimeTuple.class);
        assertEquals(timeUnit, TimeTuple.create(1, TimeUnit.MINUTES));

        timeUnit = ConverterUtils.convert("1h", TimeTuple.class);
        assertEquals(timeUnit, TimeTuple.create(1, TimeUnit.HOURS));

        timeUnit = ConverterUtils.convert("1d", TimeTuple.class);
        assertEquals(timeUnit, TimeTuple.create(1, TimeUnit.DAYS));

        try {
            timeUnit = ConverterUtils.convert("1day", TimeTuple.class);
            assertEquals(timeUnit, TimeTuple.create(1, TimeUnit.DAYS));
        } catch (UnknownUnitException e) {
            assertEquals(e.getClass(), UnknownUnitException.class);
        }
    }

    public static class MyBigDecimal extends BigDecimal {

        public MyBigDecimal(String in) {
            super(in);
        }
    }
}