package com.simyy.fisher.core;

import com.simyy.fisher.Fisher;
import com.simyy.fisher.route.Route;
import com.simyy.fisher.route.RouteMatcher;
import com.simyy.fisher.route.Routers;
import com.simyy.fisher.servlet.Request;
import com.simyy.fisher.servlet.Response;
import com.simyy.fisher.util.PathUtil;
import com.simyy.fisher.util.ReflectUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Logger;

public class FisherFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(FisherFilter.class.getName());

    private RouteMatcher routeMatcher = new RouteMatcher(new ArrayList<Route>());

    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Fisher fisher = Fisher.me();
        if(!fisher.isInit()){

            String className = filterConfig.getInitParameter("bootstrap");
            Bootstrap bootstrap = this.getBootstrap(className);
            bootstrap.init(fisher);

            Routers routers = fisher.getRouters();
            if(null != routers){
                routeMatcher.setRoutes(routers.getRoutes());
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

        LOGGER.info("Request URI：" + uri);

        Route route = routeMatcher.findRoute(uri);

        if (route != null) {
            handle(request, response, route);
        } else{
            filterChain.doFilter(request, response);
        }
    }

    private void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Route route){

        // 初始化上下文
        Request request = new Request(httpServletRequest);
        Response response = new Response(httpServletResponse);
        FisherContext.initContext(servletContext, request, response);

        Object controller = route.getController();
        // 要执行的路由方法
        Method actionMethod = route.getAction();
        // 执行route方法
        executeMethod(controller, actionMethod, request, response);
    }

    private Object executeMethod(Object object, Method method, Request request, Response response){
        int len = method.getParameterTypes().length;
        method.setAccessible(true);
        if(len > 0){
            Object[] args = getArgs(request, response, method.getParameterTypes());
            return ReflectUtil.invokeMehod(object, method, args);
        } else {
            return ReflectUtil.invokeMehod(object, method);
        }
    }

    private Object[] getArgs(Request request, Response response, Class<?>[] params){

        int len = params.length;
        Object[] args = new Object[len];

        for(int i=0; i<len; i++){
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
        if(null != className){
            try {
                Class<?> clazz = Class.forName(className);
                Bootstrap bootstrap = (Bootstrap) clazz.newInstance();
                return bootstrap;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("init bootstrap class error!");
    }

    @Override
    public void destroy() {

    }
}
