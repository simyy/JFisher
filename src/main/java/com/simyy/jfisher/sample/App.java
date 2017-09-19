package com.simyy.jfisher.sample;

import com.simyy.jfisher.core.Bootstrap;
import com.simyy.jfisher.Fisher;

public class App implements Bootstrap {
    @Override
    public void init(Fisher fisher) {
        fisher.initDriven("com.simyy.jfisher.sample");
        //HelloController helloController = new HelloController();
        //jfisher.addRoute("/", "sayHello", helloController);
    }
}
