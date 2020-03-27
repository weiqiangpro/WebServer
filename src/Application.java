import myServer.server.WebServers;

/**
 * @Author: weiqiang
 * @Time: 2020/3/18 下午8:11
 */
public class Application {
    public static void main(String[] args) {
        myServer.Application
                .setup()
                .port(8888)
                .path("wq")
                .start();

//       WebServers.setup().port(8888).path("wq").run();
    }
}
