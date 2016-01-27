package cn.xflat.core.context;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import com.bizcreator.core.context.StringArrayValuesMap;

import io.vertx.core.http.HttpServerRequest;

import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;



public class XRequestHeaderValuesMap extends StringArrayValuesMap {

	private final HttpServerRequest request;
	
	public XRequestHeaderValuesMap(HttpServerRequest request) {
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