package hbv.web;

import jakarta.servlet.*;

public class MyContextListener implements ServletContextListener, ServletRequestListener {
  ServletContext ctx;

  public void contextInitialized(ServletContextEvent servletContextEvent) {
    ctx = servletContextEvent.getServletContext();
    ctx.log("contextInitialized");
  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    ctx.log("contextDestroyed");
  }

  public void requestInitialized(ServletRequestEvent evt) {}

}

