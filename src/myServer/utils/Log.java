package myServer.utils;

import java.util.logging.Logger;

/**
 * @Author: weiqiang
 * @Time: 2020/3/17 下午10:10
 */
public class Log {
    private static Logger log = Logger.getLogger("wq");

    public static void info(String str) {
        log.info("\n"+str);
    }
}
