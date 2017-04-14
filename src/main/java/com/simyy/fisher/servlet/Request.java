package com.simyy.fisher.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Request {
    private HttpServletRequest raw;

    public Request(HttpServletRequest httpServletRequest) {
        this.raw = httpServletRequest;
    }

    public HttpServletRequest getRaw() {
        return raw;
    }

    public void attr(String name, Object value){
        raw.setAttribute(name, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T attr(String name){
        Object value = raw.getAttribute(name);
        if(null != value){
            return (T) value;
        }
        return null;
    }

    public String getParam(String name){
        return raw.getParameter(name);
    }

    public Map<String, String[]> getParams() {
        return raw.getParameterMap();
    }

    public Integer queryAsInt(String name){
        String val = getParam(name);
        if(null != val && !val.equals("")){
            return Integer.valueOf(val);
        }
        return null;
    }

    public String cookie(String name){
        Cookie[] cookies = raw.getCookies();
        if(null != cookies && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
