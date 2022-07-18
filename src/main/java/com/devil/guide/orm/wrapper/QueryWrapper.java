package com.devil.guide.orm.wrapper;

import com.devil.guide.orm.builder.SelectSqlBuilder;
import com.devil.guide.orm.expression.Select;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Devil
 * @date 2022/4/24
 */
public class QueryWrapper extends AbstractWrapper<QueryWrapper, String> implements Select<QueryWrapper, String> {
    private static final long serialVersionUID = -9011974706645431694L;

    private final SelectSqlBuilder selectSqlBuilder = new SelectSqlBuilder();

    @Override
    public QueryWrapper select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            selectSqlBuilder.select(columns);
        }
        return this;
    }

    @Override
    public String selectSql() {
        return selectSqlBuilder.getSql();
    }

}
