package net.scnetwork.positioner.tracs.gpx;

import java.util.ArrayList;

public class Trk {
    private String name;
    private String desc;
    private String type;
    private ArrayList<TrSec> trSecs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<TrSec> getTrSecs() {
        return trSecs;
    }

    public void setTrSecs(ArrayList<TrSec> trSecs) {
        this.trSecs = trSecs;
    }
}
