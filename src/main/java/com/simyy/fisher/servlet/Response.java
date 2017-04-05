package com.simyy.fisher.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
    private HttpServletResponse raw;

    public Response(HttpServletResponse httpServletResponse) {
        this.raw = httpServletResponse;
        this.raw.setHeader("Framework", "Fisher");
    }

    public void text(String text) {
        raw.setContentType("text/plan;charset=UTF-8");
        this.print(text);
    }

    public void html(String html) {
        raw.setContentType("text/html;charset=UTF-8");
        this.print(html);
    }

    private void print(String str){
        try {
            OutputStream outputStream = raw.getOutputStream();
            outputStream.write(str.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cookie(String name, String value){
        Cookie cookie = new Cookie(name, value);
        raw.addCookie(cookie);
    }

    public HttpServletResponse getRaw() {
        return raw;
    }

    public void redirect(String location) {
        try {
            raw.sendRedirect(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
