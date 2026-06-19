package hbv.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import javax.sql.*;
import javax.naming.*;

import java.io.*;
import java.sql.*;
import java.util.*;

public class CartServlet extends HttpServlet {

  private DataSource ds;

  @Override
  public void init() throws ServletException {
    try {
      Context initCtx = new InitialContext();
      ds = (DataSource)
        initCtx.lookup("java:/comp/env/jdbc/mariadb");
    } catch (NamingException e) {
      throw new ServletException(e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req,
      HttpServletResponse res)
    throws IOException, ServletException {

    res.setContentType("text/html");

    PrintWriter out = res.getWriter();

    HttpSession session = req.getSession(false);

    out.println("<html><body>");
    out.println("<h2>Warenkorb</h2>");

    if (session != null) {

      List<Integer> cart =
        (List<Integer>)
        session.getAttribute("cart");

      if (cart != null && !cart.isEmpty()) {

        for (Integer exemplarId : cart) {
          out.println(
              "Exemplar ID: "
              + exemplarId
              + "<br>");
        }

        out.println("<br>");

        out.println("<form method='post'>");
        out.println(
            "<input type='submit' value='Bestellen'>");
        out.println("</form>");

      } else {

        out.println("Warenkorb ist leer.<br>");
      }
    }

    out.println("<br>");
    out.println("<a href='catalog'>Zurück</a>");
    out.println("</body></html>");
  }

  @Override
  protected void doPost(HttpServletRequest req,
      HttpServletResponse res)
    throws IOException, ServletException {

    HttpSession session =
      req.getSession(false);

    if (session == null) {
      res.sendRedirect("cart");
      return;
    }

    List<Integer> cart =
      (List<Integer>)
      session.getAttribute("cart");

    if (cart == null || cart.isEmpty()) {
      res.sendRedirect("cart");
      return;
    }

    try (Connection con =
        ds.getConnection()) {

      con.setAutoCommit(false);

      try {

        PreparedStatement ps =
          con.prepareStatement(
              """
              insert into swe2Ausleihe
              (
               swe2Kunde_id,
               swe2Mitarbeiter_id,
               status
              )
              values (?, ?, ?)
              """,
              Statement.RETURN_GENERATED_KEYS
              );

        ps.setInt(1, 1);
        ps.setInt(2, 2);
        ps.setString(3, "ausstehend");

        ps.executeUpdate();

        ResultSet rs =
          ps.getGeneratedKeys();

        rs.next();

        int ausleiheId =
          rs.getInt(1);

        ps = con.prepareStatement(
            """
            insert into swe2Ausleihe_position
            (
             swe2Ausleihe_id,
             swe2Exemplar_id
            )
            values (?, ?)
            """
            );

        for (Integer exemplarId : cart) {

          ps.setInt(1, ausleiheId);
          ps.setInt(2, exemplarId);

          ps.executeUpdate();
        }

        con.commit();

        session.removeAttribute("cart");

      } catch (SQLException e) {

        con.rollback();
        throw e;
      }

    } catch (SQLException e) {
      throw new ServletException(e);
    }

    res.sendRedirect("cart");
  }
}
