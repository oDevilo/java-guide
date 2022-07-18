package com.devil.guide.orm.builder;

import com.devil.guide.orm.constant.SqlConstants;
import com.devil.guide.orm.expression.SortField;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author Devil
 * @date 2022/4/27
 */
public class OrderBySqlBuilder extends SqlBuilder {

    public void orderBy(SortField... sortFields) {
        if (sortFields == null || sortFields.length <= 0) {
            return;
        }
        for (SortField sortField : sortFields) {
            addSql(sortField.getColumn() + " " + sortField.getSort());
        }
    }

    @Override
    public String getSql() {
        if (CollectionUtils.isEmpty(segments)) {
            return SqlConstants.BLANK;
        }
        StringBuilder sqlBuilder = new StringBuilder("ORDER BY ");
        for (int i = 0; i < segments.size(); i++) {
            sqlBuilder.append(segments.get(i));
            if (i != segments.size() - 1) {
                sqlBuilder.append(SqlConstants.COMMA);
            }
        }
        return sqlBuilder.toString();
    }

}
