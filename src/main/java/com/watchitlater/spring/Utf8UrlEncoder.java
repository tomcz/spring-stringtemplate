package com.watchitlater.spring;

import org.apache.commons.lang.UnhandledException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Utf8UrlEncoder implements Encoder {

    public String encode(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnhandledException(e);
        }
    }
}
