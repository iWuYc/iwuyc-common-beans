package com.iwuyc.tools.beans.pojo;

import lombok.Data;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * 功能说明
 *
 * @author Neil
 * @version 1.0.0
 * @since 2022.1
 */
public class BeanMapTest {

    @Test
    public void name() {
        final Person person = new Person();

        Map<String, Object> properties = new HashMap<>();
        final String name = "Neil";
        properties.put("name", name);
        final String address = "China";
        properties.put("address", address);
        final int age = 10;
        properties.put("age", age);
        properties.put(null, "name");

        final BeanMap<Person> personBeanMap = BeanMap.create(person);
        personBeanMap.putAll(properties);
        assertNull(personBeanMap.put("nonField", "nonField"));
        assertEquals(person.getName(), name);
        assertEquals(person.getAddress(), address);
        assertEquals(person.age(), 10);

        final Object nameByMap = personBeanMap.get("name");
        assertEquals(name, nameByMap);

        final Object addressByMap = personBeanMap.get("address");
        assertEquals(address, addressByMap);

        final Object ageByMap = personBeanMap.get("age");
        assertNull(ageByMap);

        final Object nameByMapOld = personBeanMap.put("name", null);
        assertEquals(name, nameByMapOld);
        final Object nameByMapNew = personBeanMap.get("name");
        assertNull(nameByMapNew);

        final Object nonProperty = personBeanMap.get(null);
        assertNull(nonProperty);
    }

    @Test
    public void valuesAndKeySet() {
        Person person = new Person();
        BeanMap<Person> personBeanMap = BeanMap.create(person);
        final Set<String> fieldNames = personBeanMap.keySet();
        final Collection<String> allName = Arrays.asList("age", "name", "address");
        for (String name : allName) {
            assertTrue(fieldNames.contains(name));
        }
        person = Person.createPerson();
        personBeanMap = BeanMap.create(person);

        final Collection<?> values = personBeanMap.values();
        final List<? extends Serializable> allValues = Arrays.asList("Neil", "China");
        for (Serializable val : allValues) {
            assertTrue(values.contains(val));
        }

        final Object age = personBeanMap.remove("age");
        assertNull(age);

        final Object id = personBeanMap.remove("id");
        assertEquals(id, 1);
        assertNull(person.getId());

        final Object serialNo = personBeanMap.remove("serialNo");
        assertEquals(serialNo, 1L);
        assertEquals(0, person.getSerialNo());

        person = Person.createPerson();
        personBeanMap = BeanMap.create(person);
        for (Map.Entry<String, Object> item : personBeanMap.entrySet()) {
            final String key = item.getKey();
            if ("name".equals(key)) {
                item.setValue("Jack");
                break;
            }
        }

        assertEquals(person.getName(), "Jack");
        assertEquals(personBeanMap.get("name"), "Jack");
    }

    @Test
    public void size() {
        final Person person = new Person();
        final BeanMap<Person> personBeanMap = BeanMap.create(person);
        assertEquals(personBeanMap.size(), 5);
    }

    @Test
    public void clear() {
        Person person = Person.createPerson();
        Map<String, Object> properties = new HashMap<>();
        properties.put("id", 1);
        properties.put("age", null);
        properties.put("name", "Neil");
        properties.put("address", "China");
        properties.put("serialNo", 1L);
        final BeanMap<Person> personBeanMap = BeanMap.create(person);
        for (Map.Entry<String, Object> item : personBeanMap.entrySet()) {
            final String key = item.getKey();
            if (!properties.containsKey(key)) {
                continue;
            }
            final Object value = item.getValue();
            assertEquals(value, properties.get(key));
        }

        personBeanMap.clear();
        assertNull(person.getId());
        assertEquals(person.age(), 0);
        assertNull(person.getName());
        assertNull(person.getAddress());
        assertEquals(person.getSerialNo(), 0L);

    }

    @Test
    public void propertyType() {
        Person person = Person.createPerson();
        final BeanMap<Person> personBeanMap = BeanMap.create(person);

        final Optional<Class<?>> idClazz = personBeanMap.getPropertyType("id");
        assertTrue(idClazz.isPresent() && idClazz.get().equals(Integer.class));

        final Optional<Class<?>> ageClazzOpt = personBeanMap.getPropertyType("age");
        assertTrue(ageClazzOpt.isPresent() && ageClazzOpt.get().equals(int.class));


        final Optional<Class<?>> serialNoClazzOpt = personBeanMap.getPropertyType("serialNo");
        assertTrue(serialNoClazzOpt.isPresent() && serialNoClazzOpt.get().equals(long.class));

        final Optional<Class<?>> nameClazzOpt = personBeanMap.getPropertyType("name");
        assertTrue(nameClazzOpt.isPresent() && nameClazzOpt.get().equals(String.class));

        final Optional<Class<?>> nonPropertyClazzOpt = personBeanMap.getPropertyType("nonProperty");
        assertFalse(nonPropertyClazzOpt.isPresent());
    }

    @Test
    public void contains() {
        final Person person = Person.createPerson();
        final BeanMap<Person> personBeanMap = BeanMap.create(person);
        assertTrue(personBeanMap.containsKey("name"));
        assertTrue(personBeanMap.containsValue("Neil"));

        assertFalse(personBeanMap.containsKey("nonProperty"));
        assertFalse(personBeanMap.containsValue("nonValue"));

        final EmptyDTO emptyDTO = new EmptyDTO();
        final BeanMap<EmptyDTO> emptyDTOBeanMap = BeanMap.create(emptyDTO);
        assertTrue(emptyDTOBeanMap.isEmpty());

    }

    @Data
    private static class EmptyDTO {

    }
}