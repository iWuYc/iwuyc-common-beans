package com.iwuyc.tools.beans.converter.object;

import com.iwuyc.tools.beans.pojo.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 功能说明
 *
 * @author 吴宇春
 * @version 1.0.0
 * @since 2021.4
 */
public class Object2StringConverterTest {

    private Object2StringConverter converter;

    @Before
    public void setUp() {
        converter = new Object2StringConverter();
    }

    @Test
    public void convert() {
        final Person person = Person.createPerson();
        Assert.assertTrue(converter.support(String.class));
        final String convert = converter.convert(person, String.class);
        Assert.assertEquals(convert, "Person(name=Neil, address=China, age=10, id=1, serialNo=1)");
    }

    @Test
    public void support() {

    }
}