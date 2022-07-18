package com.devil.guide.orm.expression;

/**
 * 嵌套查询
 */
public interface Nested<E, R> {

    /**
     * AND 嵌套 and
     * ( a = b and c = d )
     *
     * @param condition 执行条件
     * @param exec 执行器
     */
    R and(boolean condition, E exec);

    default R and(E exec) {
        return and(true, exec);
    }

    /**
     * OR 嵌套 or
     * ( a = b and c = d )
     *
     * @param condition 执行条件
     * @param exec 执行器
     */
    R or(boolean condition, E exec);

    default R or(E exec) {
        return or(true, exec);
    }

    // 这个好像不太用的到
//    /**
//     * 正常嵌套 不带 AND 或者 OR
//     * ( a = b and c = d )
//     *
//     * @param condition 执行条件
//     * @param exec 执行器
//     */
//    R nested(boolean condition, E exec);
//
//    default R nested(E exec) {
//        return nested(true, exec);
//    }
}
