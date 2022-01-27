package com.iwuyc.tools.beans.converter.bo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 功能说明
 *
 * @author 吴宇春
 * @version 1.0.0
 * @since 2021.4
 */
public class OrderBoTest {
    @Test
    public void compare() {
        final OrderBo<String> number1 = new OrderBo<>(1, "1");
        final OrderBo<String> otherNumber1 = new OrderBo<>(1, "1");
        final OrderBo<String> numberSame1 = new OrderBo<>(1, "same1");
        final OrderBo<String> number2 = new OrderBo<>(2, "2");
        assertEquals(number1.getOrder(), 1);
        assertEquals(number1.getData(), "1");
        assertTrue(number1.compareTo(number2) < 0);
        assertTrue(number2.compareTo(number1) > 0);
        assertEquals(0, number1.compareTo(numberSame1));
        assertEquals(number1, otherNumber1);
        assertEquals(number1.hashCode(), otherNumber1.hashCode());

    }
}