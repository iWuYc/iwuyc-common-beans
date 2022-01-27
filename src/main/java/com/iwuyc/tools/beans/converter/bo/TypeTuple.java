/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.iwuyc.tools.beans.converter.bo;

import lombok.Data;

/**
 * 类型元组
 *
 * @author Neil
 * @version 2021.4
 * @since 2021.4
 */
@SuppressWarnings("rawtypes")
@Data
public class TypeTuple {
    private final Class source;
    private final Class target;
}
