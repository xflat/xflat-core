package cn.xflat.core.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cn.xflat.core.XEnv;
import cn.xflat.util.JSONConverter;
import cn.xflat.util.Util;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class RmiHandler implements Handler<RoutingContext> {

	private static final Logger log = LoggerFactory.getLogger(RmiHandler.class);
	
	public final static String ERROR = "error";
	public final static String ERROR_CODE = "errorCode";
    public final static String MESSAGE = "message";
	public final static String STACK_TRACE = "stackTrace";
    public final static String STATUS = "status";
    public final static String VALUE = "value";
    public final static int NORMAL = 200;
    
	public final static String RESP_ENCODING = "UTF-8";
    public static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    
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
        
        log.debug(">>>invocation: " + serviceName + ", " + methodName + ", " + argStr + ", " + typeStr);
        
        JsonArray jargs = (JsonArray)JSONConverter.parseJson(argStr);
        JsonElement[] args;
        if( jargs == null ) { 
        	args =  new JsonElement[] {};
        }
        else {
        	args = new JsonElement[jargs.size()];
        }
        for( int i = 0; i < jargs.size(); i++ ) {
        	args[i] = jargs.get(i);
        }
        
        String[] types = JSONConverter.gson().fromJson(typeStr, String[].class);

        //将types转换为实际参数类型
        Class[] paramTypes = Util.getActParamTypes(types);

        //将参数args转换为实际参数
        Object[] actArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
        	if (args[i].isJsonPrimitive()) {
        		actArgs[i] = Util.getActParamValue(args[i].getAsString(), paramTypes[i]);
        	}
        	else if (args[i].isJsonObject()){
        		actArgs[i] = args[i].getAsJsonObject();
        		paramTypes[i] = JsonObject.class;
        	}
        	else if (args[i].isJsonNull()) {
        		actArgs[i] = null;
        	}
        	else if (args[i].isJsonArray()){
        		actArgs[i] = args[i].getAsJsonArray();
        		paramTypes[i] = JsonArray.class;
        	}
        	else {
        		actArgs[i] = args[i];
        	}
        }
        
        //根据serviceName、methodName、paramTypes、actArgs调用相应的服务
        Object result = null;
        Object service = XEnv.getBean(serviceName);
        if (service != null) {
        	try {
                Method invokedMethod = Util.findMethod(service.getClass(), methodName, paramTypes);
                result = invokedMethod.invoke(service, args);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
                if (ex.getTargetException() != null) {
                    ex.getTargetException().printStackTrace();
                }
                /*
                if (ex.getTargetException() instanceof NoSuchObjectException) {
                    NoSuchObjectException noSuchObjectException = (NoSuchObjectException) ex.getTargetException();
                    throw noSuchObjectException;
                }*/

                //The target exception is the only result sent back on the client
                result = ex.getTargetException();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                log.error(null, ex);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                log.error(null, ex);
            } catch (SecurityException ex) {
                ex.printStackTrace();
                log.error(null, ex);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                //1. 关闭相关的资源
            }
        }
        
        //Write the result in the http stream
        //response.addHeader("jsessionid", session.getId());
        try {
        	writeResponse(rc, result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
	}
	
	protected void writeResponse(RoutingContext rc, Object result) {
		writeResponse(rc, result, null, null);
	}
	
	protected void writeResponse(RoutingContext rc, Object result, String errorCode, String message) {
		
		JsonObject jsonResult = new JsonObject();
        if (result instanceof Throwable) {//抛出异常
            Throwable ex = (Throwable) result;
            jsonResult.addProperty(STATUS, "Error");
            jsonResult.addProperty(MESSAGE, ex.getMessage());
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            //jsonResult.addProperty(STACK_TRACE, sw.toString());
        }  
        else {//调用成功
            jsonResult.add(VALUE, JSONConverter.toJsonTree(result));
            if (errorCode != null) {
            	jsonResult.addProperty(STATUS, "Error");
            	jsonResult.addProperty(ERROR_CODE, errorCode);
            } else {
            	jsonResult.addProperty(STATUS, "Success");
            }
            if (message != null)
            	jsonResult.addProperty(MESSAGE, message);
        }
        HttpServerResponse response = rc.response();
        response.putHeader("content-type", "application/json");
        //response.setCharacterEncoding(RESP_ENCODING);
        response.write(JSONConverter.gson().toJson(jsonResult)).end();
	}
	
}
