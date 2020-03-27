//package myServer.linck;
//
//
//import myServer.linck.impl.MySocketChannel;
//import myServer.utils.CloseUtil;
//
//import java.io.Closeable;
//import java.io.IOException;
//import java.nio.channels.SocketChannel;
//import java.util.UUID;
//
///**
// * @Author: weiqiang
// * @Time: 2020/3/10 上午10:58
// */
//public class Connector implements Closeable, MySocketChannel.OnChannelStatusChangedListener {
//    private UUID key = UUID.randomUUID();
//    private SocketChannel channel;
//    private MySocketChannel mySocketChannel;
//
//    public void setup(SocketChannel socketChannel) throws IOException {
//        socketChannel.configureBlocking(false);
//        this.channel = socketChannel;
//        Context context = Context.get();
//        this.mySocketChannel = new MySocketChannel(channel, context.getIoProvider(), this);
//        statRead();
//    }
//
//    private void statRead() {
//        if (mySocketChannel != null) {
//                mySocketChannel.receiveAsync();
//        }
//    }
//
//    private void send(String str) {
//        mySocketChannel.send(str);
//
//    }
//
//    @Override
//    public void close() throws IOException {
//        channel.close();
//        mySocketChannel = null;
//    }
//
//    @Override
//    public void onChannelClosed(SocketChannel channel) {
//        CloseUtil.close(this);
//    }
//}
