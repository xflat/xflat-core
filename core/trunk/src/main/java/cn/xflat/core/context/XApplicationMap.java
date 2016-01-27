package cn.xflat.core.context;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.bizcreator.core.context.BaseContextMap;

import io.vertx.ext.web.RoutingContext;

import java.util.Iterator;



/**
 * @see javax.faces.context.ExternalContext#getApplicationMap()
 */
public class XApplicationMap extends BaseContextMap<Object> {

    private final RoutingContext routingCtx;

    
    // ------------------------------------------------------------ Constructors


    public XApplicationMap(RoutingContext routingCtx) {
        this.routingCtx = routingCtx;
    }


    // -------------------------------------------------------- Methods from Map
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
        return routingCtx.get(key.toString());
    }


    @Override
    public Object put(String key, Object value) {
        Object result = routingCtx.get(key);
        routingCtx.put(key, value);
        return (result);
    }


    @Override
    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        String keyString = key.toString();
        Object result = routingCtx.get(keyString);
        routingCtx.data().remove(keyString);
        return (result);
    }


    @Override
    public boolean containsKey(Object key) {
        return (routingCtx.get(key.toString()) != null);
    }


    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof XApplicationMap))
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


    @SuppressWarnings("unchecked")
    protected Iterator<Map.Entry<String, Object>> getEntryIterator() {
    	return routingCtx.data().entrySet().iterator();
    }

    @SuppressWarnings("unchecked")
    protected Iterator<String> getKeyIterator() {
    	return routingCtx.data().keySet().iterator();
    }

    @SuppressWarnings("unchecked")
    protected Iterator<Object> getValueIterator() {
    	return routingCtx.data().values().iterator();
    }

} // END ApplicationMap


