package com.watchitlater.spring;

import org.antlr.stringtemplate.AttributeRenderer;

public interface Renderer extends AttributeRenderer {
    Class getTypeToRender();
}
