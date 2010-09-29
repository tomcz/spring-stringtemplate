package com.watchitlater.spring;

import org.springframework.web.servlet.support.RequestContext;

public class BindStatusMap extends CachingDataMap {

    private final RequestContext context;

    public BindStatusMap(RequestContext context) {
        this.context = context;
    }

    @Override
    protected Object getValue(Object key) {
        return context.getBindStatus((String) key);
    }
}
