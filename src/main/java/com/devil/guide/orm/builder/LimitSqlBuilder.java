package com.devil.guide.orm.builder;

import com.devil.guide.orm.constant.SqlConstants;
import org.apache.commons.collections.CollectionUtils;

/**
 * @author Devil
 * @date 2022/4/27
 */
public class LimitSqlBuilder extends SqlBuilder {

    public void limit(long offset, long limit) {
        if (offset <= 0) {
            addSql(String.valueOf(limit));
        } else {
            addSql(offset + SqlConstants.COMMA + limit);
        }
    }

    @Override
    public String getSql() {
        if (CollectionUtils.isEmpty(segments)) {
            return SqlConstants.BLANK;
        }
        return "LIMIT " + segments.get(0);
    }

}
