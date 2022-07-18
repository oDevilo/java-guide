package com.devil.guide.orm.expression;

import java.util.Collection;
import java.util.Objects;

/**
 * where 比较部分
 * <p>
 * and 和 or and优先级较高 会先执行
 * <p>
 * eq 和 orEq 的区别 eq 为 and A = 1  orEq 为 or A = 1
 */
public interface Condition<R, Col> {

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     */
    R eq(boolean condition, Col column, Object val);

    default R eq(Col column, Object val) {
        return eq(true, column, val);
    }

    default R eqIgnoreNull(Col column, Object val) {
        return eq(val != null, column, val);
    }

    R orEq(boolean condition, Col column, Object val);

    default R orEq(Col column, Object val) {
        return orEq(true, column, val);
    }

    default R orEqIgnoreNull(Col column, Object val) {
        return orEq(val != null, column, val);
    }


    /**
     * 不等于 !=
     *
     * @param condition 执行条件
     * @param column    字段timestamp
     * @param val       值
     */
    R ne(boolean condition, Col column, Object val);

    default R ne(Col column, Object val) {
        return ne(true, column, val);
    }

    default R neIgnoreNull(Col column, Object val) {
        return ne(val != null, column, val);
    }

    R orNe(boolean condition, Col column, Object val);

    default R orNe(Col column, Object val) {
        return orNe(true, column, val);
    }

    default R orNeIgnoreNull(Col column, Object val) {
        return orNe(val != null, column, val);
    }

    /**
     * 大于 >
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     */
    R gt(boolean condition, Col column, Object val);

    default R gt(Col column, Object val) {
        return gt(true, column, val);
    }

    default R gtIgnoreNull(Col column, Object val) {
        return gt(val != null, column, val);
    }

    R orGt(boolean condition, Col column, Object val);

    default R orGt(Col column, Object val) {
        return orGt(true, column, val);
    }

    default R orGtIgnoreNull(Col column, Object val) {
        return orGt(val != null, column, val);
    }

    /**
     * 大于等于 >=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     */
    R ge(boolean condition, Col column, Object val);

    default R ge(Col column, Object val) {
        return ge(true, column, val);
    }

    default R geIgnoreNull(Col column, Object val) {
        return ge(val != null, column, val);
    }

    R orGe(boolean condition, Col column, Object val);

    default R orGe(Col column, Object val) {
        return orGe(true, column, val);
    }

    default R orGeIgnoreNull(Col column, Object val) {
        return orGe(val != null, column, val);
    }

    /**
     * 小于 <
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     */
    R lt(boolean condition, Col column, Object val);

    default R lt(Col column, Object val) {
        return lt(true, column, val);
    }

    default R ltIgnoreNull(Col column, Object val) {
        return lt(val != null, column, val);
    }

    R orLt(boolean condition, Col column, Object val);

    default R orLt(Col column, Object val) {
        return orLt(true, column, val);
    }

    default R orLtIgnoreNull(Col column, Object val) {
        return orLt(val != null, column, val);
    }

    /**
     * 小于等于 <=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     */
    R le(boolean condition, Col column, Object val);

    default R le(Col column, Object val) {
        return le(true, column, val);
    }

    default R leIgnoreNull(Col column, Object val) {
        return le(val != null, column, val);
    }

    R orLe(boolean condition, Col column, Object val);

    default R orLe(Col column, Object val) {
        return orLe(true, column, val);
    }

    default R orLeIgnoreNull(Col column, Object val) {
        return orLe(val != null, column, val);
    }

    /**
     * BETWEEN v1 AND v2
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val1      值1
     * @param val2      值2
     */
    R between(boolean condition, Col column, Object val1, Object val2);

    default R between(Col column, Object val1, Object val2) {
        return between(true, column, val1, val2);
    }

    default R betweenIgnoreNull(Col column, Object val1, Object val2) {
        return between(val1 != null && val2 != null, column, val1, val2);
    }

    R orBetween(boolean condition, Col column, Object val1, Object val2);

    default R orBetween(Col column, Object val1, Object val2) {
        return orBetween(true, column, val1, val2);
    }

    default R orBetweenIgnoreNull(Col column, Object val1, Object val2) {
        return orBetween(val1 != null && val2 != null, column, val1, val2);
    }

    /**
     * NOT BETWEEN v1 AND v2
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val1      值1
     * @param val2      值2
     */
    R notBetween(boolean condition, Col column, Object val1, Object val2);

    default R notBetween(Col column, Object val1, Object val2) {
        return notBetween(true, column, val1, val2);
    }

    default R notBetweenIgnoreNull(Col column, Object val1, Object val2) {
        return notBetween(val1 != null && val2 != null, column, val1, val2);
    }

    R orNotBetween(boolean condition, Col column, Object val1, Object val2);

    default R orNotBetween(Col column, Object val1, Object val2) {
        return orNotBetween(true, column, val1, val2);
    }

    default R orNotBetweenIgnoreNull(Col column, Object val1, Object val2) {
        return orNotBetween(val1 != null && val2 != null, column, val1, val2);
    }

    /**
     * LIKE '%v%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     */
    R like(boolean condition, Col column, Object val);

    default R like(Col column, Object val) {
        return like(true, column, val);
    }

    default R likeIgnoreNull(Col column, Object val) {
        return like(val != null, column, val);
    }

    R orLike(boolean condition, Col column, Object val);

    default R orLike(Col column, Object val) {
        return orLike(true, column, val);
    }

    default R orLikeIgnoreNull(Col column, Object val) {
        return orLike(val != null, column, val);
    }

    /**
     * NOT LIKE '%v%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     */
    R notLike(boolean condition, Col column, Object val);

    default R notLike(Col column, Object val) {
        return notLike(true, column, val);
    }

    default R notLikeIgnoreNull(Col column, Object val) {
        return notLike(val != null, column, val);
    }

    R orNotLike(boolean condition, Col column, Object val);

    default R orNotLike(Col column, Object val) {
        return orNotLike(true, column, val);
    }

    default R orNotLikeIgnoreNull(Col column, Object val) {
        return orNotLike(val != null, column, val);
    }

    /**
     * LIKE 'v%' 左匹配
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     */
    R likeLeft(boolean condition, Col column, Object val);

    default R likeLeft(Col column, Object val) {
        return likeLeft(true, column, val);
    }

    default R likeLeftIgnoreNull(Col column, Object val) {
        return likeLeft(val != null, column, val);
    }

    R orLikeLeft(boolean condition, Col column, Object val);

    default R orLikeLeft(Col column, Object val) {
        return orLikeLeft(true, column, val);
    }

    default R orLikeLeftIgnoreNull(Col column, Object val) {
        return orLikeLeft(val != null, column, val);
    }

    /**
     * LIKE '%v' 右匹配
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     */
    R likeRight(boolean condition, Col column, Object val);

    default R likeRight(Col column, Object val) {
        return likeRight(true, column, val);
    }

    default R likeRightIgnoreNull(Col column, Object val) {
        return likeRight(val != null, column, val);
    }

    R orLikeRight(boolean condition, Col column, Object val);

    default R orLikeRight(Col column, Object val) {
        return orLikeRight(true, column, val);
    }

    default R orLikeRightIgnoreNull(Col column, Object val) {
        return orLikeRight(val != null, column, val);
    }

    /**
     * IS NULL
     *
     * @param condition 执行条件
     * @param column    字段
     */
    R isNull(boolean condition, Col column);

    default R isNull(Col column) {
        return isNull(true, column);
    }

    R orIsNull(boolean condition, Col column);

    default R orIsNull(Col column) {
        return orIsNull(true, column);
    }

    /**
     * IS NOT NULL
     *
     * @param condition 执行条件
     * @param column    字段
     */
    R isNotNull(boolean condition, Col column);

    default R isNotNull(Col column) {
        return isNotNull(true, column);
    }

    R orIsNotNull(boolean condition, Col column);

    default R orIsNotNull(Col column) {
        return orIsNotNull(true, column);
    }

    /**
     * IN (a,b,c...)
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     */
    R in(boolean condition, Col column, Collection<?> coll);

    default R in(Col column, Collection<?> coll) {
        return in(true, column, coll);
    }

    default R inIgnoreNull(Col column, Collection<?> coll) {
        return in(collectionNotNull(coll), column, coll);
    }

    R orIn(boolean condition, Col column, Collection<?> coll);

    default R orIn(Col column, Collection<?> coll) {
        return orIn(true, column, coll);
    }

    default R orInIgnoreNull(Col column, Collection<?> coll) {
        return orIn(collectionNotNull(coll), column, coll);
    }

    /**
     * NOT IN (a,b,c...)
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     */
    R notIn(boolean condition, Col column, Collection<?> coll);

    default R notIn(Col column, Collection<?> coll) {
        return notIn(true, column, coll);
    }

    default R notInIgnoreNull(Col column, Collection<?> coll) {
        return notIn(collectionNotNull(coll), column, coll);
    }

    R orNotIn(boolean condition, Col column, Collection<?> coll);

    default R orNotIn(Col column, Collection<?> coll) {
        return orNotIn(true, column, coll);
    }

    default R orNotInIgnoreNull(Col column, Collection<?> coll) {
        return orNotIn(collectionNotNull(coll), column, coll);
    }

    default boolean collectionNotNull(Collection<?> coll) {
        if (coll == null) {
            return false;
        } else {
            return coll.stream().noneMatch(Objects::isNull);
        }
    }

}
