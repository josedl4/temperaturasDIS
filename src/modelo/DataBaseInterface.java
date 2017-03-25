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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
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
    
    public void addTemperature (LocalDate ld, double temp) throws SQLException {
        String t = Double.toString(temp);
        String d = "'" + ld.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "'" ;
        System.out.println(stat);
        stat.execute("INSERT INTO temperaturas VALUES ( " + t + ", " + d + ")");
    }
    
    
    public HashMap<LocalDate, Temperatura> getData() throws SQLException {
        HashMap<LocalDate, Temperatura> result = new HashMap<LocalDate,Temperatura>();
        
        ResultSet query = stat.executeQuery("SELECT * FROM temperaturas");
        while(query.next()){
            float temp = (float) query.getObject(1);
            Date d = (Date) query.getObject(2);
            result.put( d.toLocalDate(), new Temperatura(temp));
        }
        
        return result;
    }
}
