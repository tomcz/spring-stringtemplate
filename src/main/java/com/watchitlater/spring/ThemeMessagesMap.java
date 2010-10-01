package com.watchitlater.spring;

import org.springframework.web.servlet.support.RequestContext;

public class ThemeMessagesMap extends MessagesMap {

    public ThemeMessagesMap(RequestContext context) {
        super(context);
    }

    public Object get(Object key) {
        return new ThemeMessageMap(context, (String) key);
    }
}
