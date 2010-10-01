package com.watchitlater.spring;

import org.springframework.web.servlet.support.RequestContext;

public class ThemeMessageMap extends MessageMap {

    public ThemeMessageMap(RequestContext context, String messageCode) {
        super(context, messageCode);
    }

    @Override
    protected String getMessage() {
        return context.getThemeMessage(messageCode, args, defaultMessage);
    }
}
