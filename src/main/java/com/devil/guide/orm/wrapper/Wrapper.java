package com.devil.guide.orm.wrapper;

import java.util.List;

/**
 * @author Devil
 * @date 2022/4/25
 */
public interface Wrapper {

    /**
     * 获取 where group by order by limit查询条件
     */
    String querySql(boolean where);

    /**
     * 获取查询参数
     */
    List<Object> queryParams();

    /**
     * 获取查询返回字段
     */
    String selectSql();

    /**
     * 获取set SQL
     */
    String setSql();

    /**
     * 获取set 参数
     */
    List<Object> setParams();

}
