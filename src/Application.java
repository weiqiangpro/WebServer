import myServer.WebServers;

/**
 * @Author: weiqiang
 * @Time: 2020/3/18 下午8:11
 */
public class Application {
    public static void main(String[] args) {
        WebServers.setup()
                .port(8080)
                .run();
    }
}
