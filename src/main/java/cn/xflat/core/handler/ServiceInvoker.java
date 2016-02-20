package cn.xflat.core.handler;

import com.google.gson.JsonObject;

import cn.xflat.util.JSONConverter;
import cn.xflat.util.ServiceUtil;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class ServiceInvoker {

	public final static String ERROR = "error";
	public final static String ERROR_CODE = "errorCode";
    public final static String MESSAGE = "message";
	public final static String STACK_TRACE = "stackTrace";
    public final static String STATUS = "status";
    public final static String VALUE = "value";
    public final static int NORMAL = 200;
    
    public final static String RESP_ENCODING = "UTF-8";
    public static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    
    //通常是java swing客户端调用
    public static final String CONTENT_TYPE_BSF = "application/x-bsf";
    
    //通常ajax调用, 如：前端的io-rmi
	public final static String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
	
    public final static String NOT_LOGIN = "NOT_LOGIN";
	public final static String NOT_LOGIN_MSG = "必须先登录, 才能执行该操作!";
	
	public final static String INVALID_USER = "INVALID_USER";
	public final static String INVALID_USER_MSG = "无效的用户或密码!";
	
	public final static String INVALID_URL = "INVALID_URL";
	public final static String INVALID_URL_MSG = "无效的资源路径!";
	
	public final static String LOGIN_SERVICE = "base.PersonService";
	
	protected void writeResponse(RoutingContext rc, Object result) {
		writeResponse(rc, result, null, null);
	}
	
	protected void writeResponse(RoutingContext rc, Object result, String errorCode, String message) {
		
		JsonObject jsonResult = ServiceUtil.buildJsonResult(result, errorCode, message);
		
        HttpServerResponse response = rc.response();
        response.putHeader("content-type", "application/json");
        //response.setCharacterEncoding(RESP_ENCODING);
        response.write(JSONConverter.gson().toJson(jsonResult)).end();
	}
	
}
