package pmr.mvd.positioner.dao;


import com.vaadin.tapio.googlemaps.client.LatLon;
import pmr.mvd.positioner.bean.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlDao {
    private MySQLConnector sqlConnector = new MySQLConnector();

    public SqlDao(){
        sqlConnector.setHostname("jdbc:mysql://tirgps.ddns.net:3306/traccar");
        sqlConnector.setUsername("traccar");
        sqlConnector.setPassword("traccar123");
    }

    public UserSettings GetUserSetting(String username){
        UserSettings result = new UserSettings();

        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = (Statement) connection.createStatement();

            String query = "select * from users where login='" + username + "'";

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                result.setUsername(resultSet.getString("login"));
                result.setPassword(resultSet.getString("password"));
                result.setGroup(resultSet.getString("admin"));
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<UserSettings> GetUsers(){
        ArrayList<UserSettings> result = new ArrayList<UserSettings>();
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            String query = "select * from users";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                UserSettings settings = new UserSettings();
                settings.setUsername(resultSet.getString("login"));
                settings.setGroup(resultSet.getString("admin"));
                result.add(settings);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Devices> GetDevices(){
        ArrayList<Devices> result = new ArrayList<Devices>();
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();

            String query = "select * from devices";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                Devices bean = new Devices();
                bean.setId(resultSet.getString("id"));
                bean.setName(resultSet.getString("name"));
                bean.setUniq(resultSet.getString("uniqueId"));
                bean.setLast(resultSet.getString("latestPosition_id"));
                result.add(bean);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Positions> GetPositions(String device){
        ArrayList<Positions> result = new ArrayList<Positions>();
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            String query;
            if (device.equals(""))
                query = "select * from positions order by id desc limit 20";
            else
                query = "select * from positions INNER JOIN devices ON devices.id = positions.device_id where name = '" + device + "' order by positions.id desc limit 300";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                Positions bean = new Positions();
                bean.setId(resultSet.getString("id"));
                bean.setAddress(resultSet.getString("address"));
                bean.setAltitude(resultSet.getString("altitude"));
                bean.setCourse(resultSet.getString("course"));
                bean.setLatitude(resultSet.getString("latitude"));
                bean.setLongitude(resultSet.getString("longitude"));
                bean.setOther(resultSet.getString("other"));
                bean.setPower(resultSet.getString("power"));
                bean.setSpeed(resultSet.getString("speed"));
                bean.setTime(resultSet.getString("time"));
                bean.setValid(resultSet.getString("valid"));
                bean.setDeviceId(resultSet.getString("device_id"));

                result.add(bean);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Report> GetReport(String name, String from, String to){
        ArrayList<Report> result = new ArrayList<Report>();
        String query = "select * from positions where name='" + name + "' and time > '" + from + "' and time < '" + to + "'";
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                Report report = new Report();
                report.setDate(resultSet.getString("time"));
                report.setLatitude(resultSet.getString("latitude"));
                report.setLongitude(resultSet.getString("longitude"));
                report.setNumber(resultSet.getString("id"));
                report.setVelocity(resultSet.getString("speed"));
                result.add(report);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean AddNewDevice(String name, String id){
        boolean result;
        String query = "insert into devices(name,uniqueId) values('" + name + "','" + id +"')";
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
            connection.close();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean DelDevice(String name){
        boolean result;
        String query = "delete from devices where name='" + name + "'";
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
            connection.close();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public ArrayList<LatLon> GetPathDevice(String dev) {
        ArrayList<LatLon> result = new ArrayList<LatLon>();
        String query = " select * from positions INNER JOIN devices ON devices.id = positions.device_id where name = '" + dev + "' order by positions.id desc limit 300";
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                LatLon latLon = new LatLon();
                latLon.setLat(Double.parseDouble(resultSet.getString("latitude")));
                latLon.setLon(Double.parseDouble(resultSet.getString("longitude")));
                result.add(latLon);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<DevPoint> getLastPosition(String username){
        ArrayList<DevPoint> result = new ArrayList<DevPoint>();
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            String queryDevices = "select name, latestPosition_id from devices";
            ResultSet resultSet = statement.executeQuery(queryDevices);
            while (resultSet.next()){
                DevPoint devPoint = new DevPoint();
                devPoint.setName(resultSet.getString("name"));
                String queryPos = "select latitude, longitude from positions where id='" + resultSet.getString("latestPosition_id") + "'";
                Statement statement1 = connection.createStatement();
                ResultSet rs = statement1.executeQuery(queryPos);
                while (rs.next()){
                    devPoint.setLat(rs.getString("latitude"));
                    devPoint.setLon(rs.getString("longitude"));
                }
                statement1.close();
                result.add(devPoint);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean AddNewUser(String value, String value1, String role) {
        boolean result;
        String query = "insert into users(admin,login,password,userSettings_id) values(" + role +
                ",'" + value + "','" + value1 +"', 1)";
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
            connection.close();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean ChangePassword(String chUser, String value) {
        boolean result;
        String query = "update users set password='" + value + "' where login='" + chUser + "'";
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
            connection.close();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean DelUser(String user) {
        boolean result;
        String query = "delete from users where login = '" + user + "'";
        System.out.println(query);
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
            connection.close();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean ChangeGroup(String user, String group) {
        boolean result;
        String query = "update users set admin=" + group + " where login='" + user + "'";
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
            connection.close();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public ArrayList<GroupDev> GetGroupDev(String device) {
        ArrayList<GroupDev> result = new ArrayList<GroupDev>();

        String query = "select * from groupdev where device='" + device +"'";
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                GroupDev bean = new GroupDev();
                bean.setId(rs.getString("id"));
                bean.setUser(rs.getString("username"));
                bean.setDevice(rs.getString("device"));
                result.add(bean);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean AddGroupUser(String nameuser, String namedevice) {
        boolean result;
        String query = "insert into groupdev(username,device) values('" + nameuser + "', '" + namedevice + "')";
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
            connection.close();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
