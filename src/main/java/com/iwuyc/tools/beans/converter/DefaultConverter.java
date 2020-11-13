package com.iwuyc.tools.beans.converter;

import java.util.Collection;
import java.util.Collections;

public class DefaultConverter<T> implements TypeConverter<T, T> {
    @Override
    public T convert(T source, Class<? extends T> targetClass) {
        return source;
    }

    @Override
    public Collection<Class<? extends T>> sourceType() {
        return Collections.emptyList();
    }

    @Override
    public boolean support(Class<? extends T> targetClass) {
        return true;
    }
}
