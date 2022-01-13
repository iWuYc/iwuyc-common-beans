/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.iwuyc.tools.beans.pojo;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 功能说明
 *
 * @author 吴宇春
 * @version 1.0.0
 * @date 2022/1/13
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class BeanMap<T> implements Map<String, Object> {
    private static final LoadingCache<Class, BeanFieldMap> BEAN_FIELD_MAP_BY_TYPE = CacheBuilder.newBuilder().build(new CacheLoader<Class, BeanFieldMap>() {
        @Override
        public BeanFieldMap load(@Nonnull Class key) throws Exception {
            return BeanFieldMap.create(key);
        }
    });
    private final T rawObject;
    private final BeanFieldMap beanFieldMap;

    public BeanMap(T rawObject) {
        this.rawObject = rawObject;
        beanFieldMap = BEAN_FIELD_MAP_BY_TYPE.getUnchecked(this.rawObject.getClass());
    }

    public static <T> BeanMap<T> create(T rawObject) {
        return new BeanMap<>(rawObject);
    }

    @Override
    public int size() {
        return beanFieldMap.size();
    }

    @Override
    public boolean isEmpty() {
        return beanFieldMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.beanFieldMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry<String, BeanFieldMap.GetterAndSetter> item : this.beanFieldMap.entrySet()) {
            final Method getter = item.getValue().getter();
            try {
                final Object val = getter.invoke(this.rawObject);
                return Objects.equals(val, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        if (null == key) {
            return null;
        }
        final String fieldName = String.valueOf(key);
        final Optional<Method> getter = this.beanFieldMap.getter(fieldName);
        if (!getter.isPresent()) {
            return null;
        }
        try {
            return getter.get().invoke(this.rawObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object put(String key, Object value) {
        final String fieldName = Preconditions.checkNotNull(key, "key不允许为null");
        final Object oldVal = get(fieldName);
        final Optional<Method> setterOpt = this.beanFieldMap.setter(fieldName);
        if (!setterOpt.isPresent()) {
            return null;
        }
        try {
            setterOpt.get().invoke(this.rawObject, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return oldVal;
    }


    @Override
    public Object remove(Object key) {
        final String fieldName = String.valueOf(Preconditions.checkNotNull(key, "key不允许为null"));
        return put(fieldName, null);
    }

    @Override
    public void putAll(Map m) {
        final Set<Entry> set = m.entrySet();
        for (Entry o : set) {
            final Object key = o.getKey();
            if (null == key) {
                continue;
            }
            String fieldName = key.toString();
            put(fieldName, o.getValue());
        }
    }

    @Override
    public void clear() {
        final Set<String> fieldNames = this.beanFieldMap.keySet();
        for (String fieldName : fieldNames) {
            remove(fieldName);
        }
    }

    @Override
    public Set<String> keySet() {
        return this.beanFieldMap.keySet();
    }

    @Override
    public Collection values() {
        Collection result = new ArrayList();
        final Set<String> fieldNames = keySet();
        for (String fieldName : fieldNames) {
            result.add(get(fieldName));
        }
        return result;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> result = new HashSet<>();
        for (String fieldName : keySet()) {
            result.add(new Entry<String, Object>() {
                @Override
                public String getKey() {
                    return fieldName;
                }

                @Override
                public Object getValue() {
                    return get(fieldName);
                }

                @Override
                public Object setValue(Object value) {
                    return put(fieldName, value);
                }
            });
        }
        return result;
    }

    public Class<?> getPropertyType(String key) {
        return this.beanFieldMap.get(key).getFieldType();
    }
}
