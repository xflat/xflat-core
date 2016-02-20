package cn.xflat.core.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cn.xflat.common.ServiceBase;
import cn.xflat.common.jdbc.JdbcBase;
import cn.xflat.core.XEnv;
import cn.xflat.util.JSONConverter;
import cn.xflat.util.Util;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class ApiHandler implements Handler<RoutingContext> {

	private static final Logger log = LoggerFactory.getLogger(ApiHandler.class);
	
	@Override
	public void handle(RoutingContext rc) {
		log.info("starting XEnv WebContext...");
		XEnv.start(rc);
		
		rc.next();
	}

	/**
	 * 服务调用: 目前只接受JsonObject、JsonArray、无参数三种类型的参数
	 * @param servicePath
	 * @param args
	 * @return
	 */
	protected Object invokeService(String servicePath, JsonElement args) {
		
		Object result = null;
		
		String[] ps = servicePath.substring(1).split("/");
		if (ps.length >= 2) {
			String modelId = ps[0];
			String mn = ps[1];
			if (args == null && ps.length >= 3) {
				String s = ps[2];
				if (!s.startsWith("{") && !s.startsWith("["))
					s = "{" + s + "}";
				args = JSONConverter.parseJson(s);
			}
			
			Method invokedMethod = null;
			ServiceBase service = getService(modelId);
			if (args == null)
				invokedMethod = Util.findMethod(service.getClass(), mn, new Class[]{});
			else if (args instanceof JsonObject)
				invokedMethod = Util.findMethod(service.getClass(), mn, new Class[]{JsonObject.class});
			else if (args instanceof JsonArray)
				invokedMethod = Util.findMethod(service.getClass(), mn, new Class[]{JsonArray.class});
			
			if (invokedMethod != null) {
				try {
					result = invokedMethod.invoke(service, args);
				} catch (IllegalArgumentException ex) {
					result = ex;
					ex.printStackTrace();
				} catch (IllegalAccessException ex) {
					result = ex;
					ex.printStackTrace();
				} catch (InvocationTargetException ex) {
					ex.printStackTrace();
					result = ex.getTargetException();
				}
			}
		}
		return result;
	}
	
	/**
	 * 根据模型id获取对应的服务
	 * @param modelId
	 * @return
	 */
	protected ServiceBase getService(String modelId) {
		String svcName = modelId + "Service";
		if (modelId.endsWith("Service")) {
			svcName = modelId;
		}
		ServiceBase service = (ServiceBase) XEnv.getBean(svcName);
		if (service == null) {
			service = JdbcBase.getInstance();
		}
		return service;
	}
}
