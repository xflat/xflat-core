package cn.xflat.core.context;

import java.util.Iterator;

import com.bizcreator.core.context.StringArrayValuesMap;

import io.vertx.core.http.HttpServerRequest;


public class XRequestParameterValuesMap extends StringArrayValuesMap {

	private final HttpServerRequest request;
	
	public XRequestParameterValuesMap(HttpServerRequest request) {
        this.request = request;
    }

	@Override
	protected Iterator<java.util.Map.Entry<String, String[]>> getEntryIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Iterator<String> getKeyIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Iterator<String[]> getValueIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
