package com.watchitlater.spring.sitemesh;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.RequestConstants;
import com.watchitlater.spring.StringTemplateView;
import com.watchitlater.spring.StringTemplateViewResolver;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
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
        super.init(config);

        ServletContext ctx = config.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(ctx);

        resolver = createViewResolver(config);
        resolver.setExposeBindStatus(false);
        resolver.setResourceLoader(wac);
        resolver.setServletContext(ctx);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringTemplateView template = resolver.resolveViewName(viewName(request), getLocale(request));
        if (template != null) {
            template.render(pageModel(request), request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected String viewName(HttpServletRequest request) {
        String lookupPath = pathHelper.getLookupPathForRequest(request);
        return StringUtils.substringBeforeLast(lookupPath, ".st");
    }

    protected Locale getLocale(HttpServletRequest request) {
        return Locale.getDefault();
    }

    protected Map<String, ?> pageModel(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        HTMLPage page = (HTMLPage) request.getAttribute(RequestConstants.PAGE);
        if (page != null) {
            model.put("title", page.getTitle());
            model.put("head", page.getHead());
            model.put("body", page.getBody());
            model.put("page", page);
        } else {
            model.put("title", "No Title");
            model.put("body", "No Body");
            model.put("head", "");
        }
        return model;
    }

    protected StringTemplateViewResolver createViewResolver(ServletConfig config) {
        StringTemplateViewResolver viewResolver = new StringTemplateViewResolver();
        BeanWrapper wrapper = new BeanWrapperImpl(viewResolver);
        setInitParameters(config, wrapper);
        return viewResolver;
    }

    protected void setInitParameters(ServletConfig config, BeanWrapper wrapper) {
        Enumeration names = config.getInitParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if (wrapper.isWritableProperty(name)) {
                setInitParameter(name, config, wrapper);
            }
        }
    }

    protected void setInitParameter(String paramName, ServletConfig config, BeanWrapper bean) {
        String paramValue = config.getInitParameter(paramName);
        if (paramValue != null) {
            bean.setPropertyValue(paramName, paramValue);
        }
    }
}
