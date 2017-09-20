package cn.simyy.jfisher.core;

import cn.simyy.jfisher.Constants;
import cn.simyy.jfisher.JFisher;
import cn.simyy.jfisher.enums.ErrorEnum;
import cn.simyy.jfisher.route.JFisherRoute;
import cn.simyy.jfisher.route.RouteManager;
import cn.simyy.jfisher.route.RouteMatcher;
import cn.simyy.jfisher.servlet.Request;
import cn.simyy.jfisher.servlet.Response;
import cn.simyy.jfisher.util.PathUtil;
import cn.simyy.jfisher.util.ReflectUtil;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class FisherFilter implements Filter {

    private static final Logger log = Logger.getLogger(Constants.LOGGER);

    private RouteMatcher routeMatcher = RouteMatcher.me();

    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        BasicConfigurator.configure();
        JFisher jFisher = JFisher.me();

        if(!jFisher.isInit()){
            String className = filterConfig.getInitParameter("bootstrap");
            Bootstrap bootstrap = this.getBootstrap(className);
            bootstrap.init(jFisher);

            RouteManager routeManager = jFisher.getRouteManager();
            if(null != routeManager){
                routeMatcher.setRoutes(routeManager.getJFisherRoutes());
            }
            servletContext = filterConfig.getServletContext();

            jFisher.setInit(true);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = PathUtil.getRelativePath(request);

        log.info("Request URI：" + uri);

        JFisherRoute JFisherRoute = routeMatcher.findRoute(uri);

        if (JFisherRoute != null) {
            handle(request, response, JFisherRoute);
        } else {
            //filterChain.doFilter(request, response);
            Response.setDefault(response);

        }
    }

    private void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, JFisherRoute JFisherRoute){

        Request request = new Request(httpServletRequest);
        Response response = new Response(httpServletResponse);
        JFisherContext.initContext(servletContext, request, response);

        Object controller = JFisherRoute.getController();

        Method actionMethod = JFisherRoute.getAction();

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
