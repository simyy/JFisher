package cn.simyy.jfisher;

import cn.simyy.jfisher.ioc.AnnotationDriven;
import cn.simyy.jfisher.servlet.Response;
import cn.simyy.jfisher.core.ConfigLoader;
import cn.simyy.jfisher.ioc.BeanFactory;
import cn.simyy.jfisher.route.RouteManager;
import cn.simyy.jfisher.servlet.Request;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Method;
import java.util.Map;

public final class JFisher {

    private boolean init = false;

    private RouteManager routeManager;

    private ConfigLoader configLoader;

    private BeanFactory beanFactory;

    private JFisher() {
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
        private static JFisher ME = new JFisher();
    }

    public static JFisher me(){
        return FisherHolder.ME;
    }

    public JFisher loadConf(String conf){
        configLoader.load(conf);
        return this;
    }

    public JFisher setConf(String name, String value){
        configLoader.setConf(name, value);
        return this;
    }

    public String getConf(String name){
        return configLoader.getConf(name);
    }

    public RouteManager getRouteManager() {
        return routeManager;
    }

    public JFisher addRoute(String path, String methodName, Object view){
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

    public JFisher addRoutes(RouteManager routeManager){
        this.routeManager.addRoutes(routeManager.getJFisherRoutes());
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
