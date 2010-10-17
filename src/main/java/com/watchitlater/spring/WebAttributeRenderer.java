package com.watchitlater.spring;

import org.antlr.stringtemplate.AttributeRenderer;
import org.apache.commons.lang.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class WebAttributeRenderer implements AttributeRenderer {

    private WebFormat defaultFormat;
    private Map<WebFormat, Encoder> encoders;

    public WebAttributeRenderer() {
        this.encoders = new HashMap<WebFormat, Encoder>();
        this.encoders.put(WebFormat.html, new HtmlEncoder());
        this.encoders.put(WebFormat.xml, new XmlEncoder());
        this.encoders.put(WebFormat.js, new JavaScriptEncoder());
        this.encoders.put(WebFormat.url, new Utf8UrlEncoder());
        this.encoders.put(WebFormat.none, new NoneEncoder());
        this.defaultFormat = WebFormat.html;
    }

    public void setDefaultFormat(WebFormat defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    public void setEncoder(WebFormat format, Encoder encoder) {
        encoders.put(format, encoder);
    }

    public String toString(Object obj) {
        return render(obj, defaultFormat);
    }

    public String toString(Object obj, String formatName) {
        return render(obj, WebFormat.fromName(formatName));
    }

    private String render(Object obj, WebFormat format) {
        return encoders.get(format).encode(ObjectUtils.toString(obj));
    }
}
