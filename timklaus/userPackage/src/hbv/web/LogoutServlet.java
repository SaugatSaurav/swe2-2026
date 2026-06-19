package hbv.web;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class LogoutServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest  req,
      HttpServletResponse res)
      throws IOException, ServletException {

      HttpSession session=req.getSession(false);
      if(session != null){
        session.invalidate();
      }

      res.setContentType("text/plain");
      PrintWriter out = res.getWriter();
      out.println("logged out");
  }
}
