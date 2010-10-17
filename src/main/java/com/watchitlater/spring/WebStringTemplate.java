package com.watchitlater.spring;

import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.NoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;

import java.io.IOException;
import java.io.Writer;

public class WebStringTemplate extends StringTemplate {

    private WebAttributeRenderer defaultRenderer = new WebAttributeRenderer();

    public void setDefaultFormat(WebFormat format) {
        defaultRenderer.setDefaultFormat(format);
    }

    public void setEncoder(WebFormat format, Encoder encoder) {
        defaultRenderer.setEncoder(format, encoder);
    }

    public AttributeRenderer getAttributeRenderer(Class aClass) {
        AttributeRenderer renderer = super.getAttributeRenderer(aClass);
        return (renderer != null) ? renderer : defaultRenderer;
    }

    public void register(Renderer renderer) {
        registerRenderer(renderer.getTypeToRender(), renderer);
    }

    public void write(Writer out, boolean indent) throws IOException {
        if (indent) {
            write(new AutoIndentWriter(out));
        } else {
            write(new NoIndentWriter(out));
        }
    }
}
