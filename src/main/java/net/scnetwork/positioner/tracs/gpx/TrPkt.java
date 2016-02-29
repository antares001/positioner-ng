package net.scnetwork.positioner.tracs.gpx;

import java.math.BigDecimal;
import java.util.Date;

public class TrPkt {
    private BigDecimal lat;
    private BigDecimal lon;
    private BigDecimal ele;
    private Date time;

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public BigDecimal getEle() {
        return ele;
    }

    public void setEle(BigDecimal ele) {
        this.ele = ele;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
