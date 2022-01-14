package com.iwuyc.tools.beans.converter;

import com.iwuyc.tools.beans.converter.string.String2NumberConverter;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class String2NumberConverterTest {
    @Test
    public void name() {
        final String2NumberConverter string2NumberConverter = new String2NumberConverter();
        System.out.println(string2NumberConverter.support(MyInt.class));
        System.out.println(string2NumberConverter.support(MyInt.class));
    }

    public static class MyInt extends AtomicInteger {

    }
}