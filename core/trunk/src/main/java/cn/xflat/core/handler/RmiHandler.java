package cn.xflat.core.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import cn.xflat.core.XEnv;
import cn.xflat.util.JSONConverter;
import cn.xflat.util.ServiceUtil;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class RmiHandler extends ServiceInvoker implements Handler<RoutingContext> {

	private static final Logger log = LoggerFactory.getLogger(RmiHandler.class);
	
	public final static String SERVICE = "service";
    public final static String METHOD = "mn";
    public final static String ARGS = "args";
    public final static String TYPES = "types";
    
	@Override
	public void handle(RoutingContext rc) {
		
		log.debug("starting XEnv WebContext...");
		
		XEnv.start(rc);
		
		String serviceName = rc.request().getParam(SERVICE);
		String methodName = rc.request().getParam(METHOD);
        String argStr = rc.request().getParam(ARGS);
        String typeStr = rc.request().getParam(TYPES);
        
        //Write the result in the http stream
        //response.addHeader("jsessionid", session.getId());
        try {
        	Object result = ServiceUtil.invoke(serviceName, methodName, argStr, typeStr);
        	writeResponse(rc, result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
	}
	
	
	
}
