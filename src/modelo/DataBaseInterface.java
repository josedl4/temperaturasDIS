/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author joselm
 */
public class DataBaseInterface {
    
    private static Connection conn;
    private static Statement stat;
    
    public DataBaseInterface () {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Derby driver not found.");
        }
        try {
            String url = "jdbc:derby://localhost:1527/";
            String dbName = "temperatures";
            String userName = "root";
            String passwd = "root";
            conn = DriverManager.getConnection(url+dbName, userName, passwd);
            stat = conn.createStatement();
            stat.execute("CREATE TABLE temperaturas (temperatura real not null, momento date not null, primary key(temperatura, momento))");            
            
        } catch (SQLException ex) {
            System.out.println("Err");
            System.err.println(ex.getMessage());
        }
    }
    
    public void addTemperature (Momento moment, double temp) throws SQLException {
        String t = Double.toString(temp);
                
        stat.execute("INSERT INTO temperaturas VALUES ( " + t + ", " + "'" 
                + moment.toString() + "'" + ")");
    }
    
    
    public HashMap<Momento, Temperatura> getData() throws SQLException {
        HashMap<Momento, Temperatura> result = new HashMap<Momento,Temperatura>();
        
        ResultSet query = stat.executeQuery("SELECT * FROM temperaturas");
        while(query.next()){
            float temp = (float) query.getObject(1);
            Date d = (Date) query.getObject(2);
            Momento m = new Momento(d);
            result.put( m, new Temperatura(temp));
        }
        
        return result;
    }
}
