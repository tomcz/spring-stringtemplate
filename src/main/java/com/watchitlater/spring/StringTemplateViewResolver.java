package com.watchitlater.spring;

import org.antlr.stringtemplate.StringTemplateErrorListener;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.Ordered;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StringTemplateViewResolver implements ViewResolver, ResourceLoaderAware, ServletContextAware, Ordered {

    // WebStringTemplateGroup

    protected Map<String, WebStringTemplateGroup> groupCache = new ConcurrentHashMap<String, WebStringTemplateGroup>();
    protected Integer refreshIntervalInSeconds;
    protected boolean useGroupCache = false;

    protected StringTemplateErrorListener templateErrorListener;

    protected ResourceLoader resourceLoader;
    protected String sourceFileCharEncoding;
    protected String templateRoot = "";
    protected String sharedRoot;

    // WebStringTemplate

    protected List<Renderer> renderers = Collections.emptyList();
    protected WebFormat defaultFormat = WebFormat.html;

    // StringTemplateView

    protected ServletContext servletContext;
    protected String contentType = "text/html;charset=UTF-8";
    protected boolean exposeRequestContext = true;
    protected boolean autoIndent = true;

    // Ordered

    protected int order = 1;

    public void setUseGroupCache(boolean useGroupCache) {
        this.useGroupCache = useGroupCache;
    }

    public void setTemplateErrorListener(StringTemplateErrorListener templateErrorListener) {
        this.templateErrorListener = templateErrorListener;
    }

    public void setRefreshIntervalInSeconds(Integer refreshIntervalInSeconds) {
        this.refreshIntervalInSeconds = refreshIntervalInSeconds;
    }

    public void setSourceFileCharEncoding(String sourceFileCharEncoding) {
        this.sourceFileCharEncoding = sourceFileCharEncoding;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setTemplateRoot(String templateRoot) {
        this.templateRoot = templateRoot;
    }

    public void setSharedRoot(String sharedRoot) {
        this.sharedRoot = sharedRoot;
    }

    public void setRenderers(List<Renderer> renderers) {
        this.renderers = renderers;
    }

    public void setDefaultFormat(String defaultFormat) {
        this.defaultFormat = WebFormat.fromName(defaultFormat);
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setExposeRequestContext(boolean exposeRequestContext) {
        this.exposeRequestContext = exposeRequestContext;
    }

    public void setAutoIndent(boolean autoIndent) {
        this.autoIndent = autoIndent;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public StringTemplateView resolveViewName(String viewName, Locale locale) {
        if (shouldNotResolve(viewName)) {
            return null;
        }
        try {
            WebStringTemplate tempate = createTemplate(viewName);
            StringTemplateView view = createView();
            initView(view, tempate);
            return view;

        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    protected boolean shouldNotResolve(String viewName) {
        return StringUtils.isBlank(viewName)
                || viewName.startsWith(UrlBasedViewResolver.FORWARD_URL_PREFIX)
                || viewName.startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX);
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
        WebStringTemplate template = createGroup(viewName).createTemplate(viewName);
        template.setDefaultFormat(defaultFormat);
        registerAttributeRenderers(template);
        return template;
    }

    protected void registerAttributeRenderers(WebStringTemplate template) {
        for (Renderer renderer : renderers) {
            template.register(renderer);
        }
    }

    protected WebStringTemplateGroup createGroup(String viewName) {
        return useGroupCache ? getCachedGroup(viewName) : createGroup();
    }

    protected WebStringTemplateGroup getCachedGroup(String viewName) {
        WebStringTemplateGroup group = groupCache.get(viewName);
        if (group == null) {
            group = createGroup();
            groupCache.put(viewName, group);
        }
        return group;
    }

    protected WebStringTemplateGroup createGroup() {
        WebStringTemplateGroup group = createGroup("main", templateRoot);
        if (sharedRoot != null) {
            WebStringTemplateGroup shared = createGroup("shared", sharedRoot);
            group.setSuperGroup(shared);
        }
        return group;
    }

    protected WebStringTemplateGroup createGroup(String groupName, String groupRoot) {
        WebStringTemplateGroup group = new WebStringTemplateGroup(groupName, groupRoot, resourceLoader);
        initGroup(group);
        return group;
    }

    protected void initGroup(WebStringTemplateGroup group) {
        if (refreshIntervalInSeconds != null) {
            group.setRefreshInterval(refreshIntervalInSeconds);
        }
        if (sourceFileCharEncoding != null) {
            group.setFileCharEncoding(sourceFileCharEncoding);
        }
        if (templateErrorListener != null) {
            group.setErrorListener(templateErrorListener);
        }
    }
}
