package hbv.swe2;

import java.io.*;
import java.net.*;

public class MyServer {
    
    public static void main(String[] args) throws IOException {
        ServerSocket seso = new ServerSocket(9001);
        System.out.println("Server waiting for connection on port 9001...");
        
        Socket sock = seso.accept();
        System.out.println("Client connected!");
        
        NetProtocol np = new NetProtocol(sock.getInputStream(), sock.getOutputStream());
        np.communicate();
        
        sock.close();
        seso.close();
    }
}

class NetProtocol {  
    BufferedReader br;
    PrintWriter pw;
    
    // Konstruktor (kein Rückgabetyp – das ist richtig!)
    NetProtocol(InputStream in, OutputStream out) {
        // Korrekte Erstellung des BufferedReader
        br = new BufferedReader(new InputStreamReader(in));  // ← Wichtig!
        pw = new PrintWriter(out, true);
    }
    
    public void communicate() throws IOException {
        String line;  
        
        while ((line = br.readLine()) != null) {
            pw.println(line);  // Echo an den Client zurücksenden
            System.out.println("Echo: " + line);  // Optional: Logging
        }
    }
}
