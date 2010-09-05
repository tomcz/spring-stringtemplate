package com.watchitlater.spring;

public interface Renderer {

    Class getTypeToRender();

    public String toString(Object obj);

    public String toString(Object obj, String formatName);
}
