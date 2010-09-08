package com.watchitlater.spring;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.commons.lang.UnhandledException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class WebStringTemplateGroup extends StringTemplateGroup {

    protected final ResourceLoader loader;

    public WebStringTemplateGroup(String groupName, String templateRoot, ResourceLoader loader) {
        super(groupName, templateRoot);
        this.loader = loader;
    }

    public WebStringTemplate createTemplate(String templatePath) {
        return (WebStringTemplate) getInstanceOf(templatePath);
    }

    @Override
    public StringTemplate createStringTemplate() {
        return new WebStringTemplate();
    }

    @Override
    protected StringTemplate loadTemplate(String name, String filePath) {
        Resource resource = loader.getResource(filePath);
        if (!resource.exists()) {
            return null;
        }
        InputStream stream = null;
        BufferedReader reader = null;
        try {
            stream = resource.getInputStream();
            reader = new BufferedReader(getInputStreamReader(stream));
            return loadTemplate(name, reader);

        } catch (Exception e) {
            throw new UnhandledException(e);

        } finally {
            closeQuietly(reader);
            closeQuietly(stream);
        }
    }

    protected void closeQuietly(Closeable item) {
        if (item != null) {
            try {
                item.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
