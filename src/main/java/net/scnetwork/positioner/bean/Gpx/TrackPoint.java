package net.scnetwork.positioner.bean.Gpx;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

@XmlRootElement(name = "trkpt")
public class TrackPoint {
    private BigDecimal lat;
    private BigDecimal lon;
    private BigDecimal ele;
    private Date time;

    public BigDecimal getLat() {
        return lat;
    }

    @XmlAttribute(name = "lat")
    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    @XmlAttribute(name = "lon")
    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public BigDecimal getEle() {
        return ele;
    }

    @XmlElement(name = "ele")
    public void setEle(BigDecimal ele) {
        this.ele = ele;
    }

    public Date getTime() {
        return time;
    }

    @XmlElement(name = "time")
    public void setTime(Date time) {
        this.time = time;
    }
}
