package com.simyy.fisher.core;

import com.simyy.fisher.Fisher;
import com.simyy.fisher.enums.ErrorEnum;
import com.simyy.fisher.route.FisherRoute;
import com.simyy.fisher.route.RouteMatcher;
import com.simyy.fisher.route.RouteManager;
import com.simyy.fisher.servlet.Request;
import com.simyy.fisher.servlet.Response;
import com.simyy.fisher.util.PathUtil;
import com.simyy.fisher.util.ReflectUtil;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class FisherFilter implements Filter {
    private static final Logger log = Logger.getLogger(FisherFilter.class);

    private RouteMatcher routeMatcher = RouteMatcher.me();

    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        BasicConfigurator.configure();
        Fisher fisher = Fisher.me();

        if(!fisher.isInit()){
            String className = filterConfig.getInitParameter("bootstrap");
            Bootstrap bootstrap = this.getBootstrap(className);
            bootstrap.init(fisher);

            RouteManager routeManager = fisher.getRouteManager();
            if(null != routeManager){
                routeMatcher.setRoutes(routeManager.getFisherRoutes());
            }
            servletContext = filterConfig.getServletContext();

            fisher.setInit(true);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = PathUtil.getRelativePath(request);

        log.info("Request URI：" + uri);

        FisherRoute fisherRoute = routeMatcher.findRoute(uri);

        if (fisherRoute != null) {
            handle(request, response, fisherRoute);
        } else {
            //filterChain.doFilter(request, response);
            Response.setDefault(response);

        }
    }

    private void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FisherRoute fisherRoute){

        Request request = new Request(httpServletRequest);
        Response response = new Response(httpServletResponse);
        FisherContext.initContext(servletContext, request, response);

        Object controller = fisherRoute.getController();

        Method actionMethod = fisherRoute.getAction();

        executeMethod(controller, actionMethod, request, response);
    }

    private Object executeMethod(Object object, Method method, Request request, Response response) {
        // ignore private
        method.setAccessible(true);

        Object[] args = null;
        if(method.getParameterTypes().length > 0){
            args = getArgs(request, response, method.getParameterTypes());
        }

        return ReflectUtil.invokeMehod(object, method, args);
    }

    private Object[] getArgs(Request request, Response response, Class<?>[] params){

        int len = params.length;
        Object[] args = new Object[len];

        for(int i = 0; i < len; i++){
            Class<?> paramTypeClazz = params[i];
            if(paramTypeClazz.getName().equals(Request.class.getName())){
                args[i] = request;
            }
            if(paramTypeClazz.getName().equals(Response.class.getName())){
                args[i] = response;
            }
        }

        return args;
    }

    private Bootstrap getBootstrap(String className) {
        if(className != null){
            try {
                Class<?> clazz = Class.forName(className);
                return (Bootstrap) clazz.newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        throw new FisherException(ErrorEnum.INNER, String.format("init bootstrap class error class:%s", className));
    }

    @Override
    public void destroy() {

    }
}
