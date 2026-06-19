package hbv.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.naming.*;

public class CatalogServlet extends HttpServlet {
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

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException, ServletException {

    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

    String filiale = req.getParameter("filiale");
    Connection con = null;

    try {
      con = ds.getConnection();

      out.println("<html><body>");
      out.println("<h2>Filiale auswählen</h2>");

      out.println("<form method='get'>");
      out.println("<select name='filiale'>");

      Statement st = con.createStatement();

      ResultSet rs =
        st.executeQuery(
          "select id, bezeichnung from swe2Filiale"
        );

      while (rs.next()) {

        int id = rs.getInt("id");

        out.print("<option value='" + id + "'");

        if (String.valueOf(id).equals(filiale)) {
          out.print(" selected");
        }

        out.println(">");
        out.println(rs.getString("bezeichnung"));
        out.println("</option>");
      }

      out.println("</select>");
      out.println("<input type='submit' value='Anzeigen'>");
      out.println("</form>");

      if (filiale != null) {

        PreparedStatement ps =
          con.prepareStatement(
            """
            select e.id,e.inventarnummer,b.titel
            from swe2Exemplar e
            join swe2Buch b
            on e.swe2Buch_id = b.id
            join swe2Lagerort l
            on e.swe2Lagerort_id = l.id
            where l.swe2Filiale_id = ?
            and e.status = 'frei'
            """
          );

        ps.setInt(1, Integer.parseInt(filiale));
        rs = ps.executeQuery();

        out.println("<h2>Exemplare</h2>");

        while (rs.next()) {

          int exemplarId = rs.getInt("id");

          out.println("<hr>");
          out.println("Titel: " + rs.getString("titel") + "<br>");
          out.println("Inventarnummer: " + rs.getString("inventarnummer") + "<br>");
          
          out.println("<form method='post'>");
          out.println("<input type='hidden' name='exemplarId' value='" + exemplarId + "'>");
          out.println("<input type='submit' value='In Warenkorb'>");
          out.println("</form>");
        }
      }

      out.println("<hr>");
      out.println("<a href='cart'>Warenkorb anzeigen</a>");

      out.println("</body></html>");

    } catch (SQLException e) {
      throw new ServletException(e);
    } finally {
      if (con != null) {
        try { con.close(); } catch (SQLException ignored) {}
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest req,
      HttpServletResponse res)
      throws IOException, ServletException {

    int exemplarId = Integer.parseInt(req.getParameter("exemplarId"));

    String filiale = req.getParameter("filiale");

    HttpSession session = req.getSession(true);

    synchronized (session) {

      try (Connection con = ds.getConnection()) {

        PreparedStatement ps =
          con.prepareStatement(
            "select status from swe2Exemplar where id=?"
          );

        ps.setInt(1, exemplarId);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
          res.sendRedirect("catalog?filiale=" + filiale);
          return;
        }

        if (!"frei".equals(rs.getString("status"))) {
          res.sendRedirect("catalog?filiale=" + filiale);
          return;
        }

        ps = con.prepareStatement(
          "update swe2Exemplar set status='belegt' where id=?"
        );

        ps.setInt(1, exemplarId);
        ps.executeUpdate();
      } catch(SQLException e) {  
      }

      List<Integer> cart =
        (List<Integer>) session.getAttribute("cart");

      if (cart == null) {
        cart = new ArrayList<>();
      }

      cart.add(exemplarId);

      session.setAttribute("cart", cart);
    }

    res.sendRedirect("catalog?filiale=" + filiale);
  }
}
