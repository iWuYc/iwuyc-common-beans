package com.iwuyc.tools.beans.converter;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public interface TypeConverter<S, T> {
    T convert(S source, Class<? extends T> targetClass);

    Collection<Class<? extends S>> sourceType();

    default boolean support(Class<? extends T> targetClass) {
        final Set<Class<? extends T>> supportClass = getSupportClass();
        final Class<? extends T> innerTargetClass = TypeUtils.primitiveTypeTranslator(targetClass);
        if (supportClass.contains(innerTargetClass)) {
            return true;
        }
        synchronized (this) {
            if (supportClass.contains(innerTargetClass)) {
                return true;
            }
            for (Class<? extends T> clazz : supportClass) {
                if (clazz.isAssignableFrom(innerTargetClass)) {
                    supportClass.add(innerTargetClass);
                    return true;
                }
            }
        }
        return false;
    }

    default Set<Class<? extends T>> getSupportClass() {
        return Collections.emptySet();
    }
}
