
package dvdstoreserver;

/**
 *
 * @author: Jacobus de Beer
 * @studnr: 216065186   
 * @group: 2A
 */

import dvd.store.Message;
import dvd.store.Message.Action;
import java.sql.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
            Message msg = (Message)in.readObject();
            if(msg.getAction() != Action.Get)
                SaveToDatabase(msg.getStatement());
            else
                msg = GetFromDatabase(msg);
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
        server.listen();
          
      }
      catch (Exception err) {
        System.out.println("ERROR: " + err);
      }
    }

    private void SaveToDatabase(String statement) {
         
          try {
                Path path = Paths.get("Database/publisher.mdb");
                Path absolutePath = path.toAbsolutePath();
                String filename = absolutePath.toString();
                String dbURL = "jdbc:ucanaccess://";//specify the full pathname of the database
                dbURL+= filename.trim() + ";DriverID=22;READONLY=true}"; 
                String driverName = "net.ucanaccess.jdbc.UcanaccessDriver";

                System.out.println("About to Load the JDBC Driver....");
                Class.forName(driverName);
                System.out.println("Driver Loaded Successfully....");
                System.out.println("About to get a connection....");
                Connection con = DriverManager.getConnection(dbURL); 
                System.out.println("Connection Established Successfully....");
                // create a java.sql.Statement so we can run queries
                System.out.println("Creating statement Object....");
                Statement s = con.createStatement();

                System.out.println("Statement object created Successfully....");

                System.out.println("About to execute SQL stmt....");

                s.executeUpdate(statement);

                System.out.println("About to close Statement....");
                s.close(); // close the Statement to let the database know we're done with it
                con.close(); // close the Connection to let the database know we're done with it
                System.out.println("Statement closed successfully....");
            }


                catch (Exception err) {
                System.out.println("ERROR: " + err);
            }
    }

    private Message GetFromDatabase(Message msg) {
        try {
                Path path = Paths.get("Database/publisher.mdb");
                Path absolutePath = path.toAbsolutePath();
                String filename = absolutePath.toString();
                String dbURL = "jdbc:ucanaccess://";//specify the full pathname of the database
                dbURL+= filename.trim() + ";DriverID=22;READONLY=true}"; 
                String driverName = "net.ucanaccess.jdbc.UcanaccessDriver";

                System.out.println("About to Load the JDBC Driver....");
                Class.forName(driverName);
                System.out.println("Driver Loaded Successfully....");
                System.out.println("About to get a connection....");
                Connection con = DriverManager.getConnection(dbURL); 
                System.out.println("Connection Established Successfully....");
                // create a java.sql.Statement so we can run queries
                System.out.println("Creating statement Object....");
                Statement s = con.createStatement();

                System.out.println("Statement object created Successfully....");

                System.out.println("About to execute SQL stmt....");

                ResultSet rs = s.executeQuery(msg.getStatement()); // select the data from the table

        //        ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
                if (rs != null) // if rs == null, then there is no ResultSet to view        
                while ( rs.next() ) // this will step through our data row-by-row
                    {
                   System.out.println("Data from column_name: " 
                   + rs.getString(1) + " "+rs.getString(2) );
                }

                System.out.println("About to close Statement....");
                s.close(); // close the Statement to let the database know we're done with it
                con.close(); // close the Connection to let the database know we're done with it
                System.out.println("Statement closed successfully....");
            }


                catch (Exception err) {
                System.out.println("ERROR: " + err);
            }
        return msg;
    }
    
}
