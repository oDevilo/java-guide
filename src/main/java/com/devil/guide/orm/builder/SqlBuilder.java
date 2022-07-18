package com.devil.guide.orm.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Devil
 * @date 2022/4/17
 */
public abstract class SqlBuilder {

    protected List<String> segments;

    protected List<Object> params = new ArrayList<>();

    protected void addSql(String... sql) {
        if (sql == null || sql.length <= 0) {
            return;
        }
        int i = 0;
        if (segments == null) {
            segments = new ArrayList<>();
        }
        for (; i < sql.length; i++) {
            segments.add(sql[i]);
        }
    }

    public void addParam(Object... objects) {
        if (objects == null || objects.length <= 0) {
            return;
        }
        params.addAll(Arrays.asList(objects));
    }

    public List<Object> getParams() {
        return params;
    }

    public abstract String getSql();
}
