package net.scnetwork.positioner.tracs.Xml2Bean;

import net.scnetwork.positioner.tracs.gpx.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Gpx2Bean {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public Gpx getBean(Document arg){
        Gpx result = new Gpx();

        Node metadata = arg.getElementsByTagName("metadata").item(0);
        NodeList metadataList = metadata.getChildNodes();
        for (int i = 0; i < metadataList.getLength(); i++){
            Node mtNode = metadataList.item(i);
            String name = mtNode.getNodeName();
            switch (name){
                case "name":
                    result.setName(mtNode.getTextContent());
                    break;
                case "desc":
                    result.setDesc(mtNode.getTextContent());
                    break;
                case "time":
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    try {
                        result.setTime(sdf.parse(mtNode.getTextContent()));
                    } catch (ParseException e) {
                        result.setTime(new Date());
                    }
                    break;
                case "bounds":
                    Bounds bounds = new Bounds();
                    NamedNodeMap map = mtNode.getAttributes();
                    bounds.setMaxlat(new BigDecimal(map.getNamedItem("maxlat").getTextContent()));
                    bounds.setMaxlon(new BigDecimal(map.getNamedItem("maxlon").getTextContent()));
                    bounds.setMinlat(new BigDecimal(map.getNamedItem("minlat").getTextContent()));
                    bounds.setMinlon(new BigDecimal(map.getNamedItem("minlon").getTextContent()));
                    result.setBounds(bounds);
                    break;
            }
        }

        Node trkN = arg.getElementsByTagName("trk").item(0);
        NodeList trkList = trkN.getChildNodes();
        Trk trk = new Trk();
        for (int i = 0; i < trkList.getLength(); i++){
            Node trkNode = trkList.item(i);
            String name = trkNode.getNodeName();
            switch (name){
                case "name":
                    trk.setName(trkNode.getTextContent());
                    break;
                case "desc":
                    trk.setDesc(trkNode.getTextContent());
                    break;
                case "type":
                    trk.setType(trkNode.getTextContent());
                    break;
            }
        }

        NodeList trkSecN = arg.getElementsByTagName("trkseg");
        ArrayList<TrSec> arrayTrSec = new ArrayList<>();
        for (int i = 0; i < trkSecN.getLength(); i++){
            Node nodeTrSec = trkSecN.item(i);
            ArrayList<TrPkt> arrayTrPkt = new ArrayList<>();
            NodeList nodeListTrkPoint = nodeTrSec.getChildNodes();
            for (int k = 0; k < nodeListTrkPoint.getLength(); k++){
                try {
                    TrPkt trPkt = new TrPkt();

                    Node nodeTrPkt = nodeListTrkPoint.item(k);
                    NamedNodeMap tpMap = nodeTrPkt.getAttributes();
                    trPkt.setLat(new BigDecimal(tpMap.getNamedItem("lat").getTextContent()));
                    trPkt.setLon(new BigDecimal(tpMap.getNamedItem("lon").getTextContent()));

                    NodeList chNodeTrPkt = nodeTrPkt.getChildNodes();
                    for (int l = 0; l < chNodeTrPkt.getLength(); l++) {
                        Node chN = chNodeTrPkt.item(l);
                        String name = chN.getNodeName();
                        switch (name) {
                            case "ele":
                                trPkt.setEle(new BigDecimal(chN.getTextContent()));
                                break;
                            case "time":
                                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                                try {
                                    trPkt.setTime(sdf.parse(chN.getTextContent()));
                                } catch (ParseException e) {
                                    trPkt.setTime(new Date());
                                }
                                break;
                        }
                    }

                    arrayTrPkt.add(trPkt);
                } catch (Exception ignored) {}
            }
            TrSec trSec = new TrSec(arrayTrPkt);
            arrayTrSec.add(trSec);
        }
        trk.setTrSecs(arrayTrSec);
        result.setTrk(trk);

        return result;
    }
}
