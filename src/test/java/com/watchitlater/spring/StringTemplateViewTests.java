package com.watchitlater.spring;

import org.junit.Test;
import org.mockito.InOrder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class StringTemplateViewTests {

    @Test
    public void shouldExposeModelAsTemplateAttribute() throws Exception {
        WebStringTemplate template = mock(WebStringTemplate.class);

        Map<String, String> model = Collections.singletonMap("test", "value");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletContext context = new MockServletContext();

        StringTemplateView view = createTemplate(template, context, false, "text/html");
        view.render(model, request, response);

        verify(template).setAttribute(StringTemplateView.MODEL_KEY, model);
    }

    @Test
    public void shouldExposeSimpleModelEntriesAsTemplateAttributes() throws Exception {
        WebStringTemplate template = mock(WebStringTemplate.class);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("complex.property", "complex");
        model.put("simpleProperty", "simple");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletContext context = new MockServletContext();

        StringTemplateView view = createTemplate(template, context, false, "text/html");
        view.render(model, request, response);

        verify(template).setAttribute("simpleProperty", "simple");
        verify(template, never()).setAttribute("complex.property", "complex");
    }

    @Test
    public void shouldExposeRequestParametersAsAMap() throws Exception {
        WebStringTemplate template = mock(WebStringTemplate.class);

        MockHttpServletResponse response = new MockHttpServletResponse();
        Map<String, Object> model = new HashMap<String, Object>();
        ServletContext context = new MockServletContext();

        String[] array = {"one", "two"};

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("test", "value");
        request.setParameter("array", array);

        StringTemplateView view = createTemplate(template, context, false, "text/html");
        view.render(model, request, response);

        verify(template).setAttribute(eq(StringTemplateView.PARAMS_KEY), argThat(hasEntry("test", "value")));
        verify(template).setAttribute(eq(StringTemplateView.PARAMS_KEY), argThat(hasEntry("array", array)));
    }

    @Test
    public void shouldExposeRequestAttributesAsAMap() throws Exception {
        WebStringTemplate template = mock(WebStringTemplate.class);

        MockHttpServletResponse response = new MockHttpServletResponse();
        Map<String, Object> model = new HashMap<String, Object>();
        ServletContext context = new MockServletContext();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("test", "value");

        StringTemplateView view = createTemplate(template, context, false, "text/html");
        view.render(model, request, response);

        verify(template).setAttribute(eq(StringTemplateView.REQUEST_KEY), argThat(hasEntry("test", "value")));
    }

    @Test
    public void shouldExposeEmptySessionAttributesWhenHttpSessionNotPresent() throws Exception {
        WebStringTemplate template = mock(WebStringTemplate.class);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, Object> model = new HashMap<String, Object>();
        ServletContext context = new MockServletContext();

        StringTemplateView view = createTemplate(template, context, false, "text/html");
        view.render(model, request, response);

        verify(template).setAttribute(StringTemplateView.SESSION_KEY, new HashMap());
    }

    @Test
    public void shouldExposeSessionAttributesAsMapWhenHttpSessionPresent() throws Exception {
        WebStringTemplate template = mock(WebStringTemplate.class);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("test", "value");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(session);

        MockHttpServletResponse response = new MockHttpServletResponse();
        Map<String, Object> model = new HashMap<String, Object>();
        ServletContext context = new MockServletContext();

        StringTemplateView view = createTemplate(template, context, false, "text/html");
        view.render(model, request, response);

        verify(template).setAttribute(eq(StringTemplateView.SESSION_KEY), argThat(hasEntry("test", "value")));
    }

    @Test
    public void shouldExposeApplicationAttributesAsAMap() throws Exception {
        WebStringTemplate template = mock(WebStringTemplate.class);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, Object> model = new HashMap<String, Object>();

        ServletContext context = new MockServletContext();
        context.setAttribute("test", "value");

        StringTemplateView view = createTemplate(template, context, false, "text/html");
        view.render(model, request, response);

        verify(template).setAttribute(eq(StringTemplateView.APPLICATION_KEY), argThat(hasEntry("test", "value")));
    }

    @Test
    public void shouldNotExposeRequestContextWhenConfiguredNotToExpose() throws Exception {
        WebStringTemplate template = mock(WebStringTemplate.class);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, Object> model = new HashMap<String, Object>();
        ServletContext context = new MockServletContext();

        boolean exposeRequestContext = false;

        StringTemplateView view = createTemplate(template, context, exposeRequestContext, "text/html");
        view.render(model, request, response);

        verify(template, never()).setAttribute(eq(StringTemplateView.THEME_MESSAGES_KEY), anyObject());
        verify(template, never()).setAttribute(eq(StringTemplateView.BIND_STATUS_KEY), anyObject());
        verify(template, never()).setAttribute(eq(StringTemplateView.MESSAGES_KEY), anyObject());
    }

    @Test
    public void shouldExposeRequestContextWhenConfiguredToExpose() throws Exception {
        WebApplicationContext wac = mock(WebApplicationContext.class);
        WebStringTemplate template = mock(WebStringTemplate.class);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, Object> model = new HashMap<String, Object>();

        MockServletContext context = new MockServletContext();
        context.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);

        boolean exposeRequestContext = true;

        StringTemplateView view = createTemplate(template, context, exposeRequestContext, "text/html");
        view.render(model, request, response);

        verify(template).setAttribute(eq(StringTemplateView.THEME_MESSAGES_KEY), isA(ThemeMessagesMap.class));
        verify(template).setAttribute(eq(StringTemplateView.BIND_STATUS_KEY), isA(BindStatusMap.class));
        verify(template).setAttribute(eq(StringTemplateView.MESSAGES_KEY), isA(MessagesMap.class));
    }

    @Test
    public void shouldSetBaseAndServletPathsAsTemplateAttributes() throws Exception {
        WebStringTemplate template = mock(WebStringTemplate.class);

        MockHttpServletResponse response = new MockHttpServletResponse();
        Map<String, Object> model = new HashMap<String, Object>();
        ServletContext context = new MockServletContext();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContextPath("/contextPath");
        request.setServletPath("/servletPath");

        StringTemplateView view = createTemplate(template, context, false, "text/html");
        view.render(model, request, response);

        verify(template).setAttribute(StringTemplateView.SERVLET_PATH, "/contextPath/servletPath");
        verify(template).setAttribute(StringTemplateView.BASE_PATH, "/contextPath");
    }

    @Test
    public void shouldSetContentTypeBeforeInvokingWriter() throws Exception {
        WebStringTemplate template = mock(WebStringTemplate.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        Map<String, Object> model = new HashMap<String, Object>();
        ServletContext context = new MockServletContext();

        StringTemplateView view = createTemplate(template, context, false, "text/xml");
        view.render(model, request, response);

        InOrder order = inOrder(response);
        order.verify(response).setContentType("text/xml");
        order.verify(response).getWriter();
    }

    private StringTemplateView createTemplate(WebStringTemplate template, ServletContext context,
                                              boolean exposeRequestContext, String contentType) {

        StringTemplateView view = new StringTemplateView();
        view.setExposeRequestContext(exposeRequestContext);
        view.setContentType(contentType);
        view.setServletContext(context);
        view.setTemplate(template);
        return view;
    }
}
