package myServer.linck.imp2;

import myServer.linck.IoProvider;
import myServer.utils.Constants;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * @Author: weiqiang
 * @Time: 2020/3/10 上午10:34
 */
public class Provider implements IoProvider {
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final ReentrantLock lock = new ReentrantLock();
    private final AtomicBoolean inRegIn = new AtomicBoolean(false);

    private final Selector readSelector;

    Logger log = Logger.getLogger("c");
    private ExecutorService inputHandel = Executors.newFixedThreadPool(Constants.INPUT_POOL_SIZE);
    private ExecutorService outputHandel = Executors.newFixedThreadPool(Constants.OUTPUT_POOL_SIZE);

    public Provider() throws IOException {
        this.readSelector = Selector.open();
        startRead();
    }

    private void startRead() {
        Thread thread = new Thread("Clink IoSelectorProvider ReadSelector Thread") {
            @Override
            public void run() {
                while (!isClosed.get()) {

                    try {
                        int read = readSelector.select();
                        if (read == 0) {
                            waitSelection(inRegIn);
                            continue;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    lock.lock();
                    try {
                        Set<SelectionKey> selectionKeys = readSelector.selectedKeys();
                        for (SelectionKey selectionKey : selectionKeys) {
                            if (selectionKey.isReadable()) {
                                selectionKey.cancel();
                                SocketChannel channel = (SocketChannel) selectionKey.channel();
                                new MySocketChannel(channel, inputHandel, outputHandel);
                            }
                        }

                        selectionKeys.clear();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        };
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }


    public boolean registerInput(SocketChannel channel) {
        lock.lock();
        try {
            inRegIn.set(true);
            readSelector.wakeup();
            channel.configureBlocking(false);
            channel.register(readSelector, SelectionKey.OP_READ);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            inRegIn.set(false);
            try {
                inRegIn.notify();
            } catch (Exception ignored) {
            } finally {
                lock.unlock();
            }
        }
    }


    private void waitSelection(AtomicBoolean locker) {
        lock.lock();
        try {
            if (locker.get())
                locker.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() throws IOException {

    }
}
