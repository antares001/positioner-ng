package net.scnetwork.positioner.tracs.gpx;

import java.util.ArrayList;

public class TrSec {
    private ArrayList<TrPkt> trPkts;

    public TrSec(){}

    public TrSec(ArrayList<TrPkt> args) {
        this.trPkts = args;
    }

    public ArrayList<TrPkt> getTrPkts() {
        return trPkts;
    }

    public void setTrPkts(ArrayList<TrPkt> trPkts) {
        this.trPkts = trPkts;
    }
}
