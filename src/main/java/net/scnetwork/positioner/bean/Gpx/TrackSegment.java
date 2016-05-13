package net.scnetwork.positioner.bean.Gpx;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "trkseg")
public class TrackSegment {
    private ArrayList<TrackPoint> trackPoints;

    public ArrayList<TrackPoint> getTrackPoints() {
        return trackPoints;
    }

    @XmlElement(name = "trkpt")
    public void setTrackPoints(ArrayList<TrackPoint> trackPoints) {
        this.trackPoints = trackPoints;
    }
}
