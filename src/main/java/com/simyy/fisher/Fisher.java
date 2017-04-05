package com.simyy.fisher;

import com.simyy.fisher.route.Routers;
import com.simyy.fisher.servlet.Request;
import com.simyy.fisher.servlet.Response;

import java.lang.reflect.Method;

public final class Fisher {
    /**
     * 存放所有路由
     */
    private Routers routers;

    /**
     * 配置加载器
     */
    private ConfigLoader configLoader;

    /**
     * 框架是否已经初始化
     */
    private boolean init = false;


    private Fisher() {
        routers = new Routers();
        configLoader = new ConfigLoader();
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
}
