package cn.xflat.core.context;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import cn.xflat.context.BaseContextMap;
import cn.xflat.context.ExternalContext;
import cn.xflat.context.ICookie;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;

public class XRequestCookieMap extends BaseContextMap<Object> implements ICookie {

	private final RoutingContext routingCtx;
	
	public XRequestCookieMap(RoutingContext routingCtx) {
        this.routingCtx = routingCtx;
    }
	
	// -------------------------------------------------------- Methods from Map
	@Override
    public Object get(Object key) {
		if (key == null) return null;
        return routingCtx.getCookie(key.toString());
    }
	
	public String getValue(String key) {
		Cookie cookie = (Cookie) get(key);
		if (cookie != null) {
			return cookie.getValue();
		}
		return null;
	}
	
	@Override
    public Set<Map.Entry<String,Object>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }


	    @Override
	    public Set<String> keySet() {
	        return Collections.unmodifiableSet(super.keySet());
	    }


	    @Override
	    public Collection<Object> values() {
	        return Collections.unmodifiableCollection(super.values());
	    }


	    @Override
	    public boolean equals(Object obj) {
	        return !(obj == null || !(obj.getClass()
	                == ExternalContext.theUnmodifiableMapClass)) && super.equals(obj);
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
	        return new EntryIterator(
	                new CookieArrayEnumerator(routingCtx.cookies()));
	    }


	    protected Iterator<String> getKeyIterator() {
	        return new KeyIterator(
	                new CookieArrayEnumerator(routingCtx.cookies()));
	    }


	    protected Iterator<Object> getValueIterator() {
	        return new ValueIterator(
	            new CookieArrayEnumerator(routingCtx.cookies()));
	    }
	    
	 // ----------------------------------------------------------- Inner Classes
	    private static class CookieArrayEnumerator implements Enumeration {

	    	Cookie[] cookies;
	        int curIndex = -1;
	        int upperBound;

	        public CookieArrayEnumerator(Set<Cookie> cookies) {
	        	if (cookies != null) {
	        		this.cookies = cookies.toArray(this.cookies);
	        	} else {
	        		this.cookies = null;
	        	}
	            
	            upperBound = ((this.cookies != null) ? this.cookies.length : -1);
	        }

	        public boolean hasMoreElements() {
	            return (curIndex + 2 <= upperBound);
	        }

	        public Object nextElement() {
	            curIndex++;
	            if (curIndex < upperBound) {
	                return cookies[curIndex].getName();
	            } else {
	                throw new NoSuchElementException();
	            }
	        }

	    }

}
