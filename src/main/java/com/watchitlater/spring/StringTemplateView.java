package com.watchitlater.spring;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked"})
public class StringTemplateView implements View {

    public static final String BASE_PATH = "base";

    public static final String SERVLET_PATH = "servlet";

    public static final String MODEL_KEY = "model";

    public static final String PARAMS_KEY = "params";

    public static final String REQUEST_KEY = "request";

    public static final String SESSION_KEY = "session";

    public static final String APPLICATION_KEY = "application";

    public static final String THEME_MESSAGES_KEY = "themeMessages";

    public static final String BIND_STATUS_KEY = "bindStatus";

    public static final String MESSAGES_KEY = "messages";

    private final UrlPathHelper pathHelper = new UrlPathHelper();

    protected ServletContext servletContext;
    protected boolean exposeRequestContext;

    protected WebStringTemplate template;
    protected String contentType;
    protected boolean autoIndent;

    public void setTemplate(WebStringTemplate template) {
        this.template = template;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setExposeRequestContext(boolean exposeRequestContext) {
        this.exposeRequestContext = exposeRequestContext;
    }

    public void setAutoIndent(boolean autoIndent) {
        this.autoIndent = autoIndent;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        exposeModel(model);
        exposeRequest(request);
        exposeRequestContext(request, response, model);

        response.setContentType(getContentType());

        template.write(response.getWriter(), autoIndent);
    }

    protected void exposeModel(Map<String, ?> model) {
        template.setAttribute(MODEL_KEY, model);

        for (Map.Entry<String, ?> entry : model.entrySet()) {
            if (!entry.getKey().contains(".")) {
                template.setAttribute(entry.getKey(), entry.getValue());
            }
        }
    }

    protected void exposeRequest(HttpServletRequest request) {
        template.setAttribute(PARAMS_KEY, getRequestParameters(request));
        template.setAttribute(REQUEST_KEY, getRequestAttributes(request));
        template.setAttribute(SESSION_KEY, getSessionAttributes(request));
        template.setAttribute(APPLICATION_KEY, getApplicationAttributes());

        String contextPath = pathHelper.getContextPath(request);
        String servletPath = pathHelper.getServletPath(request);

        template.setAttribute(SERVLET_PATH, contextPath + servletPath);
        template.setAttribute(BASE_PATH, contextPath);
    }

    protected void exposeRequestContext(HttpServletRequest request, HttpServletResponse response, Map model) {
        if (exposeRequestContext) {
            RequestContext requestContext = new RequestContext(request, response, servletContext, model);
            template.setAttribute(THEME_MESSAGES_KEY, new ThemeMessagesMap(requestContext));
            template.setAttribute(BIND_STATUS_KEY, new BindStatusMap(requestContext));
            template.setAttribute(MESSAGES_KEY, new MessagesMap(requestContext));
        }
    }

    protected Map<String, Object> getRequestParameters(HttpServletRequest request) {
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

    protected Map<String, Object> getRequestAttributes(HttpServletRequest request) {
        Map<String, Object> attributes = new HashMap<String, Object>();
        for (Enumeration names = request.getAttributeNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();
            attributes.put(name, request.getAttribute(name));
        }
        return attributes;
    }

    protected Map<String, Object> getSessionAttributes(HttpServletRequest request) {
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

    protected Map<String, Object> getApplicationAttributes() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        for (Enumeration names = servletContext.getAttributeNames(); names.hasMoreElements();) {
            String name = (String) names.nextElement();
            attributes.put(name, servletContext.getAttribute(name));
        }
        return attributes;
    }
}
