/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.iwuyc.tools.beans.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Bean中属性字段、getter、setter方法的缓存对象
 *
 * @author Neil
 * @version 2021.4
 * @since 2021.4
 */
@SuppressWarnings("rawtypes")
@Slf4j
public class BeanFieldMap extends HashMap<String, BeanFieldMap.GetterAndSetter> {
    private static final Set<String> IGNORE_FIELD = Collections.unmodifiableSet(new HashSet<>(Collections.singletonList("__$lineHits$__")));

    public BeanFieldMap(int length) {
        super(length);
    }

    @SuppressWarnings("unchecked")
    public static BeanFieldMap create(Class clazz) {
        final Field[] declaredFields = clazz.getDeclaredFields();
        BeanFieldMap result = new BeanFieldMap(declaredFields.length);

        for (Field field : declaredFields) {
            final String fieldName = field.getName();
            if (IGNORE_FIELD.contains(fieldName)) {
                continue;
            }
            final char firstChar = Character.toUpperCase(fieldName.charAt(0));
            final String newFieldName = firstChar + fieldName.substring(1);

            String getterName = "get" + newFieldName;
            String setterName = "set" + newFieldName;
            final Class<?> fieldType = field.getType();

            GetterAndSetter getterAndSetter = new GetterAndSetter();
            getterAndSetter.setFieldType(fieldType);
            try {
                final Method getter = clazz.getMethod(getterName);
                getterAndSetter.getter(getter);
            } catch (NoSuchMethodException e) {
                log.warn("未找到指定名字的getter方法:{}", getterName);
            }
            try {
                final Method setter = clazz.getMethod(setterName, fieldType);
                getterAndSetter.setter(setter);
            } catch (NoSuchMethodException e) {
                log.warn("未找到指定名字的setter方法:{}", setterName);
            }
            result.put(fieldName, getterAndSetter);
        }

        return result;
    }


    public Optional<Method> setter(String fieldName) {
        final GetterAndSetter getterAndSetter = this.get(fieldName);
        if (null == getterAndSetter) {
            return Optional.empty();
        }
        return Optional.ofNullable(getterAndSetter.setter());
    }

    public Optional<Method> getter(String fieldName) {
        final GetterAndSetter getterAndSetter = this.get(fieldName);
        if (null == getterAndSetter) {
            return Optional.empty();
        }
        return Optional.ofNullable(getterAndSetter.getter());
    }

    public Optional<Class> fieldType(String fieldName) {
        final GetterAndSetter getterAndSetter = this.get(fieldName);
        if (null == getterAndSetter) {
            return Optional.empty();
        }
        return Optional.ofNullable(getterAndSetter.getFieldType());
    }


    public static class GetterAndSetter {
        private Method getter;
        private Method setter;
        @Getter
        @Setter
        private Class fieldType;

        public Method getter() {
            return getter;
        }

        public void getter(Method getter) {
            this.getter = getter;
        }

        public Method setter() {
            return setter;
        }

        public void setter(Method setter) {
            this.setter = setter;
        }
    }
}
