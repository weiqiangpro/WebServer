package myServer;

import myServer.factory.FactoryBean;
import myServer.utils.ClassUtils;
import myServer.utils.Log;
import myServer.utils.MyServer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

/**
 * @Author: weiqiang
 * @Time: 2020/3/16 下午8:39
 */
public class WebServers {

    private static WebServers INSTANT = null;
    private  int PORT = 8080;
    private  String PATH = "";
    private WebServers() {
    }

    public static WebServers setup() {
        if (INSTANT == null) {
            INSTANT = new WebServers();
        }
        return INSTANT;
    }

    public  WebServers port(int port) {
        PORT = port;
        return INSTANT;
    }

    public WebServers path(String path){
        PATH = path;
        return INSTANT;
    }

    public  WebServers run() {
        try {
            Set<String> servers = ClassUtils.getClassName(PATH, true);
            for (String server : servers) {
                if (server.startsWith("."))
                    server = server.substring(1);
                Class<?> aClass = Class.forName(server);
                if (aClass.isAnnotationPresent(myServer.annotation.WebServer.class)) {
                    Object o = aClass.newInstance();
                    myServer.annotation.WebServer annotation = aClass.getAnnotation(myServer.annotation.WebServer.class);
                    FactoryBean.beanFactory.put(annotation.mapping(), (MyServer) o);
                }
            }
            //用端口号创建一个ServerSocket对象，监听8081端口
            ServerSocket ss = new ServerSocket(PORT);
            //开始循环监听来自客户端的请求
            Socket s = null;
            Log.info("服务器启动成功!，端口号为8080");
            while (true) {
                if (!((s = ss.accept()) != null)) break;
                new HttpThread(s).start(); //开始一个新的线程
            }
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return INSTANT;
    }

}