package com.watchitlater.spring.log;

import org.antlr.stringtemplate.StringTemplateErrorListener;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jStringTemplateErrorListener implements StringTemplateErrorListener {

    private final Logger logger = LoggerFactory.getLogger(StringTemplateGroup.class);

    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public void warning(String message) {
        logger.warn(message);
    }
}
