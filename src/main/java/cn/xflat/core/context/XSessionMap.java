package cn.xflat.core.context;

import java.util.Enumeration;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;
import java.util.logging.Logger;

import cn.xflat.context.BaseContextMap;
import cn.xflat.context.ExternalContext;


import io.vertx.ext.web.Session;

import java.util.logging.Level;
import java.io.Serializable;


/**
 * @see javax.faces.context.ExternalContext#getSessionMap()
 */
public class XSessionMap extends BaseContextMap<Object> {
    
	private final Session session;

	private String sessionId;
	
    // ------------------------------------------------------------ Constructors
    public XSessionMap(Session session) {
        this.session = session;

    }
    
    public String getSessionId() {
		return session.id();
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	// -------------------------------------------------------- Methods from Map
    @Override
    public void clear() {
        if (session != null) {
        	session.data().clear();
        }
    }

    // Supported by maps if overridden
    @Override
    public void putAll(Map t) {
        for (Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            Object v = entry.getValue();
            Object k = entry.getKey();

            //noinspection NonSerializableObjectBoundToHttpSession
            session.put((String) k, v);
        }
    }

    @Override
    public Object get(Object key) {
        //Util.notNull("key", key);
        return ((session != null) ? session.get(key.toString()) : null);

    }


    @Override
    public Object put(String key, Object value) {
        //Util.notNull("key", key);
        Object result = session.get(key);

        //noinspection NonSerializableObjectBoundToHttpSession
        session.put(key, value);
        return (result);
    }


    @Override
    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        if (session != null) {
            String keyString = key.toString();
            Object result = session.get(keyString);
            session.remove(keyString);
            return (result);
        }
        return null;
    }


    @Override
    public boolean containsKey(Object key) {
        return ((session != null)
                && session.get(key.toString()) != null);
    }


    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof XSessionMap))
               && super.equals(obj);
    }


    @Override
    public int hashCode() {
        int hashCode =
              7 * ((session != null) ? session.hashCode() : super.hashCode());
        if (session != null) {
            for (Iterator i = entrySet().iterator(); i.hasNext();) {
                hashCode += i.next().hashCode();
            }
        }
        return hashCode;
    }


    // --------------------------------------------- Methods from BaseContextMap


    @SuppressWarnings("unchecked")
    protected Iterator<Map.Entry<String,Object>> getEntryIterator() {
        if (session != null) {
            return session.data().entrySet().iterator();
        } else {
            Map<String,Object> empty = Collections.emptyMap();
            return empty.entrySet().iterator();
        }
    }


    @SuppressWarnings("unchecked")
    protected Iterator<String> getKeyIterator() {
        if (session != null) {
            return session.data().keySet().iterator();
        } else {
            Map<String,Object> empty = Collections.emptyMap();
            return empty.keySet().iterator();
        }
    }


    @SuppressWarnings("unchecked")
    protected Iterator<Object> getValueIterator() {
         if (session != null) {
            return session.data().values().iterator();
        } else {
            Map<String,Object> empty = Collections.emptyMap();
            return empty.values().iterator();
        }
    }


    // --------------------------------------------------------- Private Methods

    /*
    private HttpSession getSession(boolean createNew) {
    	HttpSession session = request.getSession(createNew);
    	if (session == null && sessionId != null) {
    		session = BizContext.getSession(sessionId);
    	}
        return session;
    }*/

} // END SessionMap
