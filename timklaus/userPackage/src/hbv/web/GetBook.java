package hbv.web;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class GetBook extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
    throws IOException, ServletException {

    res.setContentType("text/html; charset=UTF-8");
    PrintWriter pw = res.getWriter();

    String login = req.getParameter("isbn");
    HttpSession session=req.getSession();

    if (login == null && session == null) {
      pw.println("fehler");
      return;
    }

    try {

      String html =
        "<form method='get' action='insertbook'>" +
        
        "isbn:<br>" +
        "<input name='isbn' value=''><br><br>" +

        "title:<br>" +
        "<input name='title' value=''><br><br>" +

        "author:<br>" +
        "<input name='author' value=''><br><br>" +

        "publisher:<br>" +
        "<input name='publisher' value=''><br><br>" +

        "year:<br>" +
        "<input name='year' value=''><br><br>" +

        "language:<br>" +
        "<input name='language' value=''><br><br>" +

        "pages:<br>" +
        "<input name='pages' value=''><br><br>" +

        "genre:<br>" +
        "<input name='genre' value=''><br><br>" +

        "<button type='submit'>Speichern</button>" +
        "</form>";

      pw.println(html);

    } catch (Exception e) {
      pw.println("error: " + e.getMessage());
    }
  }
}
