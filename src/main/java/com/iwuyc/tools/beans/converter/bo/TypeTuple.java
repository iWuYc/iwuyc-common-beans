/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.iwuyc.tools.beans.converter.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 功能说明
 *
 * @author 吴宇春
 * @version 1.0.0
 * @date 2022/1/13
 */
@Data
@EqualsAndHashCode
public class TypeTuple {
    private final Class source;
    private final Class target;
}
