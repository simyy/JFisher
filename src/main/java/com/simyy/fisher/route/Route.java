package com.simyy.fisher.route;

import java.lang.reflect.Method;

public class Route {
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

    public Route() {
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
