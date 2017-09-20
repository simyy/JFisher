package cn.simyy.jfisher.route;

import cn.simyy.jfisher.Constants;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RouteManager {

    private static final Logger log = Logger.getLogger(Constants.LOGGER);

    private List<JFisherRoute> JFisherRoutes;

    private static class Holder {
        private static RouteManager ME = new RouteManager();
    }

    public static RouteManager me(){
        return Holder.ME;
    }

    private RouteManager() {
        this.JFisherRoutes = new ArrayList<>();
    }

    public void addRoute(JFisherRoute JFisherRoute){
        JFisherRoutes.add(JFisherRoute);
    }

    public void addRoutes(List<JFisherRoute> JFisherRoutes){
        JFisherRoutes.addAll(JFisherRoutes);
    }

    public void removeRoute(JFisherRoute JFisherRoute){
        JFisherRoutes.remove(JFisherRoute);
    }

    public void addRoute(String path, Method action, Object controller){
        JFisherRoute JFisherRoute = new JFisherRoute();
        JFisherRoute.setPath(path);
        JFisherRoute.setAction(action);
        JFisherRoute.setController(controller);

        JFisherRoutes.add(JFisherRoute);
        log.info("Add Route：[" + path + "]");
    }

    public List<JFisherRoute> getJFisherRoutes() {
        return JFisherRoutes;
    }

    public void setJFisherRoutes(List<JFisherRoute> JFisherRoutes) {
        this.JFisherRoutes = JFisherRoutes;
    }

}
