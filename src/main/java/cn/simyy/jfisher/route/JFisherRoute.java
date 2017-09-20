package cn.simyy.jfisher.route;

import java.lang.reflect.Method;

public class JFisherRoute {
    /**
     * path
     */
    private String path;

    /**
     * method
     */
    private Method action;

    /**
     * controller
     */
    private Object controller;

    public JFisherRoute() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Method getAction() {
        return action;
    }

    public void setAction(Method action) {
        this.action = action;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }
}
