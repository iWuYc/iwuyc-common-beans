/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.iwuyc.tools.beans.converter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.iwuyc.tools.beans.converter.bo.OrderBo;
import com.iwuyc.tools.beans.converter.bo.TypeTuple;
import com.iwuyc.tools.commons.annotaion.Order;
import com.iwuyc.tools.commons.util.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 功能说明
 *
 * @author 吴宇春
 * @version 1.0.0
 * @since 2021.4
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
public class ConverterContainer {
    /**
     * sourceType -> TypeConverter
     */
    private static final Map<Class, List<OrderBo<TypeConverter>>> CONVERTER_BY_SOURCE_TYPE = new ConcurrentHashMap<>();
    private static final LoadingCache<TypeTuple, Optional<TypeConverter>> TYPE_CONVERTER_LOADING_CACHE = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build(new CacheLoader<TypeTuple, Optional<TypeConverter>>() {
        @Override
        @Nonnull
        public Optional<TypeConverter> load(@Nonnull TypeTuple typeTuple) {
            return find(typeTuple);
        }
    });

    static {
        final ServiceLoader<TypeConverter> typeConverters = ServiceLoader.load(TypeConverter.class);
        for (TypeConverter typeConverter : typeConverters) {
            final Class<? extends TypeConverter> typeConverterClass = typeConverter.getClass();
            final Order orderAnno = typeConverterClass.getAnnotation(Order.class);
            final int order;
            if (null == orderAnno) {
                order = 0;
            } else {
                order = orderAnno.value();
            }
            log.debug("loadConverterService:{}", typeConverterClass.getName());
            final Collection<Class> sourceTypes = typeConverter.sourceType();
            for (Class sourceType : sourceTypes) {
                final List<OrderBo<TypeConverter>> typeConvertersInstances = CONVERTER_BY_SOURCE_TYPE.computeIfAbsent(sourceType, key -> new ArrayList<>());
                typeConvertersInstances.add(new OrderBo<>(order, typeConverter));
            }
        }

        final Collection<List<OrderBo<TypeConverter>>> converterList = CONVERTER_BY_SOURCE_TYPE.values();
        for (List<OrderBo<TypeConverter>> item : converterList) {
            Collections.sort(item);
        }
    }


    public static Optional<TypeConverter> find(Class sourceClass, Class targetClass) {
        return TYPE_CONVERTER_LOADING_CACHE.getUnchecked(new TypeTuple(sourceClass, targetClass));
    }

    private static Optional<TypeConverter> find(TypeTuple typeTuple) {
        Class sourceClass = typeTuple.getSource();
        Class targetClass = typeTuple.getTarget();
        Optional<TypeConverter> result;
        Deque<Class<?>> targetClassStack = new ArrayDeque<>();
        targetClassStack.push(sourceClass);
        do {
            Class<?> srcClazzTemp = targetClassStack.pop();

            result = findSupportConverter0(srcClazzTemp, targetClass);
            if (result.isPresent()) {
                break;
            }
            final Class superclass = srcClazzTemp.getSuperclass();
            if (null != superclass) {
                targetClassStack.push(superclass);
            }
            final Class<?>[] interfaces = srcClazzTemp.getInterfaces();
            for (Class interfaze : interfaces) {
                targetClassStack.push(interfaze);
            }
        } while (targetClassStack.size() > 0);
        return result;
    }

    private static Optional<TypeConverter> findSupportConverter0(Class sourceType, Class targetType) {
        List<OrderBo<TypeConverter>> typeConverters = CONVERTER_BY_SOURCE_TYPE.get(sourceType);
        if (null == typeConverters) {
            for (Map.Entry<Class, List<OrderBo<TypeConverter>>> item : CONVERTER_BY_SOURCE_TYPE.entrySet()) {
                final Class registerSrcClass = item.getKey();
                if (registerSrcClass.isAssignableFrom(sourceType)) {
                    final List<OrderBo<TypeConverter>> typeConvertersInstances = CONVERTER_BY_SOURCE_TYPE.computeIfAbsent(sourceType, key -> new ArrayList<>());
                    typeConvertersInstances.addAll(item.getValue());
                }
            }
            typeConverters = CONVERTER_BY_SOURCE_TYPE.putIfAbsent(sourceType, Collections.emptyList());
        }
        if (CollectionUtil.isEmpty(typeConverters)) {
            return Optional.empty();
        }
        for (OrderBo<TypeConverter> typeConverterOrder : typeConverters) {
            final TypeConverter typeConverter = typeConverterOrder.getData();
            if (typeConverter.support(targetType)) {
                return Optional.of(typeConverter);
            }
        }

        return Optional.empty();
    }
}
