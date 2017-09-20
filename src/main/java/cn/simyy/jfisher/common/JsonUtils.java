package cn.simyy.jfisher.common;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by simyy on 2017/9/19.
 */
public class JsonUtils {


    public static <T> String toJson(T t) {
        return JSON.toJSONString(t);
    }


    public static <T> T parseObject(String str, Class<T> t) {
        return JSON.parseObject(str, t);
    }


    public static <T> String toJson(List<T> ts) {
        return JSON.toJSONString(ts);
    }


    public static <T> List<T> parseArrayObject(String str, Class<T> t) {
        return JSON.parseArray(str, t);
    }


    public static void main(String[] args) {

        class A {

            public int a = 1;

            public int getA() {
                return a;
            }

            public void setA(int a) {
                this.a = a;
            }
        }


        A a = new A();

        assert a.a == JsonUtils.parseObject(JsonUtils.toJson(a), A.class).a;

    }
}
