package net.scnetwork.positioner.bean;

public class UserSettings {
    private String username;
    private String password;
    private String group;

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

    public String getGroup(){
        return this.group;
    }

    public void setGroup(String arg){
        this.group = arg;
    }
}
