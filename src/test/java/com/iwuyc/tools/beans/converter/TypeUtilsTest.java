package com.iwuyc.tools.beans.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TypeUtilsTest {
    @Test
    public void canBeCase() {
        assert  TypeUtils.canBeCase(int.class,Integer.class);
        assert  TypeUtils.canBeCase(Integer.class,Integer.class);
        assert  TypeUtils.canBeCase(Integer.class,int.class);
        assert  TypeUtils.canBeCase(int.class,Number.class);

    }
}