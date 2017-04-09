package com.simyy.fisher.sample;

import com.simyy.fisher.annotion.Route;
import com.simyy.fisher.annotion.View;
import com.simyy.fisher.servlet.Request;
import com.simyy.fisher.servlet.Response;

@View("hello")
public class HelloController {

    @Route(value = "/hello", desc = "sayHello")
    public void sayHello(Request request, Response response){
        response.text("I'm fisher!");
    }
}
