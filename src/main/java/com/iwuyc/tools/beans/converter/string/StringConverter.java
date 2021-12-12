package com.iwuyc.tools.beans.converter.string;

import com.iwuyc.tools.beans.converter.TypeConverter;

import java.util.Collection;
import java.util.Collections;

public abstract class StringConverter<T> implements TypeConverter<String, T> {
    @Override
    public Collection<Class<? extends String>> sourceType() {
        return Collections.singleton(String.class);
    }

}
