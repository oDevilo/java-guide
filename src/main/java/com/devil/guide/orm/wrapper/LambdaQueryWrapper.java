package com.devil.guide.orm.wrapper;

import com.devil.guide.orm.builder.SelectSqlBuilder;
import com.devil.guide.orm.expression.Select;
import com.devil.guide.orm.lambda.SerializableColumnFunction;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Devil
 * @date 2022/4/24
 */
public class LambdaQueryWrapper<T> extends AbstractLambdaWrapper<T> implements Select<LambdaQueryWrapper<T>, SerializableColumnFunction<T, ?>> {
    private static final long serialVersionUID = 5004819916931978465L;

    private final SelectSqlBuilder selectSqlBuilder = new SelectSqlBuilder();

    @SafeVarargs
    @Override
    public final LambdaQueryWrapper<T> select(SerializableColumnFunction<T, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SerializableColumnFunction<T, ?> column : columns) {
                selectSqlBuilder.select(column.columnToString());
            }
        }
        return this;
    }

    @Override
    public String selectSql() {
        return selectSqlBuilder.getSql();
    }

}
