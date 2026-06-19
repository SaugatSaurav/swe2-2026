package hbv.web;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class InsertBook extends HttpServlet {
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
    
    String sIsbn = req.getParameter("isbn");
    int isbn = Integer.parseInt(sIsbn);
    String title = req.getParameter("title");
    String author = req.getParameter("author");
    String sPublisher = req.getParameter("publisher");
    int swe2Verlag_id = Integer.parseInt(sPublisher);
    String sJahr = req.getParameter("year");
    int erscheinungsjahr = Integer.parseInt(sJahr);
    String sprache = req.getParameter("language");
    String sPages = req.getParameter("pages");
    int seitenanzahl = Integer.parseInt(sPages);
    String genre = req.getParameter("genre");

    try (Connection connection = ds.getConnection(); 
      PreparedStatement ps = connection.prepareStatement("insert ignore into swe2Buch (titel, isbn, erscheinungsjahr, genre, seitenanzahl, sprache, swe2Verlag_id) values(?,?,?,?,?,?,?)")) {

      ps.setString(1, title);
      ps.setInt(2, isbn);
      ps.setInt(3, erscheinungsjahr);
      ps.setString(4, genre);
      ps.setInt(5, seitenanzahl);
      ps.setString(6, sprache);
      ps.setInt(7, swe2Verlag_id);
      if (ps.executeUpdate() < 1){
        res.sendRedirect(req.getContextPath() + "/getbook");
      }
      out.println("executed");
    }catch (Exception e) {
      //out.println(e);
      res.sendRedirect(req.getContextPath() + "/getbook");
      //      e.printStackTrace(out);
      //throw new RuntimeException(e);
    }
  }
}
