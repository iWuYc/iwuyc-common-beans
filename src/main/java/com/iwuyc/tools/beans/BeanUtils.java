package com.iwuyc.tools.beans;

import com.iwuyc.tools.beans.converter.ConverterUtils;
import com.iwuyc.tools.beans.pojo.BeanMap;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * BeanUtils bean类的操作工具类，用于POJO的操作，涉及到部分数据类型转换的问题。
 *
 * @author Neil
 */
@Slf4j
public class BeanUtils {

    /**
     * 将map中的值，按field的名字注入到instance中。
     *
     * @param instance    实例
     * @param fieldAndVal 字段跟值（这个map的键值应该是Map&lt;String,Object&gt;）
     * @param <T>         用于注入前的类型转换。
     * @return 未注入成功的字段跟值，一般是，不存在这个字段，或者，在注入的时候出问题了
     */
    public static <T> Map<String, Object> setter(T instance, Map<String, ?> fieldAndVal) {
        final BeanMap<T> beanMap = BeanMap.create(instance);
        Map<String, Object> otherProperties = new HashMap<>(fieldAndVal.size() / 2);
        for (Map.Entry<String, ?> entryItem : fieldAndVal.entrySet()) {
            final String key = entryItem.getKey();
            final Object value = entryItem.getValue();
            if (!beanMap.containsKey(key)) {
                otherProperties.put(key, value);
                continue;
            }
            if (null == value) {
                beanMap.put(key, null);
                continue;
            }
            final Optional<Class<?>> targetClassOpt = beanMap.getPropertyType(key);
            if (!targetClassOpt.isPresent()) {
                log.info("对应的属性类型未知，不处理。{}", key);
                otherProperties.put(key, value);
                continue;
            }
            try {
                final Object convertVal = ConverterUtils.convert(value, targetClassOpt.get());
                beanMap.put(key, convertVal);
            } catch (Exception e) {
                log.warn("注入[{}]的时候出现异常。信息为：{}", key, e.getMessage());
                otherProperties.put(key, value);
            }
        }
        return otherProperties;
    }
}
