package myServer.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author: weiqiang
 * @Time: 2020/3/17 下午10:33
 */
public abstract class MyServer {
    public void execute(Socket socket) {
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            String msg = getMsg(getMethod());
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract Object getMethod();

    private String toJson(Object o){
        return o.toString();
    }
    private String getMsg(Object msg) {
        String mes = toJson(msg);
        return "HTTP/1.1 200 OK\r\n" +
                "Date: Fri, 22 May 2009 06:07:21 GMT\r\n" +
                "Content-Type: text/json; charset=UTF-8\r\n" +
                "Content-Length: " + mes.getBytes().length + "\r\n\r\n" +
                mes;
    }
}
