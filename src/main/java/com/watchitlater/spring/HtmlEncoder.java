package com.watchitlater.spring;

import org.springframework.web.util.HtmlUtils;

public class HtmlEncoder implements Encoder {

    public String encode(String text) {
        return HtmlUtils.htmlEscape(text);
    }
}
