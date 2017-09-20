package cn.simyy.jfisher.route;

import cn.simyy.jfisher.util.PathUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RouteMatcher {

    private List<JFisherRoute> routes;

    private static class Holder {
        private static RouteMatcher ME = new RouteMatcher();
    }

    public static RouteMatcher me(){
        return Holder.ME;
    }

    private RouteMatcher() {
        this.routes = new ArrayList<>();
    }

    public void setRoutes(List<JFisherRoute> JFisherRoutes) {
        this.routes = JFisherRoutes;
    }

    public JFisherRoute findRoute(String path) {
        String cleanPath = parsePath(path);
        List<JFisherRoute> matchJFisherRoutes = new ArrayList<>();
        for (JFisherRoute JFisherRoute : this.routes) {
            if (matchesPath(JFisherRoute.getPath(), cleanPath)) {
                matchJFisherRoutes.add(JFisherRoute);
            }
        }

        giveMatch(path, matchJFisherRoutes);

        return matchJFisherRoutes.size() > 0 ? matchJFisherRoutes.get(0) : null;
    }

    // match url by priority
    private void giveMatch(final String uri, List<JFisherRoute> JFisherRoutes) {
        Collections.sort(JFisherRoutes, new Comparator<JFisherRoute>() {
            @Override
            public int compare(JFisherRoute o1, JFisherRoute o2) {
                if (o2.getPath().equals(uri)) {
                    return o2.getPath().indexOf(uri);
                }
                return -1;
            }
        });
    }

    private boolean matchesPath(String routePath, String pathToMatch) {
        routePath = routePath.replaceAll(PathUtil.VAR_REGEXP, PathUtil.VAR_REPLACE);
        return pathToMatch.matches("(?i)" + routePath);
    }

    private String parsePath(String path) {
        path = PathUtil.fixPath(path);
        try {
            URI uri = new URI(path);
            return uri.getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
