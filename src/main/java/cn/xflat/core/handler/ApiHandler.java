package cn.xflat.core.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cn.xflat.common.ServiceBase;
import cn.xflat.core.XEnv;
import cn.xflat.util.JSONConverter;
import cn.xflat.util.ServiceUtil;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

public class ApiHandler extends ServiceInvoker implements Handler<RoutingContext> {

	private static final Logger log = LoggerFactory.getLogger(ApiHandler.class);
	
	@Override
	public void handle(RoutingContext rc) {
		
		log.info("starting XEnv WebContext...");
		
		XEnv.start(rc);
		
		HttpServerRequest req = rc.request();
		String pathInfo = req.path();
		if (pathInfo.endsWith("/")) 
			pathInfo = pathInfo.substring(0, pathInfo.length()-1);
		
		String dataStr = req.getParam("data");
		JsonElement jel = null;
		if (dataStr != null) {
			jel = JSONConverter.parseJson(dataStr);
		}
		
		if (pathInfo.startsWith("/bizobjects")) {
			String[] ps = pathInfo.substring(12).split("/");
			if (jel != null && ps.length >= 1) {
				JsonObject json = (JsonObject) jel;
				String modelId = ps[0];
				ServiceBase service = ServiceUtil.getModelService(modelId);
				if (ps.length == 2) {
					String id = ps[1];
					json.addProperty("id", id);
				}
				Map<String, Object> map = service.save(modelId, json);
				writeResponse(rc, map);
				return;
			}
		} else {//执行方法调用
			Object result = ServiceUtil.invoke(pathInfo, jel);
			writeResponse(rc, result);
			return;
		}
		//resp.setCharacterEncoding(RESP_ENCODING);
		writeResponse(rc, null, INVALID_URL, INVALID_URL_MSG);
		
	}
}
