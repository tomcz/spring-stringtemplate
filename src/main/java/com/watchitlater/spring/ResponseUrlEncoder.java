package com.watchitlater.spring;

import javax.servlet.http.HttpServletResponse;

public class ResponseUrlEncoder implements Encoder {

    private final HttpServletResponse response;

    public ResponseUrlEncoder(HttpServletResponse response) {
        this.response = response;
    }

    public String encode(String text) {
        return response.encodeURL(text);
    }
}
