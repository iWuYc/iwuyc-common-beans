package com.iwuyc.tools.beans;

import com.iwuyc.tools.beans.pojo.BeanMap;
import com.iwuyc.tools.beans.pojo.Person;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能说明
 *
 * @author 吴宇春
 * @version 1.0.0
 * @since 2022.1
 */
public class BeanUtilsTest {

    @Test
    public void setter() {
        final Person person = Person.createPerson();
        final BeanMap<Person> personBeanMap = BeanMap.create(person);
        final Map<String, Object> property = new HashMap<>(personBeanMap);
        property.put("nonProperty", "non");
        Person newPerson = new Person();
        BeanUtils.setter(newPerson, property);
        System.out.println(newPerson);
    }
}