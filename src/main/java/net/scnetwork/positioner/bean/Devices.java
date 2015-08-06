package net.scnetwork.positioner.bean;

public class Devices {
    private String id;
    private String name;
    private String uniq;
    private String last;

    public String getId(){
        return this.id;
    }

    public void setId(String arg){
        this.id = arg;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String arg){
        this.name = arg;
    }

    public String getUniq(){
        return this.uniq;
    }

    public void setUniq(String arg){
        this.uniq = arg;
    }

    public String getLast(){
        return this.last;
    }

    public void setLast(String arg){
        this.last = arg;
    }
}
