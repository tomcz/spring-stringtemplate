package com.watchitlater.spring;

import org.springframework.web.servlet.support.RequestContext;

import java.util.LinkedList;
import java.util.List;

public class MessageMap extends DataMap {

    public static final String TO_STRING_KEY = "to_s";

    private final RequestContext context;
    private final String defaultMessage;
    private final String messageCode;

    private List<Object> args;

    public MessageMap(RequestContext context, String messageCode) {
        this.defaultMessage = "?" + messageCode + "?";
        this.messageCode = messageCode;
        this.context = context;
    }

    public boolean containsKey(Object key) {
        return true;
    }

    public Object get(Object key) {
        if (TO_STRING_KEY.equals(key)) {
            return getMessage();
        } else {
            return with(key);
        }
    }

    protected String getMessage() {
        return context.getMessage(messageCode, args, defaultMessage);
    }

    protected Object with(Object key) {
        if (args == null) {
            args = new LinkedList<Object>();
        }
        args.add(key);
        return this;
    }
}
