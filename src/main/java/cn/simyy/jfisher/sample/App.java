package cn.simyy.jfisher.sample;

import cn.simyy.jfisher.core.Bootstrap;
import cn.simyy.jfisher.JFisher;

public class App implements Bootstrap {
    @Override
    public void init(JFisher jFisher) {
        jFisher.initDriven("com.simyy.jfisher.sample");
        //HelloController helloController = new HelloController();
        //jfisher.addRoute("/", "sayHello", helloController);
    }
}
