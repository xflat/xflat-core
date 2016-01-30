package cn.xflat.core.context;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import cn.xflat.context.ExternalContext;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;


public class XWebContext extends ExternalContext {

	private RoutingContext routingCtx = null;
	private HttpServerRequest request;
	
	private Map<String,Object> applicationMap = null;
    private Map<String,Object> sessionMap = null;
    private Map<String,Object> requestMap = null;
    private Map<String,String> requestParameterMap = null;
    private Map<String,String[]> requestParameterValuesMap = null;
    private Map<String,String> requestHeaderMap = null;
    private Map<String,String[]> requestHeaderValuesMap = null;
    private Map<String,Object> cookieMap = null;
    private Map<String,String> initParameterMap = null;
    private Map<String,String> fallbackContentTypeMap = null;
    
    private enum ALLOWABLE_COOKIE_PROPERTIES {
        domain,
        maxAge,
        path,
        secure
    }
    
    static final Class theUnmodifiableMapClass =
            Collections.unmodifiableMap(new HashMap<Object,Object>()).getClass();
    
	public XWebContext(RoutingContext routingCtx) {
		
		this.routingCtx = routingCtx;
		this.request = routingCtx.request();
		
		 getRequestMap();
	     getSessionMap();
	     getApplicationMap();
	     
	     fallbackContentTypeMap = new HashMap<String,String>(3, 1.0f);
	     fallbackContentTypeMap.put("js", "text/javascript");
	     fallbackContentTypeMap.put("css", "text/css");
	     fallbackContentTypeMap.put("groovy", "application/x-groovy");
	     fallbackContentTypeMap.put("properties", "text/plain");
	}
	
	
	public Object getSession(boolean create) {
		return routingCtx.session();
    }

	/*
    public Object getContext() {
        return this.servletContext;
    }

    public String getContextName() {

        if (servletContext.getMajorVersion() >= 3
            || (servletContext.getMajorVersion() == 2
                && servletContext.getMinorVersion() == 5)) {
            return this.servletContext.getServletContextName();
        } else {
            // for servlet 2.4 support
            return servletContext.getServletContextName();
        }

    }*/

    public Object getRequest() {
        return routingCtx.request();
    }


    public void setRequest(Object request) {
        if (request instanceof HttpServerRequest) {
            this.request = (HttpServerRequest) request;
            requestHeaderMap = null;
            requestHeaderValuesMap = null;
            requestHeaderValuesMap = null;
            requestMap = null;
            requestParameterMap = null;
            requestParameterValuesMap = null;
        }
    }

    @Override
    public Map<String,Object> getApplicationMap() {
        if (applicationMap == null) {
            applicationMap = new XApplicationMap(routingCtx.vertx().sharedData());
        }
        return applicationMap;
    }

    @Override
	public Map<String, Object> getSessionMap() {
    	if (sessionMap == null) {
    		sessionMap = new XSessionMap(routingCtx.session());
        }
        return sessionMap;
	}
    
	@Override
	public Map<String,Object> getRequestMap() {
        if (requestMap == null) {
            requestMap = new XRequestMap(routingCtx);
        }
        return requestMap;
    }

	@Override
	 public Map<String,String> getRequestHeaderMap() {
        if (null == requestHeaderMap) {
            requestHeaderMap =Collections.unmodifiableMap(new XRequestHeaderMap(request));
        }
        return requestHeaderMap;
    }
	
	@Override
	public Map<String,String[]> getRequestHeaderValuesMap() {
        if (null == requestHeaderValuesMap) {
            requestHeaderValuesMap = Collections.unmodifiableMap(new XRequestHeaderValuesMap(request));
        }
        return requestHeaderValuesMap;
    }
	
	@Override
	public Map<String,Object> getRequestCookieMap() {
        if (null == cookieMap) {
            cookieMap =Collections.unmodifiableMap(new XRequestCookieMap(routingCtx));
        }
        return cookieMap;
    }
	
	public Map<String,String> getInitParameterMap() {
        if (null == initParameterMap) {
            initParameterMap = Collections.unmodifiableMap(new XInitParameterMap(routingCtx));
        }
        return initParameterMap;
    }


    public Map<String,String> getRequestParameterMap() {
        if (null == requestParameterMap) {
            requestParameterMap =
                Collections.unmodifiableMap(new XRequestParameterMap(request));
        }
        return requestParameterMap;
    }

    public Map<String,String[]> getRequestParameterValuesMap() {
        if (null == requestParameterValuesMap) {
            requestParameterValuesMap =
                Collections.unmodifiableMap(
                    new XRequestParameterValuesMap(request));
        }
        return requestParameterValuesMap;
    }

    public Iterator<String> getRequestParameterNames() {
    	
    	MultiMap params = request.params();
    	
    	final Iterator<Map.Entry<String, String>> it = params.iterator();
    	
        return new Iterator<String>() {
            public boolean hasNext() {
                return it.hasNext();
            }


            public String next() {
                return (String) it.next().getKey();
            }


            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    
	@Override
	public void dispatch(String path) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String encodeActionURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeNamespace(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeResourceURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInitParameter(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String getRemoteUser() {
	
		return null;
	}

	

	@Override
	public String getRequestContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locale getRequestLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Locale> getRequestLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public String getRequestPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserInRole(String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void log(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log(String message, Throwable exception) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redirect(String url) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
