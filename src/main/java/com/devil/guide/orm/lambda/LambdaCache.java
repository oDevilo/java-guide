package com.devil.guide.orm.lambda;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Devil
 * @since 2022/3/7
 */
public class LambdaCache {

    /**
     * SerializedLambda 反序列化缓存
     */
    private static final Map<String, WeakReference<SerializedLambda>> FUNC_CACHE = new ConcurrentHashMap<>();

    public static WeakReference<SerializedLambda> putFunc(String key, WeakReference<SerializedLambda> value) {
        return FUNC_CACHE.put(key, value);
    }

    public static WeakReference<SerializedLambda> getFunc(String key) {
        return FUNC_CACHE.get(key);
    }
}
