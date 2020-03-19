package myServer.factory;

import myServer.utils.MyServer;

import java.util.HashMap;

/**
 * @Author: weiqiang
 * @Time: 2020/3/17 下午10:57
 */
public class FactoryBean {
    public final static HashMap<String, MyServer> beanFactory = new HashMap<>();

    private FactoryBean() {
    }

}
