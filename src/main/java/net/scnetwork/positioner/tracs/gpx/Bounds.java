package net.scnetwork.positioner.tracs.gpx;

import java.math.BigDecimal;

public class Bounds {
    private BigDecimal maxlat;
    private BigDecimal maxlon;
    private BigDecimal minlat;
    private BigDecimal minlon;

    public Bounds(){
        this.maxlat = new BigDecimal(0);
        this.maxlon = new BigDecimal(0);
        this.minlat = new BigDecimal(0);
        this.minlon = new BigDecimal(0);
    }

    public Bounds(BigDecimal maxlat, BigDecimal maxlon, BigDecimal minlat, BigDecimal minlon){
        this.maxlat = maxlat;
        this.maxlon = maxlon;
        this.minlat = minlat;
        this.minlon = minlon;
    }

    public BigDecimal getMaxlat() {
        return maxlat;
    }

    public void setMaxlat(BigDecimal maxlat) {
        this.maxlat = maxlat;
    }

    public BigDecimal getMaxlon() {
        return maxlon;
    }

    public void setMaxlon(BigDecimal maxlon) {
        this.maxlon = maxlon;
    }

    public BigDecimal getMinlat() {
        return minlat;
    }

    public void setMinlat(BigDecimal minlat) {
        this.minlat = minlat;
    }

    public BigDecimal getMinlon() {
        return minlon;
    }

    public void setMinlon(BigDecimal minlon) {
        this.minlon = minlon;
    }
}
