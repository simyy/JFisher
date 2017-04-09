package com.simyy.fisher.ioc;

import com.simyy.fisher.core.FisherException;
import com.simyy.fisher.enums.ErrorEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * ioc factory
 */
public class BeanFactory {

    private  Map<String, Object> beans = new HashMap<String, Object>();

    public void addBean(String beanName, Object bean) {
        beans.put(beanName, bean);
    }

    public Object getBean(String beanName) {
        Object o = beans.get(beanName);
        if (o != null) {
            return o;
        } else {
            FisherException.make(ErrorEnum.INVALID, String.format("invalid bean:%s", beanName));
        }

        return null;
    }

    public Boolean contain(String beanName){
        return beans.containsKey(beanName);
    }
}
