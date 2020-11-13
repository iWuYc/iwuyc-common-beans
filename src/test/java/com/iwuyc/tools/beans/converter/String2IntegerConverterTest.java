package com.iwuyc.tools.beans.converter;

import com.iwuyc.tools.beans.converter.string.String2IntegerConverter;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class String2IntegerConverterTest {
    @Test
    public void name() {
        final String2IntegerConverter string2IntegerConverter = new String2IntegerConverter();
        System.out.println(string2IntegerConverter.support(MyInt.class));
        System.out.println(string2IntegerConverter.support(MyInt.class));
    }

    public static class MyInt extends AtomicInteger {

    }
}