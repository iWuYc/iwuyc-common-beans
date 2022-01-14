package com.iwuyc.tools.beans.converter.object;

import com.iwuyc.tools.commons.annotaion.Order;

@Order(0)
public class Object2StringConverter extends ObjectConverter<String> {
    @Override
    public String convert(Object source, Class<? extends String> targetClass) {
        return String.valueOf(source);
    }

    @Override
    public boolean support(Class<? extends String> targetClass) {
        return targetClass == String.class;
    }
}
