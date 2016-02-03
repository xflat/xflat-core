package cn.xflat.core.handler;
import cn.xflat.core.XEnv;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class WebContextHandler implements Handler<RoutingContext> {

	@Override
	public void handle(RoutingContext context) {
		XEnv.start(context);
		context.next();
	}
	
}
