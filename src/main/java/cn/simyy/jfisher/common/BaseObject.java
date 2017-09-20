package cn.simyy.jfisher.common;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by simyy on 2017/9/19.
 */
public class BaseObject implements Serializable {

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}