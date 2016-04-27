package net.scnetwork.positioner.bean.Gpx;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name = "metadata")
public class Metadata {
    private String name;
    private String desc;
    private Date time;
    private Bounds bounds;

    public String getName() {
        return name;
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    @XmlElement(name = "desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getTime() {
        return time;
    }

    @XmlElement(name = "time")
    public void setTime(Date time) {
        this.time = time;
    }

    public Bounds getBounds() {
        return bounds;
    }

    @XmlElement
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
}