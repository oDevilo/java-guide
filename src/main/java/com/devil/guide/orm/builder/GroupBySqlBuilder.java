package com.devil.guide.orm.builder;

import com.devil.guide.orm.constant.SqlConstants;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author Devil
 * @date 2022/4/24
 */
public class GroupBySqlBuilder extends SqlBuilder {

    public void groupBy(String... columns) {
        addSql(columns);
    }

    @Override
    public String getSql() {
        if (CollectionUtils.isEmpty(segments)) {
            return SqlConstants.BLANK;
        }
        StringBuilder sqlBuilder = new StringBuilder("GROUP BY ");
        for (int i = 0; i < segments.size(); i++) {
            sqlBuilder.append(segments.get(i));
            if (i != segments.size() - 1) {
                sqlBuilder.append(SqlConstants.COMMA);
            }
        }
        return sqlBuilder.toString();
    }

}
