package wq;

import myServer.annotation.WebServer;
import myServer.json.MyJson;
import myServer.utils.MyServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: weiqiang
 * @Time: 2020/3/18 下午8:09
 */
@WebServer(mapping = "/hello")
public class S1  extends MyServer {
    @Override
    protected Object getMethod() {
        Map<String,Integer> map = new HashMap<>();
        map.put("aaa",1);
        map.put("bbb",2);
       return MyJson.json(map);
    }
}
