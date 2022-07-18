package com.devil.guide.orm.wrapper;

import com.devil.guide.orm.builder.SetSqlBuilder;
import com.devil.guide.orm.expression.Update;

import java.util.List;

/**
 * @author Devil
 * @date 2022/4/24
 */
public class UpdateWrapper extends AbstractWrapper<UpdateWrapper, String> implements Update<UpdateWrapper, String> {
    private static final long serialVersionUID = -4099666660198070320L;

    private final SetSqlBuilder setSqlBuilder = new SetSqlBuilder();

    @Override
    public UpdateWrapper set(boolean condition, String column, Object val) {
        if (condition) {
            setSqlBuilder.set(column);
            setSqlBuilder.addParam(val);
        }
        return this;
    }

    @Override
    public String setSql() {
        return setSqlBuilder.getSql();
    }

    @Override
    public List<Object> setParams() {
        return setSqlBuilder.getParams();
    }
}
