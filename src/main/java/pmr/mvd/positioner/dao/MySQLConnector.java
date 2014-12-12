package pmr.mvd.positioner.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {
    private String hostname = "127.0.0.1";
    private String username = "root";
    private String password= "";

    public void setHostname(String arg){
        this.hostname = arg;
    }

    public void setUsername(String arg){
        this.username = arg;
    }

    public void setPassword(String arg){
        this.password = arg;
    }

    public Connection getConnect(){
        Connection result = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            result = DriverManager.getConnection(this.hostname, this.username, this.password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
