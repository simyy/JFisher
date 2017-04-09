package com.simyy.fisher;

import com.simyy.fisher.core.ConfigLoader;
import com.simyy.fisher.ioc.AnnotationDriven;
import com.simyy.fisher.ioc.BeanFactory;
import com.simyy.fisher.route.Routers;
import com.simyy.fisher.servlet.Request;
import com.simyy.fisher.servlet.Response;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Method;
import java.util.Map;

public final class Fisher {

    private boolean init = false;

    private Routers routers;

    private ConfigLoader configLoader;

    private BeanFactory beanFactory;

    private Fisher() {
        routers = new Routers();
        configLoader = new ConfigLoader();
        beanFactory = new BeanFactory();
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    private static class FisherHolder {
        private static Fisher ME = new Fisher();
    }

    public static Fisher me(){
        return FisherHolder.ME;
    }

    public Fisher loadConf(String conf){
        configLoader.load(conf);
        return this;
    }

    public Fisher setConf(String name, String value){
        configLoader.setConf(name, value);
        return this;
    }

    public String getConf(String name){
        return configLoader.getConf(name);
    }

    public Fisher addRoutes(Routers routers){
        this.routers.addRoute(routers.getRoutes());
        return this;
    }

    public Routers getRouters() {
        return routers;
    }

    /**
     * 添加路由
     * @param path			映射的PATH
     * @param methodName	方法名称
     * @param controller	控制器对象
     * @return
     */
    public Fisher addRoute(String path, String methodName, Object controller){
        try {
            Method method = controller.getClass().getMethod(methodName, Request.class, Response.class);
            this.routers.addRoute(path, method, controller);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void addBean(String beanName, Object bean) {
        beanFactory.addBean(beanName, bean);
    }

    public void addBeans(Map<String, Object> objectMap) {
        if (MapUtils.isEmpty(objectMap)) {
            return;
        }

        objectMap.forEach((key, value) -> addBean(key, value));
    }

    public Object getBean(String beanName) {
        return beanFactory.getBean(beanName);
    }

    public Boolean contain(String beanName){
        return beanFactory.contain(beanName);
    }

    public void initDriven(String packName) {
        AnnotationDriven.init(this, packName);
    }
}
