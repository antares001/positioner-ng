package net.scnetwork.positioner.utils;

import org.apache.velocity.VelocityContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class HiddenVariable extends VelocityContext{
    private static HiddenVariable instance;
    private Map<String,String> map = new HashMap<String, String>();
    private static String currentContext;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("[dd-MM-yy hh:mm:ss.ssssss]");

    public static HiddenVariable getInstance(){
        if (instance == null)
            instance = new HiddenVariable();
        return instance;
    }

    public static HiddenVariable getInstance(String arg){
        if (instance == null)
            instance = new HiddenVariable();
        currentContext = arg;
        return instance;
    }

    public void pullDown(String name, String value){
        System.out.println("[" + sdf.format(new Date()) + "]ADD: " + name + " => " + value);
        map.put(currentContext.concat(name), value);
    }

    public String pullUp(String name){
        System.out.println("[" + sdf.format(new Date()) + "]GET: " + name + " <= " + map.get(currentContext.concat(name)));
        return map.get(currentContext.concat(name));
    }

    public void pullDel(String name){
        System.out.println("[" + sdf.format(new Date()) + "]DELETE: " + name + " >< " + map.get(currentContext.concat(name)));
        map.remove(currentContext.concat(name));
    }

    public void ClearAllVars(String sessionID){
        HashSet removeMap = new HashSet();
        for (Object key : map.keySet()) {
            String idKey = (String) key;
            try {
                if (sessionID.equals(idKey.substring(0, sessionID.length()))) {
                    removeMap.add(key);
                }
            } catch (Exception ignored) {}
        }
        map.keySet().removeAll(removeMap);
    }
}
