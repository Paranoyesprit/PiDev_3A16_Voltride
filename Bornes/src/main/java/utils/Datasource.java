package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Datasource {
    private Connection cnx;
    private String url="jdbc:mysql://localhost:3306/pi";
    private String login="root";
    private String pwd="";
    private static Datasource instance;


    private Datasource() {
        try {
            cnx= DriverManager.getConnection(url,login,pwd);
            System.out.println("succes");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Datasource getInstance(){
        if (instance==null)
            instance=new Datasource();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}
