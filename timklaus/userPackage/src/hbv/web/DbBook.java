package hbv.web;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class DbBook extends HttpServlet {
  private DataSource ds;
  @Override
  public void init() throws ServletException {
    try {
      Context initCtx = new InitialContext();
      this.ds = (DataSource) initCtx.lookup("java:/comp/env/jdbc/mariadb");
    } catch (NamingException e) {
      throw new ServletException("Zentraler JNDI Lookup fehlgeschlagen", e);
    }
  }

  protected void doGet(HttpServletRequest  req,
    HttpServletResponse res)
    throws IOException, ServletException {
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    
    try (Connection connection = ds.getConnection();
    
      PreparedStatement ps = connection.prepareStatement("select * from swe2Buch")) {

      ResultSet SqlRes = ps.executeQuery();
      
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        out.println(
          rs.getInt("id") + " | " +
          rs.getString("titel") + " | " +
          rs.getString("isbn"));
      }
        
      }catch (Exception e) {
      //out.println(e);
      //res.sendRedirect(req.getContextPath() + "/getbook");
      //      e.printStackTrace(out);
      //throw new RuntimeException(e);
    }
  }
}
