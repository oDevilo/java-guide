package com.devil.guide.orm.expression;

/**
 * @author Devil
 * @date 2022/4/29
 */
public interface Pack<R, Col> {

    /**
     * 分组：group by
     * @param condition 执行条件
     * @param columns   字段数组
     */
    R groupBy(boolean condition, Col... columns);

    R groupBy(Col... columns);

    /**
     * 排序：ORDER BY
     *
     * @param condition 执行条件
     * @param isAsc     是否是 ASC 排序
     * @param columns   字段数组
     */
    R orderBy(boolean condition, boolean isAsc, Col... columns);

    default R orderByAsc(Col column) {
        return orderByAsc(true, column);
    }

    R orderByAsc(Col... columns);

    R orderByAsc(boolean condition, Col... columns);

    default R orderByDesc(Col column) {
        return orderByDesc(true, column);
    }

    R orderByDesc(Col... columns);

    R orderByDesc(boolean condition, Col... columns);

    R orderBy(boolean condition, SortField... sortFields);

    default R orderBy(SortField... sortFields) {
        return orderBy(true, sortFields);
    }

    default R orderBy(SortField sortField) {
        return orderBy(true, sortField);
    }

    /**
     * 分页查询
     * @param offset 偏移量
     * @param limit 查询条数
     */
    R limit(long offset, long limit);

    default R limit(long limit) {
        return limit(0, limit);
    }
}
