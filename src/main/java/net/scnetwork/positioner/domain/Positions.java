package net.scnetwork.positioner.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class Positions  implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final SimpleDateFormat sdf = new SimpleDateFormat();

    private String id;
    private String address;
    private String altitude;
    private String course;
    private String latitude;
    private String longitude;
    private String other;
    private String power;
    private String speed;
    private String time;
    private String valid;
    private String deviceId;

    public String getId(){
        return this.id;
    }

    public void setId(String arg){
        this.id = arg;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String arg){
        this.address = arg;
    }

    public String getAltitude(){
        return this.altitude;
    }

    public void setAltitude(String arg){
        this.altitude = arg;
    }

    public String getCourse(){
        return this.course;
    }

    public void setCourse(String arg){
        this.course = arg;
    }

    public String getLatitude(){
        return this.latitude;
    }

    public void setLatitude(String arg){
        this.latitude = arg;
    }

    public String getLongitude(){
        return this.longitude;
    }

    public void setLongitude(String arg){
        this.longitude = arg;
    }

    public String getOther(){
        return this.other;
    }

    public void setOther(String arg){
        this.other = arg;
    }

    public String getPower(){
        return this.power;
    }

    public void setPower(String arg){
        this.power = arg;
    }

    public String getSpeed(){
        return this.speed;
    }

    public void setSpeed(String arg){
        this.speed = arg;
    }

    public String getTime(){
        return this.time;
    }

    public void setTime(String arg){
        this.time = arg;
    }

    public String getValid(){
        return this.valid;
    }

    public void setValid(String arg){
        this.valid = arg;
    }

    public String getDeviceId(){
        return this.deviceId;
    }

    public void setDeviceId(String arg){
        this.deviceId = arg;
    }
}
