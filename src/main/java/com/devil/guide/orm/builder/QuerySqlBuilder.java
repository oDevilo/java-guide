package com.devil.guide.orm.builder;

import com.devil.guide.orm.constant.SqlConstants;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author Devil
 * @date 2022/4/17
 */
public class QuerySqlBuilder extends SqlBuilder {

    public void query(String... sql) {
        addSql(sql);
    }

    @Override
    public String getSql() {
        StringBuilder sqlBuilder = new StringBuilder(SqlConstants.BLANK);
        if (CollectionUtils.isNotEmpty(segments)) {
            // 判断是否是 and or 开头的 跳过
            int i = 0;
            if (SqlConstants.AND.equalsIgnoreCase(segments.get(0)) || SqlConstants.OR.equalsIgnoreCase(segments.get(0))) {
                i++;
            }
            for (; i < segments.size(); i++) {
                sqlBuilder.append(segments.get(i));
                if (i != segments.size() - 1) {
                    sqlBuilder.append(SqlConstants.SPACE);
                }
            }
        }
        return sqlBuilder.toString();
    }
}
