package com.iwuyc.tools.beans.converter.bo;

import org.junit.Assert;
import org.junit.Test;

/**
 * 功能说明
 *
 * @author 吴宇春
 * @version 1.0.0
 * @since 2021.4
 */
public class TypeTupleTest {
    @Test
    public void name() {
        final TypeTuple typeTuple = new TypeTuple(String.class, Integer.class);
        final TypeTuple typeTupleOther = new TypeTuple(String.class, Integer.class);
        Assert.assertEquals(typeTuple, typeTupleOther);
        Assert.assertEquals(typeTuple, typeTupleOther);
    }
}