package main.http;

import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * @author Stanislav Valov <stanislav.valov@clouway.com>
 */
class JettyServer {

  public void startServer() {
    Server server = new Server(8080);

    WebAppContext webapp = new WebAppContext();
    webapp.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
    webapp.setContextPath("/");
    webapp.setWar("front-end/build");

    String[] welcomeFiles = new String[]{"/index.html"};

    webapp.setWelcomeFiles(welcomeFiles);
    server.setHandler(webapp);

    try {
      server.start();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
