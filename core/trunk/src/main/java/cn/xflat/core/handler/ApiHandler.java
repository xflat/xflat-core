package cn.xflat.core.handler;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;

import cn.xflat.common.ServiceBase;
import cn.xflat.common.jdbc.JdbcBase;
import cn.xflat.common.mongodb.MongoBase;
import cn.xflat.core.XEnv;
import cn.xflat.util.JSONConverter;
import cn.xflat.util.ServiceUtil;
import cn.xflat.util.Util;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

public class ApiHandler extends ServiceInvoker implements Handler<RoutingContext> {

	private static final Logger log = LoggerFactory.getLogger(ApiHandler.class);
	
	@Override
	public void handle(RoutingContext rc) {
		
		log.info("starting XEnv WebContext...");
		
		XEnv.start(rc);
		
		HttpServerRequest req = rc.request();
		if (req.method() == HttpMethod.GET) {
			doGet(rc);
		} else {
			doPost(rc);
		}
		
	}
	
	private void doPost(RoutingContext rc) {
		
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
	
	private void doGet(RoutingContext rc) {
		
		HttpServerRequest req = rc.request();
		String pathInfo = req.path();
		
		if (pathInfo.endsWith("/")) 
			pathInfo = pathInfo.substring(0, pathInfo.length()-1);
		if (pathInfo.startsWith("/bizobjects")) {
			String[] ps = pathInfo.substring(12).split("/");
			if (ps.length == 2) {
				String modelId = ps[0];
				String id = ps[1];
				ServiceBase service = ServiceUtil.getModelService(modelId);
				Map<String, Object> map = service.queryForModelMap(modelId, id);
				writeResponse(rc, map);
				return;
			}
		} else if (pathInfo.startsWith("/lovs")) {
			String[] ps = pathInfo.substring(6).split("/");
			if (ps.length >= 1) {
				String lovId = ps[0];
				List<BasicDBObject> list = MongoBase.obj().findLov(lovId);
				writeResponse(rc, list);
				return;
			}
		} else if (pathInfo.startsWith("/query")) {
			String[] ps = pathInfo.substring(7).split("/");
			if (ps.length >= 2) {
				String sqlId = ps[0];
				String filter = ps[1];
				int offset = 0;
				int rows = -1;
				if (ps.length == 3) {
					String pageParam = ps[2];
					String[] ss = pageParam.split(",\\s*");
					offset = Integer.valueOf(ss[0]);
					rows = Integer.valueOf(ss[1]);
				}
				JdbcBase service = JdbcBase.getInstance();
				Map<String, Object> params = new HashMap<String, Object>();
				if (filter != null) {
					JsonObject json = (JsonObject) JSONConverter.parseJson(filter);
					params = Util.toMap(json);
				}
				Map<String, Object> result = service.limitQuery(sqlId, params, offset, rows);
				writeResponse(rc, result);
				return;
			}
		} else  if (pathInfo.startsWith("/currentUsername")) {
			String result = rc.session().get("username");
			writeResponse(rc, result);
			return;
		} else  if (pathInfo.startsWith("/print")) {
			//todo ...
		} else {//执行方法调用
			Object result = ServiceUtil.invoke(pathInfo, null);
			writeResponse(rc, result);
			return;
		}
		//resp.setCharacterEncoding(RESP_ENCODING);
		writeResponse(rc, null, INVALID_URL, INVALID_URL_MSG);
	}
}
