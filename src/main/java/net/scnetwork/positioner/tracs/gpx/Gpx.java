package net.scnetwork.positioner.tracs.gpx;

import java.util.Date;

public class Gpx {
    private String name;
    private String desc;
    private Date time;
    private Bounds bounds;
    private Trk trk;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public Trk getTrk() {
        return trk;
    }

    public void setTrk(Trk trk) {
        this.trk = trk;
    }
}
