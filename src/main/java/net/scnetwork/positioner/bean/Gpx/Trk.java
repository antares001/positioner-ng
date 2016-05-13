package net.scnetwork.positioner.bean.Gpx;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "trk")
public class Trk {
    private String name;
    private String type;
    private String desc;
    private ArrayList<TrackSegment> trackSegments;

    public String getName() {
        return name;
    }

    @XmlElement(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    @XmlElement(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    @XmlElement(name = "desc")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<TrackSegment> getTrackSegments() {
        return trackSegments;
    }

    @XmlElement(name = "trkseg")
    public void setTrackSegments(ArrayList<TrackSegment> trackSegments) {
        this.trackSegments = trackSegments;
    }
}
