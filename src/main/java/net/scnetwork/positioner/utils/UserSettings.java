package net.scnetwork.positioner.utils;

import net.scnetwork.positioner.bean.BeanSettings;

public class UserSettings {
    private BeanSettings settings = new BeanSettings();

    public UserSettings(){}

    public UserSettings(String session){
        settings.setSession(session);
    }

    public UserSettings(String username, String password, String session){
        settings.setUsername(username);
        settings.setPassword(password);
        settings.setSession(session);
    }

    static {
        //TODO: initialise static method
    }
}
