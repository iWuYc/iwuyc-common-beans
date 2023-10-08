/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.iwuyc.tools.beans.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * 数据对象类
 *
 * @author Neil
 * @version 2022.1
 * @since 2022.1
 */
@Data
@ToString
public class Person {
    private String name;
    private String address;
    private int age;
    private Integer id;
    private long serialNo;

    public static Person createPerson() {
        Person person = new Person();
        person.setId(1);
        person.setAge(10);
        person.setName("Neil");
        person.setAddress("China");
        person.setSerialNo(1L);
        return person;
    }

    private int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int age() {
        return age;
    }
}
