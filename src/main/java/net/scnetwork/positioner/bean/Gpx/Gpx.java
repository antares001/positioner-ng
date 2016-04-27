package net.scnetwork.positioner.bean.Gpx;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "gpx")
public class Gpx {
    private Metadata metadata;
    private ArrayList<Trk> trk;

    public Metadata getMetadata() {
        return metadata;
    }

    @XmlElement
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public ArrayList<Trk> getTracks() {
        return trk;
    }

    @XmlElement
    public void setTracks(ArrayList<Trk> tracks) {
        this.trk = tracks;
    }
}
