package cn.xflat.core.context;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


import com.bizcreator.core.context.BaseContextMap;
import com.bizcreator.core.context.ExternalContext;

import io.vertx.core.http.HttpServerRequest;

public class XRequestHeaderMap extends BaseContextMap<String> {

	private final HttpServerRequest request;
	
	public XRequestHeaderMap(HttpServerRequest request) {
        this.request = request;
    }
	
	 // -------------------------------------------------------- Methods from Map


    @Override
    public String get(Object key) {
        return (request.getHeader(key.toString()));
    }
    
    @Override
    public Set<Map.Entry<String,String>> entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }


    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }


    @Override
    public Collection<String> values() {
        return Collections.unmodifiableCollection(super.values());
    }


    @Override
    public boolean containsKey(Object key) {
        return (request.getHeader(key.toString()) != null);
    }


    @Override
    public boolean equals(Object obj) {
        return !(obj == null ||
                 !(obj.getClass()
                   == ExternalContext
                       .theUnmodifiableMapClass)) && super.equals(obj);
    }


    @Override
    public int hashCode() {
        int hashCode = 7 * request.hashCode();
        for (Iterator i = entrySet().iterator(); i.hasNext(); ) {
            hashCode += i.next().hashCode();
        }
        return hashCode;
    }

    
 // --------------------------------------------- Methods from BaseContextMap


    protected Iterator<Map.Entry<String,String>> getEntryIterator() {
        return new EntryIterator(new HeaderArrayEnumerator(request.headers().entries()));
    }


    protected Iterator<String> getKeyIterator() {
        return new KeyIterator(new HeaderArrayEnumerator(request.headers().entries()));
    }


    protected Iterator<String> getValueIterator() {
        return new ValueIterator(new HeaderArrayEnumerator(request.headers().entries()));
    }
    
    private static class HeaderArrayEnumerator implements Enumeration {

    	List<Map.Entry<String, String>> entries;
        int curIndex = -1;
        int upperBound;

        public HeaderArrayEnumerator(List<Map.Entry<String, String>> entries) {
        	this.entries = entries;
            upperBound = ((this.entries != null) ? this.entries.size() : -1);
        }

        public boolean hasMoreElements() {
            return (curIndex + 2 <= upperBound);
        }

        public Object nextElement() {
            curIndex++;
            if (curIndex < upperBound) {
                return entries.get(curIndex).getKey();
            } else {
                throw new NoSuchElementException();
            }
        }

    }
}

