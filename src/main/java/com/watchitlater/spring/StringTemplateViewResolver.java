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

    private ResourceLoader resourceLoader;
    private ServletContext servletContext;

    private String sourceFileCharEncoding = SystemUtils.FILE_ENCODING;
    private List<Renderer> renderers = Collections.emptyList();
    private String contentType = "text/html;charset=UTF-8";
    private WebFormat defaultFormat = WebFormat.html;
    private boolean exposeBindStatus = true;
    private String prefix = "";
    private int order = 1;

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
            return new StringTemplateView(tempate, servletContext, exposeBindStatus, contentType);

        } catch (IllegalArgumentException e) {
            return null; // template cannot be found
        }
    }

    private WebStringTemplate createTemplate(String viewName) {
        WebStringTemplate template = createGroup().createTemplate(viewName);
        template.setDefaultFormat(defaultFormat);
        registerAttributeRenderers(template);
        return template;
    }

    private WebStringTemplateGroup createGroup() {
        WebStringTemplateGroup group = new WebStringTemplateGroup(resourceLoader, prefix);
        group.setFileCharEncoding(sourceFileCharEncoding);
        return group;
    }

    private void registerAttributeRenderers(WebStringTemplate template) {
        for (Renderer renderer : renderers) {
            template.register(renderer);
        }
    }
}
