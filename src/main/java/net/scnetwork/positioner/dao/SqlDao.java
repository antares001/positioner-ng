package net.scnetwork.positioner.dao;

public class SqlDao {
    private MySQLConnector sqlConnector = new MySQLConnector();

    public SqlDao(){
        sqlConnector.setHostname("jdbc:mysql://tirgps.ddns.net:3306/traccar");
        sqlConnector.setUsername("traccar");
        sqlConnector.setPassword("traccar123");
    }
}
