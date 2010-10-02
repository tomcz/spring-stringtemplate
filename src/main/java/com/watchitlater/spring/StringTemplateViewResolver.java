package com.watchitlater.spring;

import org.antlr.stringtemplate.StringTemplateErrorListener;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.Ordered;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class StringTemplateViewResolver implements ViewResolver, ResourceLoaderAware, ServletContextAware, Ordered {

    protected ResourceLoader resourceLoader;
    protected ServletContext servletContext;

    protected StringTemplateErrorListener templateErrorListener;
    protected String sourceFileCharEncoding;

    protected List<Renderer> renderers = Collections.emptyList();
    protected String contentType = "text/html;charset=UTF-8";
    protected WebFormat defaultFormat = WebFormat.html;
    protected boolean exposeRequestContext = true;
    protected boolean autoIndent = true;
    protected String templateRoot = "";
    protected String sharedRoot;

    protected int order = 1;

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setTemplateErrorListener(StringTemplateErrorListener templateErrorListener) {
        this.templateErrorListener = templateErrorListener;
    }

    public void setSourceFileCharEncoding(String sourceFileCharEncoding) {
        this.sourceFileCharEncoding = sourceFileCharEncoding;
    }

    public void setExposeRequestContext(boolean exposeRequestContext) {
        this.exposeRequestContext = exposeRequestContext;
    }

    public void setAutoIndent(boolean autoIndent) {
        this.autoIndent = autoIndent;
    }

    public void setDefaultFormat(String defaultFormat) {
        this.defaultFormat = WebFormat.fromName(defaultFormat);
    }

    public void setRenderers(List<Renderer> renderers) {
        this.renderers = renderers;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setTemplateRoot(String templateRoot) {
        this.templateRoot = templateRoot;
    }

    public void setSharedRoot(String sharedRoot) {
        this.sharedRoot = sharedRoot;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public StringTemplateView resolveViewName(String viewName, Locale locale) {
        try {
            WebStringTemplate tempate = createTemplate(viewName);
            StringTemplateView view = createView();
            initView(view, tempate);
            return view;

        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    protected StringTemplateView createView() {
        return new StringTemplateView();
    }

    protected void initView(StringTemplateView view, WebStringTemplate tempate) {
        view.setExposeRequestContext(exposeRequestContext);
        view.setServletContext(servletContext);
        view.setContentType(contentType);
        view.setAutoIndent(autoIndent);
        view.setTemplate(tempate);
    }

    protected WebStringTemplate createTemplate(String viewName) {
        WebStringTemplate template = createGroup().createTemplate(viewName);
        template.setDefaultFormat(defaultFormat);
        registerAttributeRenderers(template);
        return template;
    }

    protected void registerAttributeRenderers(WebStringTemplate template) {
        for (Renderer renderer : renderers) {
            template.register(renderer);
        }
    }

    protected WebStringTemplateGroup createGroup() {
        WebStringTemplateGroup group = new WebStringTemplateGroup("main", templateRoot, resourceLoader);
        if (sharedRoot != null) {
            WebStringTemplateGroup shared = new WebStringTemplateGroup("shared", sharedRoot, resourceLoader);
            group.setSuperGroup(shared);
            initGroup(shared);
        }
        initGroup(group);
        return group;
    }

    protected void initGroup(WebStringTemplateGroup group) {
        if (sourceFileCharEncoding != null) {
            group.setFileCharEncoding(sourceFileCharEncoding);
        }
        if (templateErrorListener != null) {
            group.setErrorListener(templateErrorListener);
        }
    }
}
