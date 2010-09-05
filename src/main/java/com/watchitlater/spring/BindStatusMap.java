package com.watchitlater.spring;

import org.springframework.web.servlet.support.BindStatus;
import org.springframework.web.servlet.support.RequestContext;

import java.util.HashMap;
import java.util.Map;

public class BindStatusMap extends DataMap {

    private final Map<String, BindStatus> cache;
    private final RequestContext context;

    public BindStatusMap(RequestContext context) {
        this.cache = new HashMap<String, BindStatus>();
        this.context = context;
    }

    public Object get(Object key) {
        return getBindStatus((String) key);
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    private Object getBindStatus(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }
        BindStatus status = context.getBindStatus(path);
        cache.put(path, status);
        return status;
    }
}
