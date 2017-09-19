package com.simyy.jfisher.sample;

import com.simyy.jfisher.annotion.Route;
import com.simyy.jfisher.annotion.View;
import com.simyy.jfisher.servlet.Request;
import com.simyy.jfisher.servlet.Response;

@View("hello")
public class HelloController {

    @Route(value = "/hello", desc = "sayHello")
    public void sayHello(Request request, Response response){
        response.text("I'm jfisher!");
    }
}
