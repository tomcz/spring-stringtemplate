package com.watchitlater.spring;

public class NoneEncoder implements Encoder {

    public String encode(String text) {
        return text;
    }
}
