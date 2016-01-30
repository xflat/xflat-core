package cn.xflat.core.context;

import java.util.Iterator;

import cn.xflat.context.BaseContextMap;
import io.vertx.ext.web.RoutingContext;

public class XInitParameterMap extends BaseContextMap<String> {

	private final RoutingContext routingCtx;
	
	public XInitParameterMap(RoutingContext routingCtx) {
        this.routingCtx = routingCtx;
    }
	
	@Override
	protected Iterator<java.util.Map.Entry<String, String>> getEntryIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Iterator<String> getKeyIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Iterator<String> getValueIterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
