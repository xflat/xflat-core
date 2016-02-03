package cn.xflat.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cn.xflat.context.ExternalContext;
import cn.xflat.context.TheContext;
import cn.xflat.core.context.XWebContext;
import io.vertx.ext.web.RoutingContext;

public class XEnv extends TheContext {

	public XEnv() {
		
	}
	
	public XEnv(XWebContext wc) {
		this.webContext = wc;
		set(this);
	}
	
	public XEnv(RoutingContext routingCtx) {
		this(new XWebContext(routingCtx));
	}
	
	public static void start(RoutingContext routingCtx) {
		new XEnv(routingCtx);
	}
	
	public static XEnv obj() {
		return (XEnv) (instance.get());
	}
	
	@Override
	public void execute(Runnable task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExternalContext newJobWebContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getResoucePath(String module, String action, String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getResoucePath(String module, String action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRealPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadSql(String module, String action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonElement loadDef(String module, String viewType, boolean isExt) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject loadViewDef(String module, String viewType) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject getBizLogic(String modelId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject getDefaultValues(String modelId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject getDefaultValues(String module, String viewType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject getFieldRule(String modelId, String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGridConf(String module, String viewType, String gridKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUploadDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCustomPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteIP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRequest() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCookie(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCookieValue(Object cookie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> loginInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeResponse(String content) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
