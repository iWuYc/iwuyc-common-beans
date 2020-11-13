package com.iwuyc.tools.beans.converter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@SuppressWarnings({"rawtypes", "unchecked"})
public class ConverterUtils {
    private static final Map<Class, Collection<TypeConverter>> CLASS_COLLECTION_MAP = new ConcurrentHashMap<>();

    static {
        final ServiceLoader<TypeConverter> typeConverters = ServiceLoader.load(TypeConverter.class);
        for (TypeConverter typeConverter : typeConverters) {
            final Collection<Class> sourceTypes = typeConverter.sourceType();
            for (Class sourceType : sourceTypes) {
                final Collection<TypeConverter> typeConvertersInstances =
                        CLASS_COLLECTION_MAP.computeIfAbsent(sourceType, key -> new ArrayList<>());
                typeConvertersInstances.add(typeConverter);
            }
        }
    }

    public static <S, T> T convert(S source, Class<T> targetClass) {
        if (targetClass.isAssignableFrom(source.getClass())) {
            return (T) source;
        }
        Optional<TypeConverter<S, T>> supportConverterOpt = findSupportConverter(source, targetClass);
        if (!supportConverterOpt.isPresent()) {
            throw new IllegalArgumentException("不支持将[" + source.getClass() + "]转换成[" + targetClass + "]");
        }
        final TypeConverter<S, T> supportConverter = supportConverterOpt.get();
        return supportConverter.convert(source, targetClass);
    }

    private static <S, T> Optional<TypeConverter<S, T>> findSupportConverter(S source, Class<T> targetClass) {
        Optional<TypeConverter<S, T>> result;
        Stack<Class<?>> targetClassStack = new Stack<>();
        targetClassStack.push(source.getClass());
        do {
            Class<?> sourceClass = targetClassStack.pop();

            result = findSupportConverter0(sourceClass, targetClass);
            if (result.isPresent()) {
                return result;
            }
            final Class superclass = sourceClass.getSuperclass();
            if (null != superclass) {
                targetClassStack.push(superclass);
            }
            final Class<?>[] interfaces = sourceClass.getInterfaces();
            for (Class interfaze : interfaces) {
                targetClassStack.push(interfaze);
            }
        } while (targetClassStack.size() > 0);
        return Optional.empty();
    }

    private static <S, T> Optional<TypeConverter<S, T>> findSupportConverter0(Class<?> sourceType,
                                                                              Class<T> targetClass) {
        Collection<TypeConverter> typeConverters = CLASS_COLLECTION_MAP.get(sourceType);
        if (null == typeConverters) {
            for (Map.Entry<Class, Collection<TypeConverter>> item : CLASS_COLLECTION_MAP.entrySet()) {
                final Class registerSrcClass = item.getKey();
                if (registerSrcClass.isAssignableFrom(sourceType)) {
                    final Collection<TypeConverter> typeConvertersInstances =
                            CLASS_COLLECTION_MAP.computeIfAbsent(sourceType, key -> new ArrayList<>());
                    typeConvertersInstances.addAll(item.getValue());
                }
            }
            typeConverters = CLASS_COLLECTION_MAP.putIfAbsent(sourceType, Collections.emptyList());
        } else if (typeConverters.size() == 0) {
            return Optional.empty();
        }
        for (TypeConverter typeConverter : typeConverters) {
            if (typeConverter.support(targetClass)) {
                return Optional.of(typeConverter);
            }
        }

        return Optional.empty();
    }
}
