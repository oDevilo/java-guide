package com.devil.guide.orm.wrapper;

import com.devil.guide.orm.lambda.SerializableColumnFunction;

/**
 * @author Devil
 * @date 2022/4/24
 */
public abstract class AbstractLambdaWrapper<T> extends AbstractWrapper<AbstractLambdaWrapper<T>, SerializableColumnFunction<T, ?>> {

    @Override
    protected String getColumnString(SerializableColumnFunction<T, ?> column) {
        return column.columnToString();
    }

}
