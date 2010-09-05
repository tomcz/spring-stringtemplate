package com.watchitlater.spring;

import org.antlr.stringtemplate.AttributeRenderer;

public class RendererAdaptor implements AttributeRenderer {

    private final Renderer delegate;

    public RendererAdaptor(Renderer delegate) {
        this.delegate = delegate;
    }

    public String toString(Object obj) {
        return delegate.toString(obj);
    }

    public String toString(Object obj, String formatName) {
        return delegate.toString(obj, formatName);
    }
}
