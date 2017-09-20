package cn.simyy.jfisher.sample;

import cn.simyy.jfisher.annotion.Route;
import cn.simyy.jfisher.annotion.View;
import cn.simyy.jfisher.servlet.Request;
import cn.simyy.jfisher.servlet.Response;

@View("hello")
public class HelloController {

    @Route(value = "/hello", desc = "sayHello")
    public void sayHello(Request request, Response response){
        response.text("I'm jfisher!");
    }
}
