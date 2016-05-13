package net.scnetwork.positioner.bean.Gpx;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "gpx", namespace = "http://www.topografix.com/GPX/1/1")
public class Gpx {
    private Metadata metadata;
    private ArrayList<Trk> trk;

    private String xmlns;
    private String xmlnsGpxtpx;
    private String creator;
    private String version;
    private String xmlnsXsi;
    private String xsiSchemaLocation;

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

    @XmlElement(name = "trk")
    public void setTracks(ArrayList<Trk> tracks) {
        this.trk = tracks;
    }

    public String getXmlns() {
        return xmlns;
    }

    @XmlAttribute(name="xmlns")
    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getXmlnsGpxtpx() {
        return xmlnsGpxtpx;
    }

    @XmlAttribute(name = "xmlns:gpxtpx")
    public void setXmlnsGpxtpx(String xmlnsGpxtpx) {
        this.xmlnsGpxtpx = xmlnsGpxtpx;
    }

    public String getCreator() {
        return creator;
    }

    @XmlAttribute(name = "creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVersion() {
        return version;
    }

    @XmlAttribute(name = "version")
    public void setVersion(String version) {
        this.version = version;
    }

    public String getXmlnsXsi() {
        return xmlnsXsi;
    }

    @XmlAttribute(name = "xmlns:xsi")
    public void setXmlnsXsi(String xmlnsXsi) {
        this.xmlnsXsi = xmlnsXsi;
    }

    public String getXsiSchemaLocation() {
        return xsiSchemaLocation;
    }

    @XmlAttribute(name = "xsi:schemaLocation")
    public void setXsiSchemaLocation(String xsiSchemaLocation) {
        this.xsiSchemaLocation = xsiSchemaLocation;
    }
}
