package com.watchitlater.spring;

import org.springframework.web.servlet.support.RequestContext;

public class MessagesMap extends DataMap {

    private final RequestContext context;

    public MessagesMap(RequestContext context) {
        this.context = context;
    }

    public Object get(Object key) {
        return new MessageMap(context, (String) key);
    }

    public boolean containsKey(Object key) {
        return true;
    }
}
