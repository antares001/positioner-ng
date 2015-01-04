package pmr.mvd.positioner.bean;

public class DevPoint {
    private String name;
    private String lat;
    private String lon;

    public String getName(){
        return this.name;
    }

    public void setName(String arg){
        this.name = arg;
    }

    public String getLat(){
        return this.lat;
    }

    public void setLat(String arg){
        this.lat = arg;
    }

    public String getLon(){
        return this.lon;
    }

    public void setLon(String arg){
        this.lon = arg;
    }
}