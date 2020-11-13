package com.iwuyc.tools.beans;

import com.iwuyc.tools.beans.converter.ConverterUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

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
        final BeanMap beanMap = BeanMap.create(instance);
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
            final Class<?> targetClass = beanMap.getPropertyType(key);
            try {
                final Object convertVal = ConverterUtils.convert(value, targetClass);
                beanMap.put(key, convertVal);
            } catch (Exception e) {
                log.warn("注入[{}]的时候出现异常。信息为：{}", key, e.getMessage());
                otherProperties.put(key, value);
            }
        }
        return otherProperties;
    }
}
