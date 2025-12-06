package com.immobilier.soap;
import java.sql.Connection;
import java.sql.DriverManager;
public class DBConnexion {
    static { try { Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); } catch(Exception e){ e.printStackTrace(); } }
    public static Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=ImmobilierDB;encrypt=false";
        return DriverManager.getConnection(url, "sa", "123456");
    }
}
