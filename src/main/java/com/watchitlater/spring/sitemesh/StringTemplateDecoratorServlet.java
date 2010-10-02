package com.watchitlater.spring.sitemesh;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.RequestConstants;
import com.watchitlater.spring.StringTemplateView;
import com.watchitlater.spring.StringTemplateViewResolver;
import org.antlr.stringtemplate.StringTemplateErrorListener;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StringTemplateDecoratorServlet extends HttpServlet {

    protected final UrlPathHelper pathHelper = new UrlPathHelper();

    protected StringTemplateViewResolver resolver;

    @Override
    public void init(ServletConfig config) throws ServletException {
        resolver = createResolver(config);
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringTemplateView template = resolveTemplate(request);
        if (template != null) {
            render(template, request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected StringTemplateView resolveTemplate(HttpServletRequest request) {
        return resolver.resolveViewName(viewName(request), getLocale(request));
    }

    protected String viewName(HttpServletRequest request) {
        String lookupPath = pathHelper.getLookupPathForRequest(request);
        return StringUtils.substringBeforeLast(lookupPath, ".st");
    }

    protected Locale getLocale(HttpServletRequest request) {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver != null) {
            return localeResolver.resolveLocale(request);
        } else {
            return request.getLocale();
        }
    }

    protected void render(StringTemplateView template, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            template.render(pageModel(request), request, response);

        } catch (IOException e) {
            throw e; // no need to wrap

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Map<String, ?> pageModel(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        HTMLPage page = (HTMLPage) request.getAttribute(RequestConstants.PAGE);
        if (page != null) {
            model.put("page", page);
            model.put("head", StringUtils.trim(page.getHead()));
            model.put("body", StringUtils.trim(page.getBody()));
            model.put("title", StringUtils.trim(page.getTitle()));
        } else {
            model.put("page", new HashMap());
            model.put("title", "No Title");
            model.put("body", "No Body");
            model.put("head", "");
        }
        return model;
    }

    protected StringTemplateViewResolver createResolver(ServletConfig config) {
        StringTemplateViewResolver viewResolver = createResolver();
        initParameters(viewResolver, config);
        initContexts(viewResolver, config);
        return viewResolver;
    }

    protected StringTemplateViewResolver createResolver() {
        return new StringTemplateViewResolver();
    }

    protected void initParameters(StringTemplateViewResolver viewResolver, ServletConfig config) {
        BeanWrapper wrapper = new BeanWrapperImpl(viewResolver);
        Enumeration names = config.getInitParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if (wrapper.isWritableProperty(name)) {
                String paramValue = config.getInitParameter(name);
                wrapper.setPropertyValue(name, paramValue);
            }
        }
    }

    protected void initContexts(StringTemplateViewResolver viewResolver, ServletConfig config) {
        ServletContext ctx = config.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(ctx);
        StringTemplateErrorListener listener = findTemplateErrorListener(wac);

        viewResolver.setTemplateErrorListener(listener);
        viewResolver.setResourceLoader(wac);
        viewResolver.setServletContext(ctx);
    }

    protected StringTemplateErrorListener findTemplateErrorListener(WebApplicationContext wac) {
        try {
            return wac.getBean(StringTemplateErrorListener.class);
        } catch (BeansException e) {
            return null;
        }
    }
}
