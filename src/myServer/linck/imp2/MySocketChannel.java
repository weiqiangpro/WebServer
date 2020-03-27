package myServer.linck.imp2;

import myServer.linck.IoArgs;
import myServer.linck.IoProvider;
import myServer.utils.CloseUtil;
import myServer.utils.Constants;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * @Author: weiqiang
 * @Time: 2020/3/10 上午10:50
 */
public class MySocketChannel implements Closeable {
    private Logger log = Logger.getLogger("a");
    private boolean isClosed = false;
    private final SocketChannel channel;
    private IoArgs ioArgs = new IoArgs();
    private ExecutorService outputHandel;

    public MySocketChannel(SocketChannel channel, ExecutorService inputHandel,ExecutorService outputHandel) throws IOException {
        this.channel = channel;
        this.outputHandel = outputHandel;
        inputHandel.execute(inputCallback);
    }

    @Override
    public void close() throws IOException {
        isClosed = true;
        channel.close();
    }

    private final IoProvider.HandleInputCallback inputCallback = new IoProvider.HandleInputCallback() {
        @Override
        protected void canProviderInput() {
            if (isClosed) {
                return;
            }
            IoArgs args = MySocketChannel.this.ioArgs;
            int read = args.read(channel);
//            int read = IoArgs.read(channel);
            if (read <= 0){
                log.info("wrong");
                send(Constants.ERROR_MES);
                return;
            }
            if (read > 0) {
//                String str = args.bufferString();
//                int finish;
//                finish = str.indexOf("HTTP") - 1;
//                int start = str.indexOf("/");
//                if (start == -1 || finish == -1 || finish <= start) {
////                    System.out.println("wrong");
//                    CloseUtil.close(MySocketChannel.this);
//                    log.info("wrong");
//                    send(Constants.ERROR_MES);
//                    return;
//                }
//                String path = str.substring(start, finish);
//                if ((finish = path.indexOf("?")) != -1) {
//                    path = path.substring(0, finish);
//                }
//
//                log.info(path);
//                MyServer myServer = FactoryBean.beanFactory.get(path);
//
//                if (myServer != null)
//                    send(myServer.getMsg());
//                else
                    send(Constants.OK_MES);
            }
        }
    };

    public void send(String str) {
        outputCallback.setStr(str);
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        outputHandel.execute(outputCallback);
//        outputCallback.run();
        //log.info("发送成功");
    }

    private final IoProvider.HandleOutputCallback outputCallback = new IoProvider.HandleOutputCallback() {
        @Override
        protected void canProviderInput() {
            if (isClosed) {
                return;
            }
           // IoArgs args = MySocketChannel.this.ioArgs;
            try {
                int read = new IoArgs().write(channel,getStr());
                CloseUtil.close(MySocketChannel.this);
            } catch (IOException ignored) {
                CloseUtil.close(MySocketChannel.this);
            }
        }
    };



    public interface OnChannelStatusChangedListener {
        void onChannelClosed(SocketChannel channel);
    }

}