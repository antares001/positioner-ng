package pmr.mvd.positioner.dao;


import pmr.mvd.positioner.bean.Devices;
import pmr.mvd.positioner.bean.Positions;
import pmr.mvd.positioner.bean.Report;
import pmr.mvd.positioner.bean.UserSettings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlDao {
    MySQLConnector sqlConnector = new MySQLConnector();

    public SqlDao(){
        sqlConnector.setHostname("jdbc:mysql://127.0.0.1:3306/traccar");
        sqlConnector.setUsername("root");
        sqlConnector.setPassword("oopwdlin357");
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
                query = "select * from positions INNER JOIN devices ON devices.id = positions.device_id where name = '" + device + "'";

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
                bean.setTime(resultSet.getDate("time"));
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
}
