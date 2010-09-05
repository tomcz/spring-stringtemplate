package com.watchitlater.spring;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.UnhandledException;
import org.springframework.web.util.HtmlUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public enum WebFormat {

    html, xml, js, url, none;

    public static WebFormat fromName(String name) {
        try {
            return WebFormat.valueOf(name);
        } catch (IllegalArgumentException e) {
            return WebFormat.none;
        }
    }

    public String format(Object obj) {
        String text = ObjectUtils.toString(obj);
        switch (this) {
            case html:
                return HtmlUtils.htmlEscape(text);
            case xml:
                return StringEscapeUtils.escapeXml(text);
            case js:
                return StringEscapeUtils.escapeJavaScript(text);
            case url:
                return encodeURL(text);
            default:
                return text.trim();
        }
    }

    private String encodeURL(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnhandledException(e);
        }
    }
}
