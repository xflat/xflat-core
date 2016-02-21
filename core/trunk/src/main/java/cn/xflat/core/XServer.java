package cn.xflat.core;

import java.io.InputStream;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import cn.xflat.common.jdbc.SqlConfigBase;
import cn.xflat.common.mail.SendMail;
import cn.xflat.context.TheContext;
import cn.xflat.core.handler.ApiHandler;
import cn.xflat.core.handler.RmiHandler;
import cn.xflat.core.spring.ConfigType;
import cn.xflat.core.spring.SpringContextHolder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.TemplateHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.ext.web.templ.TemplateEngine;

public class XServer extends AbstractVerticle {

	private static final Logger log = LoggerFactory.getLogger(XServer.class);
	
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
	    
	    //3. 启用BodyHandler 
	    router.route().handler(BodyHandler.create());
	    
	    //3. 开启WebContext，以便spring相关的服务获取上下文进行处理
	    //WebContextHandler wcl = new WebContextHandler();
	    //router.route().handler(wcl);
	    
	    //4. 服务调用
	    router.post("/rmi").blockingHandler(new RmiHandler());
	    router.route("/services").blockingHandler(new ApiHandler());

	    router.route("/static/*").handler(StaticHandler.create());
	    
	    TemplateEngine engine = HandlebarsTemplateEngine.create();
	    TemplateHandler handler = TemplateHandler.create(engine);

	    // This will route all GET requests starting with /dynamic/ to the template handler
	    // E.g. /dynamic/graph.hbs will look for a template in /templates/dynamic/graph.hbs
	    router.get("/dynamic/").handler(handler);

	    // Route all GET requests for resource ending in .hbs to the template handler
	    //router.getWithRegex(".+\\.hbs").handler(handler);
	    
	    //5. 启动服务器
	    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	    
	    printServerInfo();
	}
	
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

}
