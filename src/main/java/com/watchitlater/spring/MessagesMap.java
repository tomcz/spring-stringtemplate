package com.watchitlater.spring;

import org.springframework.web.servlet.support.RequestContext;

import java.util.Collections;
import java.util.List;

public class MessagesMap extends DataMap {

    protected final RequestContext context;

    public MessagesMap(RequestContext context) {
        this.context = context;
    }

    public boolean containsKey(Object key) {
        return true;
    }

    public Object get(Object key) {
        if (key instanceof List) {
            List list = (List) key;
            switch (list.size()) {
                case 0:
                    return "";
                case 1:
                    return get(list.get(0));
                default:
                    return getMessage(list);
            }
        }
        return getMessage(key.toString());
    }

    protected String getMessage(List list) {
        String code = list.get(0).toString();
        List args = list.subList(1, list.size());
        String defaultMessage = defaultMessage(code);
        return getMessage(code, args, defaultMessage);
    }

    protected String getMessage(String code) {
        List args = Collections.emptyList();
        String defaultMessage = defaultMessage(code);
        return getMessage(code, args, defaultMessage);
    }

    protected String defaultMessage(String code) {
        return "?" + code + "?";
    }

    protected String getMessage(String code, List args, String defaultMessage) {
        return context.getMessage(code, args, defaultMessage);
    }
}
