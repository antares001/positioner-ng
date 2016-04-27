package net.scnetwork.positioner.bean.Gpx;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "bounds")
public class Bounds {
    private BigDecimal maxlat;
    private BigDecimal maxlon;
    private BigDecimal minlat;
    private BigDecimal minlon;

    public BigDecimal getMaxlat(){
        return maxlat;
    }

    @XmlAttribute(name = "maxlat")
    public void setMaxlat(BigDecimal maxlat) {
        this.maxlat = maxlat;
    }

    public BigDecimal getMaxlon() {
        return maxlon;
    }

    @XmlAttribute(name = "maxlon")
    public void setMaxlon(BigDecimal maxlon) {
        this.maxlon = maxlon;
    }

    public BigDecimal getMinlat() {
        return minlat;
    }

    @XmlAttribute(name = "minlat")
    public void setMinlat(BigDecimal minlat) {
        this.minlat = minlat;
    }

    public BigDecimal getMinlon() {
        return minlon;
    }

    @XmlAttribute(name = "minlon")
    public void setMinlon(BigDecimal minlon) {
        this.minlon = minlon;
    }
}
