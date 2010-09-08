package com.watchitlater.spring;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletContext;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class StringTemplateViewResolverTests {

    private StringTemplateViewResolver resolver;

    @Before
    public void setUp() {
        resolver = new StringTemplateViewResolver();
        resolver.setServletContext(new MockServletContext());
        resolver.setResourceLoader(new FileSystemResourceLoader());
        resolver.setTemplateRoot("src/test/webapp/templates");
    }

    @Test
    public void shouldResolveTemplateFromFilesystem() {
        StringTemplateView view = resolver.resolveViewName("form", Locale.getDefault());
        assertThat(view, notNullValue());
    }

    @Test
    public void shouldReturnNullWhenTemplateDoesNotExist() {
        StringTemplateView view = resolver.resolveViewName("missing", Locale.getDefault());
        assertThat(view, nullValue());
    }
}
