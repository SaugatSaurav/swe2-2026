package hbv.swe2;

import java.io.*;
import java.net.*;

public class MyClient {
    public static void main(String[] args) throws IOException {
        Socket sock = new Socket("localhost", 9001);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        PrintWriter pw = new PrintWriter(sock.getOutputStream(), true);
        
        // Lies von der Tastatur und sende zum Server
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        String line;
        
        System.out.println("Verbunden mit Server. Gib Nachrichten ein (exit zum Beenden):");
        
        while ((line = keyboard.readLine()) != null) {
            if (line.equals("exit")) break;
            
            pw.println(line);  // Sende zum Server
            
            // Lies Antwort vom Server
            String response = br.readLine();
            if (response != null) {
                System.out.println("Server antwortet: " + response);
            }
        }
        
        sock.close();
    }
}
