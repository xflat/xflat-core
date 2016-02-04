package cn.xflat.core;

import java.io.InputStream;
import java.net.InetAddress;

import org.springframework.context.ApplicationContext;

import cn.xflat.common.jdbc.SqlConfigBase;
import cn.xflat.common.mail.SendMail;
import cn.xflat.context.TheContext;
import cn.xflat.core.handler.WebContextHandler;
import cn.xflat.core.spring.ConfigType;
import cn.xflat.core.spring.SpringContextHolder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

public class XServer extends AbstractVerticle {

	public static void main(String[] args) {
		
		final Vertx vertx = Vertx.vertx();
		
		//1. 创建一个dummy XEnv，以便通过dummy()调用其实例上的实用方法
		TheContext.dummy = new XEnv();
		
		//2. 加载spring环境
		loadSpringContext(vertx);
		
		//3. 解析sql语句
		//模拟一个context
        XEnv.set(new XEnv());
        
        //7. UPLOAD_DIR
        String uploadDir = TheContext.getUploadDir();
        
        loadSqlConfig();
        
		//4. 部署XServer
	    vertx.deployVerticle(new XServer());
	}
	
	public static void loadSpringContext(Vertx vertx) {
		JsonObject configFiles = new JsonObject();
	    JsonArray xmlFilesArray = new JsonArray();
	    xmlFilesArray.add("spring-context.xml");
	    configFiles.put("configFiles", xmlFilesArray);
	    configFiles.put("configType", ConfigType.XML.getValue());
	    SpringContextHolder.setVertx(vertx);
	    SpringContextHolder.createApplicationContext(configFiles);
	    ApplicationContext context = SpringContextHolder.getApplicationContext();
	    TheContext.springContext = context;
	}
	
	public static void loadSqlConfig() {
		SqlConfigBase sqlConfig = new SqlConfigBase();
        sqlConfig.init();
        sqlConfig.build();
        XEnv.sqlConfig = sqlConfig;
	}
	
	public static void printServerInfo() {
		 //8. 显示服务器启动信息
        System.out.println(">>>>server info:" /*+ sc.getServerInfo()*/);
        try {
        InetAddress inet = InetAddress.getLocalHost();
        System.out.println("=========================================================");
        System.out.println("HostAddress=" + inet.getHostAddress());
        System.out.println("HostName=" + inet.getHostName());
        System.out.println("CanonicalHostName=" + inet.getCanonicalHostName());
        System.out.println("LocalHost=" + inet.getLocalHost());
        //System.out.println("ServerIP=" + sc.getAttribute(TheContext.SERVER_IP));
        System.out.println("SMTP Host=" + SendMail.smtp_host);
        System.out.println("SMTP User=" + SendMail.smtp_user);
        System.out.println("SMTP Password=" + SendMail.smtp_password);
        System.out.println("UPLOAD DIR =" + TheContext.UPLOAD_DIR);
        System.out.println("=========================================================");
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
	}
	
	@Override
	public void start() throws Exception {

	    Router router = Router.router(vertx);
	    
	    //1. 启用cookie
	    router.route().handler(CookieHandler.create());
	    
	    //2. 启用session
	    SessionStore store = LocalSessionStore.create(vertx);
	    SessionHandler sessionHandler = SessionHandler.create(store);

	    // Make sure all requests are routed through the session handler too
	    router.route().handler(sessionHandler);
	    
	    //3. 开启WebContext，以便spring相关的服务获取上下文进行处理
	    WebContextHandler wcl = new WebContextHandler();
	    router.route().handler(wcl);
	    
	    //3. 向客户端发送响应
	    router.route().handler(routingContext -> {
	    	routingContext.response().putHeader("content-type", "text/html").end("Hello World!");
	    });

	    //4. 启动服务器
	    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	    
	    printServerInfo();
	}
}
