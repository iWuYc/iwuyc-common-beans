package com.iwuyc.tools.beans.converter.string;

import com.iwuyc.tools.beans.converter.PrimitiveTypeConstants;
import com.iwuyc.tools.beans.converter.TypeConverter;
import com.iwuyc.tools.beans.converter.TypeUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class StringConverter<T> implements TypeConverter<String, T> {
    @Override
    public Collection<Class<? extends String>> sourceType() {
        return Collections.singleton(String.class);
    }

    @Override
    public boolean support(Class<? extends T> targetClass) {
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

    protected abstract Set<Class<? extends T>> getSupportClass();
}
