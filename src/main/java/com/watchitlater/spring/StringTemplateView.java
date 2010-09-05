package com.watchitlater.spring;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class StringTemplateView implements View {

    public static final String BASE_PATH = "base";

    public static final String SERVLET_PATH = "servlet";

    public static final String MODEL_KEY = "model";

    public static final String PARAMS_KEY = "params";

    public static final String REQUEST_KEY = "request";

    public static final String SESSION_KEY = "session";

    public static final String APPLICATION_KEY = "application";

    public static final String BIND_STATUS_KEY = "bindStatus";

    private final UrlPathHelper pathHelper = new UrlPathHelper();

    private final ServletContext servletContext;
    private final WebStringTemplate template;
    private final boolean exposeBindStatus;
    private final String contentType;

    public StringTemplateView(WebStringTemplate template, ServletContext servletContext,
                              boolean exposeBindStatus, String contentType) {

        this.exposeBindStatus = exposeBindStatus;
        this.servletContext = servletContext;
        this.contentType = contentType;
        this.template = template;
    }

    public String getContentType() {
        return contentType;
    }

    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        exposeModel(model);
        exposeRequest(request);
        exposeBindStatus(request, response, model);

        response.setContentType(getContentType());
        template.write(response.getWriter());
    }

    private void exposeModel(Map<String, ?> model) {
        template.setAttribute(MODEL_KEY, model);

        for (Map.Entry<String, ?> entry : model.entrySet()) {
            if (!entry.getKey().contains(".")) {
                template.setAttribute(entry.getKey(), entry.getValue());
            }
        }
    }

    private void exposeRequest(HttpServletRequest request) {
        template.setAttribute(PARAMS_KEY, getRequestParameters(request));
        template.setAttribute(REQUEST_KEY, getRequestAttributes(request));
        template.setAttribute(SESSION_KEY, getSessionAttributes(request));
        template.setAttribute(APPLICATION_KEY, getApplicationAttributes());

        String contextPath = pathHelper.getContextPath(request);
        String servletPath = pathHelper.getServletPath(request);

        template.setAttribute(SERVLET_PATH, contextPath + servletPath);
        template.setAttribute(BASE_PATH, contextPath);
    }

    @SuppressWarnings({"unchecked"})
    private void exposeBindStatus(HttpServletRequest request, HttpServletResponse response, Map model) {
        if (exposeBindStatus) {
            RequestContext requestContext = new RequestContext(request, response, servletContext, model);
            template.setAttribute(BIND_STATUS_KEY, new BindStatusMap(requestContext));
        }
    }

    private Map<String, Object> getRequestParameters(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        for (Enumeration names = request.getParameterNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();
            String[] values = request.getParameterValues(name);
            if (values.length == 1) {
                params.put(name, values[0]);
            } else {
                params.put(name, values);
            }
        }
        return params;
    }

    private Map<String, Object> getRequestAttributes(HttpServletRequest request) {
        Map<String, Object> attributes = new HashMap<String, Object>();
        for (Enumeration names = request.getAttributeNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();
            attributes.put(name, request.getAttribute(name));
        }
        return attributes;
    }

    private Map<String, Object> getSessionAttributes(HttpServletRequest request) {
        Map<String, Object> attributes = new HashMap<String, Object>();
        HttpSession session = request.getSession(false);
        if (session != null) {
            for (Enumeration names = session.getAttributeNames(); names.hasMoreElements();) {
                String name = (String) names.nextElement();
                attributes.put(name, session.getAttribute(name));
            }
        }
        return attributes;
    }

    public Map<String, Object> getApplicationAttributes() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        for (Enumeration names = servletContext.getAttributeNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();
            attributes.put(name, servletContext.getAttribute(name));
        }
        return attributes;
    }
}
