
package dvdstoreserver;

/**
 *
 * @author: Jacobus de Beer
 * @studnr: 216065186   
 * @group: 2A
 */

import java.sql.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.io.*;

public class DvdStoreServer {
    public String tName;
    private ServerSocket listener;
    private Socket client;
    
    public DvdStoreServer()
    {
        try {
            listener = new ServerSocket(12345, 10);
        }
        catch (IOException ioe)
        {
          System.out.println("IO Exception: " + ioe.getMessage());
        }
    }
    public void listen(){
        // Start listening for client connections
        try {
          System.out.println("Server is listening");
          client = listener.accept();  
          System.out.println("Now moving onto processClient");
          
          processClient();
        }
        catch(IOException ioe)
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }
    public void processClient()
    {
        // Communicate with the client
        
        // First step: initiate channels
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            
            // Step 2: communicate
            String msg = (String)in.readObject();
            System.out.println("From CLIENT>> " + msg);
            out.writeObject("Hello " + msg);
            out.flush();
            
            // Step 3:close down
            out.close();
            in.close();
            client.close();        
        }
        catch (IOException ioe)
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
        catch (ClassNotFoundException cnfe)
        {
            System.out.println("Class not found: " + cnfe.getMessage());
        }
    }
        
    public static void main(String[] args) {
      DvdStoreServer server = new DvdStoreServer();
      //to call on database
      try{
          String filename = "C:\\Users\\jpjde\\Desktop\\DVD Store\\publisher.mdb";
          String dbURL = "jdbc:ucanaccess://";
          dbURL+= filename.trim() + ";DriverID=22;READONLY=true}";
        //  System.out.println("About to Load the JDBC Driver....");
          Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        //  System.out.println("Driver Loaded Successfully....");
         // System.out.println("About to get a connection....");
          Connection con = DriverManager.getConnection(dbURL); 
         // System.out.println("Connection Established Successfully....");
         // System.out.println("Creating statement Object....");
          Statement serverCom = con.createStatement();
         // System.out.println("Statement object created Successfully....");
         // System.out.println("About to execute SQL stmt....");
          server.listen();
          
      }
      catch (Exception err) {
        System.out.println("ERROR: " + err);
      }
    }
    
}
