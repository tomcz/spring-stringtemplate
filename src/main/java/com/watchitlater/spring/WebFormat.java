package com.watchitlater.spring;

public enum WebFormat {

    html, xml, js, url, none;

    public static WebFormat fromName(String name) {
        try {
            return WebFormat.valueOf(name);
        } catch (IllegalArgumentException e) {
            return WebFormat.none;
        }
    }
}
