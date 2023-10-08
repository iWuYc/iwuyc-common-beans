/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.iwuyc.tools.beans.pojo;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 对符合Pojo规范的类的属性进行获取、设置的操作。
 * example:
 * <pre>
 *
 *     public class Person {
 *         private String name;
 *         private int age;
 *     }
 *     public static void main(String[]args){
 *         Person p = new Person();
 *         BeanMap&lt;Person&gt; pMap = BeanMap.create(p);
 *         pMap.put("name","Neil");
 *         pMap.get("name"); // will return Neil
 *     }
 * </pre>
 *
 * @author 吴宇春
 * @version 1.0.0
 * @since 2021.4
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
public class BeanMap<T> implements Map<String, Object> {
    private static final LoadingCache<Class, BeanFieldMap> BEAN_FIELD_MAP_BY_TYPE = CacheBuilder.newBuilder().build(new CacheLoader<Class, BeanFieldMap>() {
        @Override
        @Nonnull
        public BeanFieldMap load(@Nonnull Class key) {
            return BeanFieldMap.create(key);
        }
    });
    private static final Map<Class<?>, Object> PRIMITIVE_INIT_VAL = new HashMap<>();

    static {

        PRIMITIVE_INIT_VAL.put(byte.class, 0);
        PRIMITIVE_INIT_VAL.put(short.class, 0);
        PRIMITIVE_INIT_VAL.put(int.class, 0);
        PRIMITIVE_INIT_VAL.put(long.class, 0L);

        PRIMITIVE_INIT_VAL.put(float.class, 0.0f);
        PRIMITIVE_INIT_VAL.put(double.class, 0.0d);

        PRIMITIVE_INIT_VAL.put(boolean.class, false);
        PRIMITIVE_INIT_VAL.put(char.class, 0);
    }

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
        for (String item : this.beanFieldMap.keySet()) {
            final Object val = get(item);
            if (Objects.equals(val, value)) {
                return true;
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
            log.info("Field[{}] not found getter method.", fieldName);
            return null;
        }
        return get(fieldName, getter.get());
    }

    @Override
    public Object put(String key, Object value) {
        final String fieldName = Preconditions.checkNotNull(key, "key不允许为null");
        final Object oldVal = get(fieldName);
        final Optional<Method> setterOpt = this.beanFieldMap.setter(fieldName);
        if (!setterOpt.isPresent()) {
            log.info("the field[{}] can't write!", fieldName);
            return null;
        }
        try {
            final Object valWillSet;

            if (null == value) {
                final Optional<Class> aClass = this.beanFieldMap.fieldType(key);
                if (aClass.isPresent()) {
                    final Class clazz = aClass.get();
                    valWillSet = clazz.isPrimitive() ? PRIMITIVE_INIT_VAL.get(clazz) : value;
                } else {
                    valWillSet = null;
                }
            } else {
                valWillSet = value;
            }
            final Method setterMethod = setterOpt.get();
            setterMethod.invoke(this.rawObject, valWillSet);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.warn("write field[{}]write raise an error.", fieldName, e);
        }
        return oldVal;
    }


    /**
     * 删除某个字段的值，如果是原始类型，则会设置为该原始类型的默认值。
     *
     * @param key 将要移除的字段名
     * @return 移除前的值
     */
    @Override
    public Object remove(Object key) {
        final String fieldName = String.valueOf(Preconditions.checkNotNull(key, "key不允许为null"));
        return put(fieldName, null);
    }

    /**
     * 将所有的字段设置到指定的类中
     *
     * @param properties 将要设置的字段与值
     */
    @Override
    public void putAll(Map properties) {
        final Set<Entry> set = properties.entrySet();
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
        for (Entry<String, BeanFieldMap.GetterAndSetter> item : this.beanFieldMap.entrySet()) {
            final String fieldName = item.getKey();
            final BeanFieldMap.GetterAndSetter getterAndSetter = item.getValue();
            final Method getter = getterAndSetter.getter();
            if (null == getter) {
                continue;
            }
            final Object val = get(fieldName, getter);
            result.add(val);
        }
        return result;
    }

    private Object get(String fieldName, @Nonnull Method getter) {
        try {
            return getter.invoke(this.rawObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.warn("read field[{}] raise an error.", fieldName, e);
        }
        return null;
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

    public Optional<Class<?>> getPropertyType(String key) {
        final BeanFieldMap.GetterAndSetter getterAndSetter = this.beanFieldMap.get(key);
        if (null == getterAndSetter) {
            return Optional.empty();
        }
        return Optional.ofNullable(getterAndSetter.getFieldType());
    }
}
