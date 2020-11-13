package com.iwuyc.tools.beans.converter.object;

import com.iwuyc.tools.beans.converter.TypeConverter;

import java.util.Collection;
import java.util.Collections;

public abstract class ObjectConverter<T> implements TypeConverter<Object, T> {
    @Override
    public Collection<Class<?>> sourceType() {
        return Collections.singleton(Object.class);
    }
}
