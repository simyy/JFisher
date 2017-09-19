package com.simyy.jfisher.route;

import com.simyy.jfisher.util.PathUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RouteMatcher {
    private List<FisherRoute> routes;

    private static class Holder {
        private static RouteMatcher ME = new RouteMatcher();
    }

    public static RouteMatcher me(){
        return Holder.ME;
    }

    private RouteMatcher() {
        this.routes = new ArrayList<>();
    }

    public void setRoutes(List<FisherRoute> fisherRoutes) {
        this.routes = fisherRoutes;
    }

    public FisherRoute findRoute(String path) {
        String cleanPath = parsePath(path);
        List<FisherRoute> matchFisherRoutes = new ArrayList<>();
        for (FisherRoute fisherRoute : this.routes) {
            if (matchesPath(fisherRoute.getPath(), cleanPath)) {
                matchFisherRoutes.add(fisherRoute);
            }
        }

        giveMatch(path, matchFisherRoutes);

        return matchFisherRoutes.size() > 0 ? matchFisherRoutes.get(0) : null;
    }

    // match url by priority
    private void giveMatch(final String uri, List<FisherRoute> fisherRoutes) {
        Collections.sort(fisherRoutes, new Comparator<FisherRoute>() {
            @Override
            public int compare(FisherRoute o1, FisherRoute o2) {
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
