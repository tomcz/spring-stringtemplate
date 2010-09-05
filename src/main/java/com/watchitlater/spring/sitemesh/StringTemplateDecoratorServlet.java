package com.watchitlater.spring.sitemesh;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.RequestConstants;
import com.watchitlater.spring.StringTemplateView;
import com.watchitlater.spring.StringTemplateViewResolver;
import org.apache.commons.lang.StringUtils;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StringTemplateDecoratorServlet extends HttpServlet {

    private final UrlPathHelper pathHelper = new UrlPathHelper();

    private StringTemplateViewResolver resolver;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ServletContext ctx = config.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(ctx);

        resolver = new StringTemplateViewResolver();
        resolver.setExposeBindStatus(false);
        resolver.setResourceLoader(wac);
        resolver.setServletContext(ctx);

        String encoding = config.getInitParameter("sourceFileCharEncoding");
        if (encoding != null) {
            resolver.setSourceFileCharEncoding(encoding);
        }

        String contentType = config.getInitParameter("contentType");
        if (contentType != null) {
            resolver.setContentType(contentType);
        }

        String prefix = config.getInitParameter("prefix");
        if (prefix != null) {
            resolver.setPrefix(prefix);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String lookupPath = pathHelper.getLookupPathForRequest(request);
        StringTemplateView template = resolver.resolveViewName(viewName(lookupPath), Locale.getDefault());
        if (template != null) {
            template.render(pageModel(request), request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private String viewName(String lookupPath) {
        return StringUtils.substringBeforeLast(lookupPath, ".st");
    }

    private Map<String, ?> pageModel(HttpServletRequest request) {
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
}
