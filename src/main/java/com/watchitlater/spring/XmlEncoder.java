package com.watchitlater.spring;

import org.apache.commons.lang.StringEscapeUtils;

public class XmlEncoder implements Encoder {

    public String encode(String text) {
        return StringEscapeUtils.escapeXml(text);
    }
}
