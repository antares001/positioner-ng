package net.scnetwork.positioner.utils;

import net.scnetwork.positioner.domain.BeanSettings;

public class UserSettings {
    private static BeanSettings settings = new BeanSettings();
    private static String context;

    public UserSettings(){}

    public UserSettings(String session){
        settings.setSession(session);
    }

    public UserSettings(BeanSettings arg) {
        settings = arg;
    }

    public UserSettings(String username, String password, String session){
        settings.setUsername(username);
        settings.setPassword(password);
        settings.setSession(session);
    }

    static {
        settings = new BeanSettings();
    }
}
