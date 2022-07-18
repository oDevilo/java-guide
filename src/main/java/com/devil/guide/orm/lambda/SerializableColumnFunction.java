package com.devil.guide.orm.lambda;

import org.apache.ibatis.reflection.property.PropertyNamer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.function.Function;

/**
 * 可序列化function
 *
 * @author Devil
 * @since 2022/3/7
 */
@FunctionalInterface
public interface SerializableColumnFunction<T, R> extends Function<T, R>, Serializable {

    /**
     * 返回驼峰式列名
     */
    default String columnToString() {
        SerializableColumnFunction<?, ?> func = this;
        Class<?> clazz = func.getClass();
        String canonicalName = clazz.getCanonicalName();

        SerializedLambda serializedLambda = Optional.ofNullable(LambdaCache.getFunc(canonicalName))
                .map(WeakReference::get)
                .orElseGet(() -> {
                    // 将流转为 SerializedLambda 对象
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                        oos.writeObject(func);
                        oos.flush();
                    } catch (IOException ex) {
                        throw new IllegalArgumentException("Failed to serialize object of type: " + clazz, ex);
                    }

                    try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())) {
                        @Override
                        protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
                            Class<?> clazz;
                            ClassLoader cl = Thread.currentThread().getContextClassLoader();
                            try {
                                clazz = Class.forName(objectStreamClass.getName(), false, cl);
                            } catch (Exception ex) {
                                clazz = super.resolveClass(objectStreamClass);
                            }
                            return clazz == java.lang.invoke.SerializedLambda.class ? SerializedLambda.class : clazz;
                        }
                    }) {
                        SerializedLambda lambda = (SerializedLambda) objIn.readObject();
                        LambdaCache.putFunc(canonicalName, new WeakReference<>(lambda));
                        return lambda;
                    } catch (ClassNotFoundException | IOException e) {
                        throw new IllegalArgumentException("This is impossible to happen", e);
                    }
                });
        return PropertyNamer.methodToProperty(serializedLambda.getImplMethodName());
    }
}
