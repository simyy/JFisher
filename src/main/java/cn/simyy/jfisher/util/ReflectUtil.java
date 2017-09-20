package cn.simyy.jfisher.util;

import java.lang.reflect.Method;


public class ReflectUtil {

    public static Object invokeMehod(Object bean, Method method, Object... args) {
        try {
            Class<?>[] types = method.getParameterTypes();

            int argCount = args == null ? 0 : args.length;

            if (argCount != 0) {
                for (int i = 0; i < argCount; i++) {
                    args[i] = cast(args[i], types[i]);
                }
            }

            return method.invoke(bean, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T cast(Object value, Class<T> type) {
        if (value != null && !type.isAssignableFrom(value.getClass())) {
            if (is(type, int.class, Integer.class)) {
                value = Integer.parseInt(String.valueOf(value));
            } else if (is(type, long.class, Long.class)) {
                value = Long.parseLong(String.valueOf(value));
            } else if (is(type, float.class, Float.class)) {
                value = Float.parseFloat(String.valueOf(value));
            } else if (is(type, double.class, Double.class)) {
                value = Double.parseDouble(String.valueOf(value));
            } else if (is(type, boolean.class, Boolean.class)) {
                value = Boolean.parseBoolean(String.valueOf(value));
            } else if (is(type, String.class)) {
                value = String.valueOf(value);
            }
        }
        return (T) value;
    }

    public static boolean is(Object obj, Object... mybe) {
        if (obj != null && mybe != null) {
            for (Object mb : mybe)
                if (obj.equals(mb))
                    return true;
        }
        return false;
    }
}
