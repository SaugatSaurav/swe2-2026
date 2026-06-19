package hbv.web;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class LoginServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest  req,
      HttpServletResponse res)
      throws IOException, ServletException {
      PrintWriter pw = res.getWriter();

      String user=req.getParameter("user");
      String passwd=req.getParameter("passwd");

      if (user != null && passwd != null){
        HttpSession session=req.getSession();
        session.setAttribute("user",user);
        pw.println("ok");
      } else {
        pw.println("fehler");
      }
  }
}
