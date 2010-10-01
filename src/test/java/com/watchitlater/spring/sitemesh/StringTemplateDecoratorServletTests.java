package com.watchitlater.spring.sitemesh;

import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

public class StringTemplateDecoratorServletTests {

    @Test
    public void shouldSend404ErrorWhenDecoratorNotFound() throws Exception {
        WebApplicationContext wac = mock(WebApplicationContext.class);
        Resource resource = mock(Resource.class);

        MockServletContext servletContext = new MockServletContext();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, wac);

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();

        StringTemplateDecoratorServlet servlet = new StringTemplateDecoratorServlet();
        servlet.init(new MockServletConfig(servletContext));

        given(wac.getResource(anyString())).willReturn(resource);

        servlet.doGet(request, response);

        assertThat(response.getStatus(), equalTo(HttpServletResponse.SC_NOT_FOUND));
    }
}
