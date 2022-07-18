package com.devil.guide.orm.wrapper;

import com.devil.guide.orm.builder.GroupBySqlBuilder;
import com.devil.guide.orm.builder.LimitSqlBuilder;
import com.devil.guide.orm.builder.OrderBySqlBuilder;
import com.devil.guide.orm.builder.QuerySqlBuilder;
import com.devil.guide.orm.constant.SqlConstants;
import com.devil.guide.orm.expression.Condition;
import com.devil.guide.orm.expression.Nested;
import com.devil.guide.orm.expression.Pack;
import com.devil.guide.orm.expression.SortField;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.devil.guide.orm.constant.SqlConstants.*;

/**
 * @author Devil
 * @date 2022/4/10
 */
public abstract class AbstractWrapper<R extends AbstractWrapper<R, Col>, Col> implements Wrapper, Condition<R, Col>, Pack<R, Col>, Nested<R, R> {

    protected R self = (R) this;

    private final QuerySqlBuilder querySqlBuilder = new QuerySqlBuilder();

    private final OrderBySqlBuilder orderBySqlBuilder = new OrderBySqlBuilder();

    private final LimitSqlBuilder limitSqSqlBuilder = new LimitSqlBuilder();

    private final GroupBySqlBuilder groupBySqlBuilder = new GroupBySqlBuilder();

    @Override
    public R eq(boolean condition, Col column, Object val) {
        return addQuery(condition, false, column, val, EQ);
    }

    @Override
    public R orEq(boolean condition, Col column, Object val) {
        return addQuery(condition, true, column, val, EQ);
    }

    @Override
    public R ne(boolean condition, Col column, Object val) {
        return addQuery(condition, false, column, val, NE);
    }

    @Override
    public R orNe(boolean condition, Col column, Object val) {
        return addQuery(condition, true, column, val, NE);
    }

    @Override
    public R gt(boolean condition, Col column, Object val) {
        return addQuery(condition, false, column, val, GT);
    }

    @Override
    public R orGt(boolean condition, Col column, Object val) {
        return addQuery(condition, true, column, val, GT);
    }

    @Override
    public R ge(boolean condition, Col column, Object val) {
        return addQuery(condition, false, column, val, GE);
    }

    @Override
    public R orGe(boolean condition, Col column, Object val) {
        return addQuery(condition, true, column, val, GE);
    }

    @Override
    public R lt(boolean condition, Col column, Object val) {
        return addQuery(condition, false, column, val, LT);
    }

    @Override
    public R orLt(boolean condition, Col column, Object val) {
        return addQuery(condition, true, column, val, LT);
    }

    @Override
    public R le(boolean condition, Col column, Object val) {
        return addQuery(condition, false, column, val, LE);
    }

    @Override
    public R orLe(boolean condition, Col column, Object val) {
        return addQuery(condition, true, column, val, LE);
    }

    @Override
    public R between(boolean condition, Col column, Object val1, Object val2) {
        return addBetweenQuery(condition, false, column, val1, val2, false);
    }

    @Override
    public R orBetween(boolean condition, Col column, Object val1, Object val2) {
        return addBetweenQuery(condition, true, column, val1, val2, false);
    }

    @Override
    public R notBetween(boolean condition, Col column, Object val1, Object val2) {
        return addBetweenQuery(condition, false, column, val1, val2, true);
    }

    @Override
    public R orNotBetween(boolean condition, Col column, Object val1, Object val2) {
        return addBetweenQuery(condition, true, column, val1, val2, true);
    }

    @Override
    public R like(boolean condition, Col column, Object val) {
        return addQuery(condition, false, column, "%" + val + "%", LIKE);
    }

    @Override
    public R orLike(boolean condition, Col column, Object val) {
        return addQuery(condition, true, column, "%" + val + "%", LIKE);
    }

    @Override
    public R notLike(boolean condition, Col column, Object val) {
        return addQuery(condition, false, column, "%" + val + "%", NOT, LIKE);
    }

    @Override
    public R orNotLike(boolean condition, Col column, Object val) {
        return addQuery(condition, true, column, "%" + val + "%", NOT, LIKE);
    }

    @Override
    public R likeLeft(boolean condition, Col column, Object val) {
        return addQuery(condition, false, column, val + "%", LIKE);
    }

    @Override
    public R orLikeLeft(boolean condition, Col column, Object val) {
        return addQuery(condition, true, column, val + "%", LIKE);
    }

    @Override
    public R likeRight(boolean condition, Col column, Object val) {
        return addQuery(condition, false, column, "%" + val, LIKE);
    }

    @Override
    public R orLikeRight(boolean condition, Col column, Object val) {
        return addQuery(condition, true, column, "%" + val, LIKE);
    }

    @Override
    public R isNull(boolean condition, Col column) {
        return addQuery(condition, false, column, null, IS_NULL);
    }

    @Override
    public R orIsNull(boolean condition, Col column) {
        return addQuery(condition, true, column, null, IS_NULL);
    }

    @Override
    public R isNotNull(boolean condition, Col column) {
        return addQuery(condition, false, column, null, IS_NOT_NULL);
    }

    @Override
    public R orIsNotNull(boolean condition, Col column) {
        return addQuery(condition, true, column, null, IS_NOT_NULL);
    }

    @Override
    public R in(boolean condition, Col column, Collection<?> coll) {
        return addNormalInParam(condition, false, column, coll, false);
    }

    @Override
    public R orIn(boolean condition, Col column, Collection<?> coll) {
        return addNormalInParam(condition, true, column, coll, false);
    }

    @Override
    public R notIn(boolean condition, Col column, Collection<?> coll) {
        return addNormalInParam(condition, false, column, coll, true);
    }

    @Override
    public R orNotIn(boolean condition, Col column, Collection<?> coll) {
        return addNormalInParam(condition, true, column, coll, true);
    }

    /**
     * 普通查询条件
     *
     * @param condition   是否执行
     * @param or          and 还是 or 连接
     * @param column      属性
     * @param sqlKeywords SQL 关键词
     * @param param       条件值
     */
    protected R addQuery(boolean condition, boolean or, Col column, Object param, String... sqlKeywords) {
        if (condition) {
            // sql
            addAndOrJoin(or);
            querySqlBuilder.query(getColumnString(column));
            querySqlBuilder.query(sqlKeywords);
            if (param != null) { // param
                querySqlBuilder.query(SqlConstants.PRECOMPILE_MARK);
                querySqlBuilder.addParam(param);
            }
        }
        return self;
    }

    protected R addBetweenQuery(boolean condition, boolean or, Col column, Object val1, Object val2, boolean not) {
        if (condition) {
            // where
            addAndOrJoin(or);
            if (not) {
                querySqlBuilder.query(NOT);
            }
            querySqlBuilder.query(getColumnString(column), BETWEEN, PRECOMPILE_MARK, AND, PRECOMPILE_MARK);
            // param
            querySqlBuilder.addParam(val1, val2);
        }
        return self;
    }

    protected R addNormalInParam(boolean condition, boolean or, Col column, Collection<?> coll, boolean not) {
        if (condition) {
            addAndOrJoin(or);
            if (not) {
                querySqlBuilder.query(NOT);
            }
            querySqlBuilder.query(getColumnString(column), IN, LEFT_BRACKET);
            if (CollectionUtils.isNotEmpty(coll)) {
                // param
                querySqlBuilder.addParam(coll.toArray());
                // in
                querySqlBuilder.query(StringUtils.join(coll.stream().map(v -> PRECOMPILE_MARK).collect(Collectors.toList()), COMMA));
            }
            querySqlBuilder.query(RIGHT_BRACKET);
        }
        return self;
    }

    @Override
    public R and(boolean condition, R wrapper) {
        if (condition) {
            addAndOrJoin(false);
            nested(wrapper);
        }
        return self;
    }

    @Override
    public R or(boolean condition, R wrapper) {
        if (condition) {
            addAndOrJoin(true);
            nested(wrapper);
        }
        return self;
    }

    protected void nested(R wrapper) {
        querySqlBuilder.query(LEFT_BRACKET + wrapper.querySql(false) + RIGHT_BRACKET);
        querySqlBuilder.addParam(wrapper.queryParams().toArray());
    }

//    @Override
//    public R nested(boolean condition, R wrapper) {
//        if (condition) {
//            querySqlBuilder.addSql(LEFT_BRACKET);
//            querySqlBuilder.addSql(wrapper.querySql());
//            querySqlBuilder.addSql(RIGHT_BRACKET);
//
//            querySqlBuilder.addParam(wrapper.queryParams().toArray());
//        }
//        return self;
//    }

    private void addAndOrJoin(boolean or) {
        if (or) {
            querySqlBuilder.query(OR);
        } else {
            querySqlBuilder.query(AND);
        }
    }

    @Override
    public R orderBy(boolean condition, SortField... sortFields) {
        if (condition && ArrayUtils.isNotEmpty(sortFields)) {
            orderBySqlBuilder.orderBy(sortFields);
        }
        return self;
    }

    @SafeVarargs
    @Override
    public final R orderBy(boolean condition, boolean isAsc, Col... columns) {
        if (condition && ArrayUtils.isNotEmpty(columns)) {
            SortField[] sortFields = new SortField[columns.length];
            for (int i = 0; i < columns.length; i++) {
                sortFields[i] = new SortField(getColumnString(columns[i]), isAsc);
            }
            orderBySqlBuilder.orderBy(sortFields);
        }
        return self;
    }

    @SafeVarargs
    @Override
    public final R orderByAsc(Col... columns) {
        return orderByAsc(true, columns);
    }

    @SafeVarargs
    @Override
    public final R orderByAsc(boolean condition, Col... columns) {
        return orderBy(condition, true, columns);
    }

    @SafeVarargs
    @Override
    public final R orderByDesc(Col... columns) {
        return orderByDesc(true, columns);
    }

    @SafeVarargs
    @Override
    public final R orderByDesc(boolean condition, Col... columns) {
        return orderBy(condition, false, columns);
    }

    @SafeVarargs
    @Override
    public final R groupBy(Col... columns) {
        return groupBy(true, columns);
    }

    @SafeVarargs
    @Override
    public final R groupBy(boolean condition, Col... columns) {
        if (condition && ArrayUtils.isNotEmpty(columns)) {
            for (Col column : columns) {
                groupBySqlBuilder.groupBy(getColumnString(column));
            }
        }
        return self;
    }

    @Override
    public R limit(long offset, long limit) {
        limitSqSqlBuilder.limit(offset, limit);
        return self;
    }

    protected String getColumnString(Col column) {
        return (String) column;
    }

    @Override
    public String querySql(boolean where) {
        StringBuilder sql = new StringBuilder();
        String query = querySqlBuilder.getSql();
        String groupBy = groupBySqlBuilder.getSql();
        String orderBy = orderBySqlBuilder.getSql();
        String limit = limitSqSqlBuilder.getSql();
        if (StringUtils.isNotBlank(query)) {
            if (where) {
                sql.append("WHERE ").append(query).append(SqlConstants.SPACE);
            } else {
                sql.append(query);
            }
        }
        if (StringUtils.isNotBlank(groupBy)) {
            sql.append(groupBy).append(SqlConstants.SPACE);
        }
        if (StringUtils.isNotBlank(orderBy)) {
            sql.append(orderBy).append(SqlConstants.SPACE);
        }
        if (StringUtils.isNotBlank(limit)) {
            sql.append(limit).append(SqlConstants.SPACE);
        }
        return sql.toString();
    }

    @Override
    public List<Object> queryParams() {
        return querySqlBuilder.getParams();
    }

    @Override
    public String selectSql() {
        return ASTERISK;
    }

    @Override
    public String setSql() {
        return SqlConstants.BLANK;
    }

    @Override
    public List<Object> setParams() {
        return Collections.emptyList();
    }

}
