package hbv.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import javax.sql.DataSource;
import javax.naming.*;

import java.io.*;
import java.sql.*;

public class MitarbeiterServlet extends HttpServlet {
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

    out.println("<html><body>");
    out.println("<h2>Ausstehende Ausleihen</h2>");

    try (Connection con = ds.getConnection()) {

      PreparedStatement ps =
        con.prepareStatement(
            """
            select
            a.id,
            k.vorname,
            k.nachname
            from swe2Ausleihe a
            join swe2Kunde k
            on a.swe2Kunde_id = k.id
            where a.status = 'ausstehend'
            and a.swe2Mitarbeiter_id = 2
            """
            );

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {

        int ausleiheId = rs.getInt("id");

        out.println("<hr>");

        out.println(
            "Ausleihe "
            + ausleiheId
            + "<br>");

        out.println(
            rs.getString("vorname")
            + " "
            + rs.getString("nachname")
            + "<br>");

        PreparedStatement ps2 =
          con.prepareStatement(
              """
              select
              b.titel
              from swe2Ausleihe_position p
              join swe2Exemplar e
              on p.swe2Exemplar_id = e.id
              join swe2Buch b
              on e.swe2Buch_id = b.id
              where p.swe2Ausleihe_id = ?
              """
              );

        ps2.setInt(1, ausleiheId);

        ResultSet rs2 =
          ps2.executeQuery();

        while (rs2.next()) {

          out.println("Buchtitel: "
              +rs2.getString("titel")
              + "<br>");
        }

        out.println(
            "<form method='post'>");

        out.println(
            "<input type='hidden' " +
            "name='id' value='" +
            ausleiheId +
            "'>");

        out.println(
            "<input type='hidden' " +
            "name='aktion' value='aktiv'>");

        out.println(
            "<input type='submit' " +
            "value='Annehmen'>");

        out.println("</form>");

        out.println(
            "<form method='post'>");

        out.println(
            "<input type='hidden' " +
            "name='id' value='" +
            ausleiheId +
            "'>");

        out.println(
            "<input type='hidden' " +
            "name='aktion' value='abgelehnt'>");

        out.println(
            "<input type='submit' " +
            "value='Ablehnen'>");

        out.println("</form>");
      }

    } catch (SQLException e) {
      throw new ServletException(e);
    }

    out.println("</body></html>");
  }

  @Override
  protected void doPost(HttpServletRequest req,
      HttpServletResponse res)
    throws IOException, ServletException {

    int id =
      Integer.parseInt(
          req.getParameter("id"));

    String status =
      req.getParameter("aktion");

    try (Connection con =
        ds.getConnection()) {

      PreparedStatement ps =
        con.prepareStatement(
            """
            update swe2Ausleihe
            set status = ?
            where id = ?
            """
            );

      ps.setString(1, status);
      ps.setInt(2, id);

      ps.executeUpdate();

    } catch (SQLException e) {
      throw new ServletException(e);
    }

    res.sendRedirect("mitarbeiter");
  }
}
