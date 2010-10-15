package com.watchitlater.spring;

import org.springframework.web.servlet.support.RequestContext;

import java.util.List;

public class ThemeMessagesMap extends MessagesMap {

    public ThemeMessagesMap(RequestContext context) {
        super(context);
    }

    @Override
    protected String getMessage(String code, List args, String defaultMessage) {
        return context.getThemeMessage(code, args, defaultMessage);
    }
}
