# WebServer
# 基于restful风格的HTTP服务器  
**目前尚在bio阶段，完成开发后是基于NIO和多线程池的高并发服务器**  
**原生JSON解析，基于JDK的动态代理实现拦截器功能，并做出了统一回复格式**  
**基于注解的风格，轻松开发**  

*注册拦截*  
@WebServer(mapping = "/hello")  
public class hello  extends MyServer

*启动服务器*  
WebServers.setup().port(8080).run();
