package myServer;

import myServer.annotation.RequestMappting;
import myServer.factory.FactoryBean;
import myServer.linck.Context;
import myServer.linck.imp2.Provider;
import myServer.utils.ClassUtils;
import myServer.utils.Constants;
import myServer.utils.MyServer;

import java.io.IOException;
import java.util.Set;

/**
 * @Author: weiqiang
 * @Time: 2020/3/26 下午11:22
 */
public class Application {
    private static Application INSTANT = null;


    public static Application setup() {
        if (INSTANT == null) {
            INSTANT = new Application();
        }
        return INSTANT;
    }

    public Application port(int port) {
        Constants.PORT_SERVER = port;
        return INSTANT;
    }

    public Application path(String path) {
        Constants.SCAN_PATH = path;
        return INSTANT;
    }

    public Application buffer_size(int size) {
        Constants.IOARGS_SIZE = size;
        return INSTANT;
    }

    public Application intput_pool_size(int size) {
        Constants.INPUT_POOL_SIZE = size;
        return INSTANT;
    }

    public Application output_pool_size(int size) {
        Constants.OUTPUT_POOL_SIZE = size;
        return INSTANT;
    }

    public void start() {
        Set<String> servers = ClassUtils.getClassName(Constants.SCAN_PATH, true);
        try {
            for (String server : servers) {
                if (server.startsWith("."))
                    server = server.substring(1);
                Class<?> aClass = null;
                aClass = Class.forName(server);
                if (aClass.isAnnotationPresent(RequestMappting.class)) {
                    Object o = aClass.newInstance();
                    RequestMappting annotation = aClass.getAnnotation(RequestMappting.class);
                    FactoryBean.beanFactory.put(annotation.mapping(), (MyServer) o);
                }
            }
            Context.setup(new Provider());
            TcpServer tcpServer = new TcpServer();
            boolean start = tcpServer.start(Constants.PORT_SERVER);
            if (!start) {
                System.out.println("TCP服务器启动失败");
                return;
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException  e) {
            e.printStackTrace();
        }

    }
}
