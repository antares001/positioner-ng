package net.scnetwork.positioner;

import net.scnetwork.positioner.tracs.Xml2Bean.Gpx2Bean;
import net.scnetwork.positioner.tracs.gpx.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GpxTest {
    private Gpx2Bean gpx2Bean = new Gpx2Bean();

    @Test
    public void GpxReadFile(){
        File file = new File("src/test/java/net/scnetwork/positioner/gpx.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.normalizeDocument();

            Gpx gpx = gpx2Bean.getBean(document);
            System.out.println("NAME: " + gpx.getName());
            System.out.println("DESCRIPTION: " + gpx.getDesc());
            System.out.println("TIME: " + (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")).format(gpx.getTime()));
            System.out.println("========================================================");
            Bounds bounds = gpx.getBounds();
            System.out.println("LAT MAXIMUM: " + bounds.getMaxlat());
            System.out.println("LAT MINIMUM: " + bounds.getMinlat());
            System.out.println("LON MAXIMUM: " + bounds.getMaxlon());
            System.out.println("LON MINIMUM: " + bounds.getMinlon());
            System.out.println("=========================================================");
            Trk trk = gpx.getTrk();
            System.out.println("TRACK NAME: " + trk.getName());
            System.out.println("TRACK DESCRIPTION: " + trk.getDesc());
            System.out.println("=========================================================");
            ArrayList<TrSec> arrayListTrSec = trk.getTrSecs();
            System.out.println("SEGMENTS: " + arrayListTrSec.size());
            ArrayList<TrPkt> trPktArrayList = arrayListTrSec.get(0).getTrPkts();
            System.out.println("POINTS: " + trPktArrayList.size());

            for(int i = 0; i < trPktArrayList.size(); i++){
                TrPkt trPkt = trPktArrayList.get(i);
                System.out.println(i + ". " + trPkt.getLat() + " - " + trPkt.getLon() + " - " + trPkt.getEle() + ". " + trPkt.getTime());
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
