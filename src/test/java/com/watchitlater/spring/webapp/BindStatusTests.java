package com.watchitlater.spring.webapp;

import com.meterware.httpunit.HTMLElement;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.ServerSocket;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class BindStatusTests {

    private static WebServer server;
    private static int serverPort;

    @BeforeClass
    public static void startServer() throws Exception {
        serverPort = findFreePort();
        server = new WebServer(serverPort).start();
        HttpUnitOptions.setScriptingEnabled(false);
    }

    @AfterClass
    public static void stopServer() {
        server.stop();
    }

    @Test
    public void shouldSeeErrorsForNumberFieldAfterFormSubmission() throws Exception {
        String name = RandomStringUtils.randomAlphanumeric(10);
        String number = RandomStringUtils.randomAlphabetic(10);

        WebConversation conv = new WebConversation();
        WebResponse response = conv.getResponse(formPage());

        WebForm form = response.getFormWithID("testForm");
        form.setParameter("name", name);
        form.setParameter("number", number);
        response = form.submit();

        form = response.getFormWithID("testForm");
        assertThat(form.getParameterValue("name"), equalTo(name));
        assertThat(form.getParameterValue("number"), equalTo(number));

        HTMLElement[] elements = response.getElementsWithAttribute("class", "number error");
        assertThat(elements.length, equalTo(1));
    }

    @Test
    public void shouldRedirectBackToClearedFormOnReset() throws Exception {
        String name = RandomStringUtils.randomAlphanumeric(10);

        WebConversation conv = new WebConversation();
        WebResponse response = conv.getResponse(formPage());

        WebForm form = response.getFormWithID("testForm");
        form.setParameter("name", name);
        response = form.submit();

        form = response.getFormWithID("testForm");
        assertThat(form.getParameterValue("name"), equalTo(name));

        response = response.getLinkWith("Reset").click();

        form = response.getFormWithID("testForm");
        assertThat(form.getParameterValue("name"), equalTo(""));
    }

    private String formPage() {
        return "http://localhost:" + serverPort + "/stringtemplate/page/form";
    }

    private static int findFreePort() throws Exception {
        ServerSocket socket = new ServerSocket(0);
        int port = socket.getLocalPort();
        socket.close();
        return port;
    }
}
