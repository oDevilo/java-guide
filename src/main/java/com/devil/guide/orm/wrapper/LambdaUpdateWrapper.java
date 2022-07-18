package com.devil.guide.orm.wrapper;

import com.devil.guide.orm.builder.SetSqlBuilder;
import com.devil.guide.orm.expression.Update;
import com.devil.guide.orm.lambda.SerializableColumnFunction;

import java.util.List;

/**
 * @author Devil
 * @date 2022/4/24
 */
public class LambdaUpdateWrapper<T> extends AbstractLambdaWrapper<T> implements Update<LambdaUpdateWrapper<T>, SerializableColumnFunction<T, ?>> {
    private static final long serialVersionUID = 8638324130666742347L;

    private final SetSqlBuilder setSqlBuilder = new SetSqlBuilder();

    @Override
    public LambdaUpdateWrapper<T> set(boolean condition, SerializableColumnFunction<T, ?> column, Object val) {
        if (condition) {
            setSqlBuilder.set(column.columnToString());
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
