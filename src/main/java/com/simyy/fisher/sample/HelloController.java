package com.simyy.fisher.sample;

import com.simyy.fisher.servlet.Request;
import com.simyy.fisher.servlet.Response;

public class HelloController {

    public void sayHello(Request request, Response response){
        response.text("I'm fisher!");
    }
}
