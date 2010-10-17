package com.watchitlater.spring;

import org.apache.commons.lang.StringEscapeUtils;

public class JavaScriptEncoder implements Encoder {

    public String encode(String text) {
        return StringEscapeUtils.escapeJavaScript(text);
    }
}
