package cn.xflat.core.context;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import cn.xflat.context.BaseContextMap;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.web.RoutingContext;

import java.util.Iterator;



/**
 * @see javax.faces.context.ExternalContext#getApplicationMap()
 */
public class XApplicationMap extends BaseContextMap<Object> {

    private final SharedData  sd;
    private LocalMap<String, Object> map;
    
    // ------------------------------------------------------------ Constructors


    public XApplicationMap(SharedData sd) {
        this.sd = sd;
        this.map = sd.getLocalMap("_app_context");
    }


    // -------------------------------------------------------- Methods from Map
    @Override
    public void clear() {
    	map.clear();
    }


    // Supported by maps if overridden
    @Override
    public void putAll(Map t) {
        for (Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            map.put((String) entry.getKey(), entry.getValue());
        }
    }


    @Override
    public Object get(Object key) {
        return map.get(key.toString());
    }


    @Override
    public Object put(String key, Object value) {
        Object result = map.get(key);
        map.put(key, value);
        return (result);
    }


    @Override
    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        String keyString = key.toString();
        Object result = map.get(keyString);
        map.remove(keyString);
        return (result);
    }


    @Override
    public boolean containsKey(Object key) {
        return (map.get(key.toString()) != null);
    }


    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof XApplicationMap))
                   && super.equals(obj);
    }


    @Override
    public int hashCode() {
        int hashCode = 7 * map.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }


    // --------------------------------------------- Methods from BaseContextMap


    @SuppressWarnings("unchecked")
    protected Iterator<Map.Entry<String, Object>> getEntryIterator() {
    	return new EntryIterator(map.keySet().iterator());
    }

    @SuppressWarnings("unchecked")
    protected Iterator<String> getKeyIterator() {
    	return map.keySet().iterator();
    }

    @SuppressWarnings("unchecked")
    protected Iterator<Object> getValueIterator() {
    	return map.values().iterator();
    }

} // END ApplicationMap


