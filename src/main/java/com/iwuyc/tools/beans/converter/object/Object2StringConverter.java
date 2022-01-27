package com.iwuyc.tools.beans.converter.object;

import com.iwuyc.tools.commons.annotaion.Order;

import java.util.Collections;
import java.util.Set;

@Order(0)
public class Object2StringConverter extends ObjectConverter<String> {
    @Override
    public String convert(Object source, Class<? extends String> targetClass) {
        return String.valueOf(source);
    }

    @Override
    public Set<Class<? extends String>> getSupportClass() {
        return Collections.singleton(String.class);
    }
}
