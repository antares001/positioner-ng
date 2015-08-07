package net.scnetwork.positioner.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {
    private String hostname;
    private String username;
    private String password;

    public MySQLConnector(){
        this.hostname = "jdbc:mysql://127.0.0.1:3306/postioner";
        this.username = "root";
        this.password = "oopwdlin357";
    }

    public MySQLConnector(String hostname, String username, String password){
        this.hostname = hostname;
        this.username = username;
        this.password = password;
    }

    public String getHostname(){
        return this.hostname;
    }

    public void setHostname(String arg){
        this.hostname = arg;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String arg){
        this.username = arg;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String arg){
        this.password = arg;
    }

    public Connection getConnect(){
        Connection result = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            result = DriverManager.getConnection(this.hostname, this.username, this.password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
