package hbv.web;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class SelectServlet extends HttpServlet {

  protected void doGet(HttpServletRequest  request,
      HttpServletResponse response)
      throws IOException, ServletException {

      response.setContentType("text/plain");
      PrintWriter out = response.getWriter();
      long start = System.nanoTime();
      int rows=0;

      try {
        // Naming Context
        Context initCtx = new InitialContext();
        DataSource ds = (DataSource)initCtx.lookup("java:/comp/env/jdbc/mariadb");

        // get connection ...
        Connection connection = ds.getConnection();

        // preparedstatement to prevent sql-injection
        PreparedStatement ps = connection.prepareStatement(
            "insert into demo (name) values(?)");
        ps.setString(1,"whatevercomes...");
        ps.executeUpdate();
        ps.close();

        // simple select statement is ok
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from demo limit 10");

        // cursor pattern
        while(rs.next()){
          String name = rs.getString("name");
          rows++;
        }
        rs.close();
        stmt.close();

        connection.close();
      } catch(Exception e){
        out.println(e);
        e.printStackTrace(out);
        throw new RuntimeException(e);
      }
      long ende = System.nanoTime();
      out.format("%3d in %11.2fms\n",rows,(ende-start)/1000000d);
  }
}
