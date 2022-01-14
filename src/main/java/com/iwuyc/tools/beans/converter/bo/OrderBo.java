/*
 * 深圳市灵智数科有限公司版权所有.
 */
package com.iwuyc.tools.beans.converter.bo;

import lombok.Data;

/**
 * 功能说明
 *
 * @author Neil
 * @version 1.0.0
 * @date 2022/1/13
 */
@Data
public class OrderBo<T> implements Comparable<OrderBo<T>> {
    private final int order;
    private final T data;

    public OrderBo(int order, T data) {
        this.order = order;
        this.data = data;
    }

    @Override
    public int compareTo(OrderBo<T> o) {
        if (this.order == o.order) {
            return 0;
        }
        return this.order > o.order ? 1 : -1;
    }
}
