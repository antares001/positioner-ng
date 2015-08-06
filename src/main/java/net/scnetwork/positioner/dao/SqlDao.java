package net.scnetwork.positioner.dao;


import com.vaadin.tapio.googlemaps.client.LatLon;
import net.scnetwork.positioner.bean.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
        String query = "select * from positions INNER JOIN devices ON devices.id = positions.device_id where name = '" +
                name + "' and time > '" + from + "' and time < '" + to + "'";
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

    private int getOperation(String arg){
        int result = -1;

        if (arg.equals("add_new_device"))
            return 0;
        if (arg.equals("del_device"))
            return 1;
        if (arg.equals("add_new_user"))
            return 2;
        if (arg.equals("change_password"))
            return 3;
        if (arg.equals("delete_user"))
            return 4;
        if (arg.equals("change_group"))
            return 5;
        if (arg.equals("add_group_user"))
            return 6;
        if (arg.equals("del_group_dev"))
            return 7;

        return result;
    }

    public boolean ExecuteOperation(HashMap<String,String> params, String operation){
        boolean result;
        String query = "";
        switch (getOperation(operation)){
            case 0:
                String name = params.get("name");
                String id = params.get("id");
                query = "insert into devices(name,uniqueId) values('" + name + "','" + id +"')";
                break;
            case 1:
                name = params.get("name");
                query = "delete from devices where name='" + name + "'";
                break;
            case 2:
                String role = params.get("role");
                String value = params.get("value");
                String value1 = params.get("value1");
                query = "insert into users(admin,login,password,userSettings_id) values(" + role + ",'" + value + "','" + value1 +"', 1)";
                break;
            case 3:
                value = params.get("value");
                String user = params.get("user");
                query = "update users set password='" + value + "' where login='" + user + "'";
                break;
            case 4:
                user = params.get("user");
                query = "delete from users where login = '" + user + "'";
                break;
            case 5:
                String group = params.get("group");
                user = params.get("user");
                query = "update users set admin=" + group + " where login='" + user + "'";
                break;
            case 6:
                user = params.get("user");
                String device = params.get("device");
                query = "insert into groupdev(username,device) values('" + user + "', '" + device + "')";
                break;
            case 7:
                user = params.get("user");
                device = params.get("device");
                query = "delete from groupdev where username='" + user + "' and device='" + device + "'";
                break;
        }

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

            Statement statementUserDev = connection.createStatement();
            String listDev = "select device from groupdev where username='" + username + "'";
            ResultSet rsDev = statementUserDev.executeQuery(listDev);
            while (rsDev.next()) {
                String devName = rsDev.getString("device");
                Statement statement = connection.createStatement();
                String queryDevices = "select name, latestPosition_id from devices where name='" + devName + "'";
                ResultSet resultSet = statement.executeQuery(queryDevices);
                while (resultSet.next()) {
                    DevPoint devPoint = new DevPoint();
                    devPoint.setName(resultSet.getString("name"));
                    String queryPos = "select latitude, longitude from positions where id='" + resultSet.getString("latestPosition_id") + "'";
                    Statement statement1 = connection.createStatement();
                    ResultSet rs = statement1.executeQuery(queryPos);
                    while (rs.next()) {
                        devPoint.setLat(rs.getString("latitude"));
                        devPoint.setLon(rs.getString("longitude"));
                    }
                    statement1.close();
                    result.add(devPoint);
                }
                statement.close();
            }
            statementUserDev.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public ArrayList<GroupDev> GetGroupUser(String nameuser) {
        ArrayList<GroupDev> result = new ArrayList<GroupDev>();

        String query = "select * from groupdev where username='" + nameuser +"'";
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

    public ArrayList<GroupDev> GetDevices(String username) {
        ArrayList<GroupDev> result = new ArrayList<GroupDev>();
        try {
            Connection connection = sqlConnector.getConnect();
            Statement statementUserDev = connection.createStatement();
            String listDev = "select device from groupdev where username='" + username + "'";
            ResultSet rsDev = statementUserDev.executeQuery(listDev);
            while (rsDev.next()){
                GroupDev bean = new GroupDev();
                bean.setDevice(rsDev.getString("device"));
                bean.setUser(username);
                result.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
