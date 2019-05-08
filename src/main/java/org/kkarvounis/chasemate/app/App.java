package org.kkarvounis.chasemate.app;

import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class App {
    private static final String DEFAULT_PORT = "8080";
    private static final String DEFAULT_HOSTNAME = "localhost";

    public static void main(String[] args) throws Exception {
        String contextPath = "/";
        String appBase = "src/main/webapp";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(App.getPort());
        tomcat.setHostname(App.getHostname());
        tomcat.getHost().setAppBase(appBase);
        tomcat.addWebapp(contextPath, new File(appBase).getAbsolutePath());
        tomcat.start();
        tomcat.getServer().await();
    }

    private static int getPort() {
        String port = System.getenv("PORT");
        if (port == null) {
            port = DEFAULT_PORT;
        }

        return Integer.valueOf(port);
    }

    private static String getHostname() {
        String hostname = System.getenv("HOSTNAME");
        if (hostname == null) {
            hostname = DEFAULT_HOSTNAME;
        }

        return hostname;
    }
}
