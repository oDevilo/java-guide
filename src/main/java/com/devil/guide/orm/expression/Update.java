package com.devil.guide.orm.expression;

import java.io.Serializable;

/**
 * @author Devil
 * @date 2022/4/24
 */
public interface Update<R, Col> extends Serializable {

    /**
     * 设置查询字段
     *
     * @param condition 是否满足set条件
     * @param column    字段
     * @param val       值
     */
    R set(boolean condition, Col column, Object val);

    default R set(Col column, Object val) {
        return set(true, column, val);
    }

    default R setIgnoreNull(Col column, Object val) {
        return set(val != null, column, val);
    }

}
