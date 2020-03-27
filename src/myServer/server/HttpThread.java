package myServer.server;

import myServer.factory.FactoryBean;
import myServer.utils.MyServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author: weiqiang
 * @Time: 2020/3/16 下午8:39
 */
public class HttpThread implements Runnable {
    private Socket socket;//连接点

    public HttpThread(Socket socket) {
        super();
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] b = new byte[256];
            int len = 0;
            StringBuilder str = new StringBuilder();
            while ((len = inputStream.read(b)) > 0) {
                str.append(new String(b, 0, len));
                if (len < 256)
                    break;
            }
            String msg = str.toString();
           // Log.info(msg);
            int finish;
            if ((finish = str.indexOf("?")) == -1) {
                finish = str.indexOf("HTTP") - 1;
            }
            int start = msg.indexOf("/");
            if (start == -1 || finish == -1 || finish <= start) {
                System.out.println("wrong");
                return;
            }
            String path = msg.substring(start, finish);
            String[] split = path.split("/");
          //  Log.info(path);
            System.out.println(path);
            MyServer myServer = FactoryBean.beanFactory.get(path);
         //   Thread.sleep(1);
            if (myServer == null) {
                OutputStream os = socket.getOutputStream();//获得输出流
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n"
                        + "Content-Length: 18\r\n" + "\r\n" + "<h1>Not Found</h1>";
                os.write(errorMessage.getBytes());
                os.close();
            } else
                myServer.execute(socket);


            inputStream.close();
            socket.close();//关闭socket
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


