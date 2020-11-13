package com.iwuyc.tools.beans.converter;

import java.util.Collection;

public interface TypeConverter<S, T> {
    T convert(S source, Class<? extends T> targetClass);

    Collection<Class<? extends S>> sourceType();

    boolean support(Class<? extends T> targetClass);
}
