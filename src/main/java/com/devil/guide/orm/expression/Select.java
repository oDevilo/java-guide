package com.devil.guide.orm.expression;

import java.io.Serializable;

/**
 * @author Devil
 * @date 2022/4/24
 */
public interface Select<R, Col> extends Serializable {

    /**
     * 设置查询字段
     *
     * @param columns 字段数组
     */
    R select(Col... columns);

}
