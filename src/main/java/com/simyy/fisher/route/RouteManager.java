package com.simyy.fisher.route;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RouteManager {
    private static final Logger log = Logger.getLogger(RouteManager.class);

    private List<FisherRoute> fisherRoutes;

    private static class Holder {
        private static RouteManager ME = new RouteManager();
    }

    public static RouteManager me(){
        return Holder.ME;
    }

    private RouteManager() {
        this.fisherRoutes = new ArrayList<>();
    }

    public void addRoute(FisherRoute fisherRoute){
        fisherRoutes.add(fisherRoute);
    }

    public void addRoutes(List<FisherRoute> fisherRoutes){
        fisherRoutes.addAll(fisherRoutes);
    }

    public void removeRoute(FisherRoute fisherRoute){
        fisherRoutes.remove(fisherRoute);
    }

    public void addRoute(String path, Method action, Object controller){
        FisherRoute fisherRoute = new FisherRoute();
        fisherRoute.setPath(path);
        fisherRoute.setAction(action);
        fisherRoute.setController(controller);

        fisherRoutes.add(fisherRoute);
        log.info("Add Route：[" + path + "]");
    }

    public List<FisherRoute> getFisherRoutes() {
        return fisherRoutes;
    }

    public void setFisherRoutes(List<FisherRoute> fisherRoutes) {
        this.fisherRoutes = fisherRoutes;
    }

}
