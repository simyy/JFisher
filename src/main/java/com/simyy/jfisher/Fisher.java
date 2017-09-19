package com.simyy.jfisher;

import com.simyy.jfisher.core.ConfigLoader;
import com.simyy.jfisher.ioc.AnnotationDriven;
import com.simyy.jfisher.ioc.BeanFactory;
import com.simyy.jfisher.route.RouteManager;
import com.simyy.jfisher.servlet.Request;
import com.simyy.jfisher.servlet.Response;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Method;
import java.util.Map;

public final class Fisher {

    private boolean init = false;

    private RouteManager routeManager;

    private ConfigLoader configLoader;

    private BeanFactory beanFactory;

    private Fisher() {
        routeManager = RouteManager.me();
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

    public RouteManager getRouteManager() {
        return routeManager;
    }

    public Fisher addRoute(String path, String methodName, Object view){
        try {
            Method method = view.getClass().getMethod(methodName, Request.class, Response.class);
            this.routeManager.addRoute(path, method, view);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Fisher addRoutes(RouteManager routeManager){
        this.routeManager.addRoutes(routeManager.getFisherRoutes());
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
