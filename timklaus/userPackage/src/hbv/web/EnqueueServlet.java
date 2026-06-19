package hbv.web;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import redis.clients.jedis.*;

public class EnqueueServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    
    response.setContentType("text/plain");
    PrintWriter out = response.getWriter();
    
    long start = System.nanoTime();

    RedisClient redisClient = JedisAdapter.getClient(); 
    String nanoStr = Long.toString(System.nanoTime());
    redisClient.lpush("myqueue", nanoStr);

    long ende = System.nanoTime();    
    out.format("%11.2fms\n", (ende - start) / 1000000d);
  }
}

