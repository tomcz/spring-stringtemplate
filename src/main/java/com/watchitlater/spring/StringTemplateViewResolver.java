package com.watchitlater.spring;

import org.apache.commons.lang.SystemUtils;
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

    protected String sourceFileCharEncoding = SystemUtils.FILE_ENCODING;
    protected List<Renderer> renderers = Collections.emptyList();
    protected String contentType = "text/html;charset=UTF-8";
    protected WebFormat defaultFormat = WebFormat.html;
    protected boolean exposeBindStatus = true;
    protected String prefix = "";
    protected int order = 1;

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setSourceFileCharEncoding(String sourceFileCharEncoding) {
        this.sourceFileCharEncoding = sourceFileCharEncoding;
    }

    public void setExposeBindStatus(boolean exposeBindStatus) {
        this.exposeBindStatus = exposeBindStatus;
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

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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
            setupView(tempate, view);
            return view;

        } catch (TemplateNotFoundException e) {
            return null;
        }
    }

    protected StringTemplateView createView() {
        return new StringTemplateView();
    }

    protected void setupView(WebStringTemplate tempate, StringTemplateView view) {
        view.setExposeBindStatus(exposeBindStatus);
        view.setServletContext(servletContext);
        view.setContentType(contentType);
        view.setTemplate(tempate);
    }

    protected WebStringTemplate createTemplate(String viewName) {
        WebStringTemplate template = createGroup().createTemplate(viewName);
        template.setDefaultFormat(defaultFormat);
        registerAttributeRenderers(template);
        return template;
    }

    protected WebStringTemplateGroup createGroup() {
        WebStringTemplateGroup group = new WebStringTemplateGroup(resourceLoader, prefix);
        group.setFileCharEncoding(sourceFileCharEncoding);
        return group;
    }

    protected void registerAttributeRenderers(WebStringTemplate template) {
        for (Renderer renderer : renderers) {
            template.register(renderer);
        }
    }
}
