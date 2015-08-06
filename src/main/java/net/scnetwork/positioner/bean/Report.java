package net.scnetwork.positioner.bean;

public class Report {
    private String number;
    private String date;
    private String longitude;
    private String latitude;
    private String velocity;
    private String url;

    public String getNumber(){
        return this.number;
    }

    public void setNumber(String arg){
        this.number = arg;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String arg){
        this.date = arg;
    }

    public String getLongitude(){
        return this.longitude;
    }

    public void setLongitude(String arg){
        this.longitude = arg;
    }

    public String getLatitude(){
        return this.latitude;
    }

    public void setLatitude(String arg){
        this.latitude = arg;
    }

    public String getVelocity(){
        return this.velocity;
    }

    public void setVelocity(String arg){
        this.velocity = arg;
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String arg){
        this.url = arg;
    }
}
