package com.iwuyc.tools.beans.converter;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;


@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
public class ConverterUtils {

    public static <S, T> T convert(S source, Class<T> targetClass) {
        Class<?> innerTargetClass = primitiveTypeTranslator(targetClass);
        final Class<S> sourceClass = (Class<S>) source.getClass();
        if (innerTargetClass.isAssignableFrom(sourceClass)) {
            return (T) source;
        }
        Optional<TypeConverter> supportConverterOpt = ConverterContainer.find(sourceClass, targetClass);
        if (!supportConverterOpt.isPresent()) {
            throw new IllegalArgumentException("不支持将[" + sourceClass + "]转换成[" + targetClass + "]");
        }
        final TypeConverter<S, T> supportConverter = supportConverterOpt.get();
        return supportConverter.convert(source, targetClass);
    }


    public static Class<?> primitiveTypeTranslator(Class<?> targetClass) {
        final Class<?> innerTargetClass;
        if (targetClass.isPrimitive()) {
            innerTargetClass = PrimitiveTypeConstants.PRIMITIVE_TYPES_MAPPING_WRAPPED_TYPES.get(targetClass);
        } else {
            innerTargetClass = targetClass;
        }
        return innerTargetClass;
    }


}
