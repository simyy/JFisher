package cn.simyy.jfisher.ioc;

import cn.simyy.jfisher.Constants;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BeanFactory {

    private static final Logger log = Logger.getLogger(Constants.LOGGER);

    private  Map<String, Object> beans = new HashMap<>();

    public void addBean(String beanName, Object bean) {
        beans.put(beanName, bean);
    }

    public Object getBean(String beanName) {
        Object o = beans.get(beanName);
        if (o == null) {
            log.warn(String.format("invalid bean: %s", beanName));
        }
        return o;
    }

    public Boolean contain(String beanName){
        return beans.containsKey(beanName);
    }
}
