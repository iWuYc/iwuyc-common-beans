package com.iwuyc.tools.beans.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public interface TypeConverter<S, T> {
    T convert(S source, Class<? extends T> targetClass);

    Collection<Class<? extends S>> sourceType();

    default boolean support(Class<? extends T> targetClass) {
        final Set<Class<?>> supportClass = getSupportClass();
        final Class<?> innerTargetClass = ConverterUtils.primitiveTypeTranslator(targetClass);
        if (supportClass.contains(innerTargetClass)) {
            return true;
        }
        synchronized (this) {
            if (supportClass.contains(innerTargetClass)) {
                return true;
            }
            for (Class<?> clazz : supportClass) {
                if (clazz.isAssignableFrom(innerTargetClass)) {
                    supportClass.add(innerTargetClass);
                    return true;
                }
            }
        }
        return false;
    }

    default Set<Class<?>> getSupportClass() {
        return Collections.emptySet();
    }
}
