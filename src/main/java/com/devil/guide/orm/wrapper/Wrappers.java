package com.devil.guide.orm.wrapper;

/**
 * @author Devil
 * @date 2022/4/27
 */
public class Wrappers {

    public static <T> QueryWrapper query() {
        return new QueryWrapper();
    }

    public static <T> LambdaQueryWrapper<T> lambdaQuery() {
        return new LambdaQueryWrapper<>();
    }

    public static <T> UpdateWrapper update() {
        return new UpdateWrapper();
    }

    public static <T> LambdaUpdateWrapper<T> lambdaUpdate() {
        return new LambdaUpdateWrapper<>();
    }

}
