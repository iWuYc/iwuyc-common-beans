package com.iwuyc.tools.beans.converter.string;

import com.iwuyc.tools.beans.converter.PrimitiveTypeConstants;
import com.iwuyc.tools.beans.converter.TypeConverter;

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
        final Set<Class<? extends T>> SUPPORT_TARGET_TYPE = getSupportClass();
        final Class<? extends T> innerTargetClass = primitiveTypeTranslator(targetClass);
        if (SUPPORT_TARGET_TYPE.contains(innerTargetClass)) {
            return true;
        }
        synchronized (SUPPORT_TARGET_TYPE) {
            if (SUPPORT_TARGET_TYPE.contains(innerTargetClass)) {
                return true;
            }
            for (Class<? extends T> clazz : SUPPORT_TARGET_TYPE) {
                if (clazz.isAssignableFrom(innerTargetClass)) {
                    SUPPORT_TARGET_TYPE.add(innerTargetClass);
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    protected Class<? extends T> primitiveTypeTranslator(Class<? extends T> targetClass) {
        final Class<? extends T> innerTargetClass;
        if (targetClass.isPrimitive()) {
            innerTargetClass =
                    (Class<? extends T>) PrimitiveTypeConstants.PRIMITIVE_TYPES_MAPPING_WRAPPED_TYPES.get(targetClass);
        } else {
            innerTargetClass = targetClass;
        }
        return innerTargetClass;
    }

    protected abstract Set<Class<? extends T>> getSupportClass();
}
