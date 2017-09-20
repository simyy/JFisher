package cn.simyy.jfisher.core;

import cn.simyy.jfisher.servlet.Request;
import cn.simyy.jfisher.servlet.Response;

import javax.servlet.ServletContext;

public class JFisherContext {
    private static final ThreadLocal<JFisherContext> CONTEXT = new ThreadLocal<JFisherContext>();

    private ServletContext context;
    private Request request;
    private Response response;

    private JFisherContext() {
    }

    public static JFisherContext me(){
        return CONTEXT.get();
    }

    public static void initContext(ServletContext context, Request request, Response response) {
        JFisherContext jFisherContext = new JFisherContext();
        jFisherContext.context = context;
        jFisherContext.request = request;
        jFisherContext.response = response;
        CONTEXT.set(jFisherContext);
    }

    public static void remove(){
        CONTEXT.remove();
    }

    public ServletContext getContext() {
        return context;
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
