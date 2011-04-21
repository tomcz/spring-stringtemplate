package com.watchitlater.spring.webapp;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class WebServer {

    private Server server;
    private final int port;

    public WebServer(int port) {
        this.port = port;
    }

    public WebServer start() throws Exception {
        System.setProperty("org.mortbay.xml.XmlParser.Validating", "false");

        server = new Server(port);
        server.addHandler(new WebAppContext("src/test/webapp", "/stringtemplate"));
        server.start();

        return this;
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new WebServer(8080).start();
    }
}
