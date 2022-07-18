package com.devil.guide.orm.expression;

import com.devil.guide.orm.constant.SqlConstants;
import lombok.Data;

/**
 * @author Devil
 * @date 2022/4/27
 */
@Data
public class SortField {
    /**
     * 字段
     */
    private String column;

    private String sort = SqlConstants.ASC;

    public SortField(String column) {
        this.column = column;
    }

    public SortField(String column, boolean asc) {
        this.column = column;
        if (!asc) {
            sort = SqlConstants.DESC;
        }
    }
}
