package cn.xflat.core.handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.xflat.core.XEnv;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class WebContextHandler implements Handler<RoutingContext> {

	private static final Logger log = LoggerFactory.getLogger(WebContextHandler.class);
	
	@Override
	public void handle(RoutingContext context) {
		log.info("starting XEnv WebContext...");
		XEnv.start(context);
		context.next();
	}
	
}
