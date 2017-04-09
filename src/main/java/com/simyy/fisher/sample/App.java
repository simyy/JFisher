package com.simyy.fisher.sample;

import com.simyy.fisher.core.Bootstrap;
import com.simyy.fisher.Fisher;


public class App implements Bootstrap {
    @Override
    public void init(Fisher fisher) {

        HelloController helloController = new HelloController();
        fisher.addRoute("/", "sayHello", helloController);
    }
}
