package com.simyy.jfisher.core;

import com.simyy.jfisher.servlet.Request;
import com.simyy.jfisher.servlet.Response;

import javax.servlet.ServletContext;

public class FisherContext {
    private static final ThreadLocal<FisherContext> CONTEXT = new ThreadLocal<FisherContext>();

    private ServletContext context;
    private Request request;
    private Response response;

    private FisherContext() {
    }

    public static FisherContext me(){
        return CONTEXT.get();
    }

    public static void initContext(ServletContext context, Request request, Response response) {
        FisherContext fisherContext = new FisherContext();
        fisherContext.context = context;
        fisherContext.request = request;
        fisherContext.response = response;
        CONTEXT.set(fisherContext);
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
