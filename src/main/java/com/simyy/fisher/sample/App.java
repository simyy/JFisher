package com.simyy.fisher.sample;

import com.simyy.fisher.core.Bootstrap;
import com.simyy.fisher.Fisher;
import org.apache.log4j.PropertyConfigurator;

public class App implements Bootstrap {
    @Override
    public void init(Fisher fisher) {
        fisher.initDriven("com.simyy.fisher.sample");
        //HelloController helloController = new HelloController();
        //fisher.addRoute("/", "sayHello", helloController);
    }
}
