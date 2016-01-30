package cn.xflat.core.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import io.vertx.ext.web.RoutingContext;
import cn.xflat.context.BaseContextMap;



/**
 * @see javax.faces.context.ExternalContext#getRequestMap()
 */
public class XRequestMap extends BaseContextMap<Object> {

    private final RoutingContext routingCtx;
    
    //多线程访问时, 如果不是请求主线程, 无法调用request.setAttribute方法, 需要针对attrs进行操作 
    private Map<String, Object> attrs = new HashMap<>();
   
    //是否请求的分支线程
    private boolean isFork = false;
    
    // ------------------------------------------------------------ Constructors
    public XRequestMap(RoutingContext routingCtx) {
        this.routingCtx = routingCtx;
    }


    // -------------------------------------------------------- Methods from Map

    public boolean isFork() {
    	return isFork;
    }
    
    public void setFork(boolean fork) {
    	isFork = fork;
    }
    
    @Override
    public void clear() {
    	routingCtx.data().clear();
    }


    // Supported by maps if overridden
    @Override
    public void putAll(Map t) {
        for (Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            routingCtx.put((String) entry.getKey(), entry.getValue());
        }
    }


    @Override
    public Object get(Object key) {
        //Util.notNull("key", key);
        Object result = routingCtx.get(key.toString());
        if (result == null)
        	result = getAttr(key.toString());
        return result;
    }


    @Override
    public Object put(String key, Object value) {
        //Util.notNull("key", key);
        Object result = get(key);
        if (isFork) {
        	setAttr(key, value);
        } else {
        	routingCtx.put(key, value);
        }
        return (result);
    }

    public Object getAttr(String key) {
    	return attrs.get(key);
    }
    
    public void setAttr(String key, Object value) {
    	attrs.put(key, value);
    }
    
    @Override
    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        String keyStr = key.toString();
        Object result = routingCtx.get(keyStr);
        routingCtx.data().remove(keyStr);
        if (attrs.containsKey(keyStr)) {
        	result = attrs.remove(keyStr);
        }
        return (result);
    }


    @Override
    public boolean containsKey(Object key) {
        return (routingCtx.get(key.toString()) != null);
    }


    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof XRequestMap))
               && super.equals(obj);
    }


    @Override
    public int hashCode() {
        int hashCode = 7 * routingCtx.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }


    // --------------------------------------------- Methods from BaseContextMap


    protected Iterator<Map.Entry<String,Object>> getEntryIterator() {
    	return routingCtx.data().entrySet().iterator();
    }


    protected Iterator<String> getKeyIterator() {
    	return routingCtx.data().keySet().iterator();
    }


    protected Iterator<Object> getValueIterator() {
    	return routingCtx.data().values().iterator();
    }

} // END RequestMap
