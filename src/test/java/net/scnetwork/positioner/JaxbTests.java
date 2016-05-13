package net.scnetwork.positioner;

import net.scnetwork.positioner.bean.Gpx.*;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class JaxbTests {
    private static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<gpx creator=\"OruxMaps v.6.0.1\" version=\"1.1\" xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:gpxtpx=\"http://www.garmin.com/xmlschemas/TrackPointExtension/v1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">" +
            "<metadata>" +
            "<bounds maxlat=\"10\" maxlon=\"15\" minlat=\"5\" minlon=\"6\"/>" +
            "<desc>testDescriptions</desc>" +
            "<name>testName</name>" +
            "<time>2016-05-13T21:56:24.858+03:00</time>" +
            "</metadata>" +
            "<trk>" +
            "<desc>trackTestDescription</desc>" +
            "<name>trackTestName</name>" +
            "<trkseg>" +
            "<trkpt lat=\"1\" lon=\"2\">" +
            "<ele>3</ele>" +
            "<time>2016-05-13T21:56:24.880+03:00</time>" +
            "</trkpt>" +
            "<trkpt lat=\"10\" lon=\"20\">" +
            "<ele>30</ele>" +
            "<time>2016-05-13T21:56:24.880+03:00</time>" +
            "</trkpt></trkseg>" +
            "<type>trackTestType</type>" +
            "</trk>" +
            "</gpx>";

    @Test
    public void MetadataTest(){
        Metadata metadata = new Metadata();
        metadata.setName("testName");
        metadata.setDesc("testDescriptions");
        metadata.setTime(new Date());

        Bounds bounds = new Bounds();
        bounds.setMaxlat(new BigDecimal(10));
        bounds.setMaxlon(new BigDecimal(15));
        bounds.setMinlat(new BigDecimal(5));
        bounds.setMinlon(new BigDecimal(6));
        metadata.setBounds(bounds);

        try {
            JAXBContext context = JAXBContext.newInstance(Metadata.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(metadata, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void GpxCreateTest(){
        Gpx gpx = new Gpx();

        gpx.setXmlns("http://www.topografix.com/GPX/1/1");
        gpx.setXmlnsGpxtpx("http://www.garmin.com/xmlschemas/TrackPointExtension/v1");
        gpx.setCreator("OruxMaps v.6.0.1");
        gpx.setVersion("1.1");
        gpx.setXmlnsXsi("http://www.w3.org/2001/XMLSchema-instance");
        gpx.setXsiSchemaLocation("http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd");

        Metadata metadata = new Metadata();
        metadata.setName("testName");
        metadata.setDesc("testDescriptions");
        metadata.setTime(new Date());

        Bounds bounds = new Bounds();
        bounds.setMaxlat(new BigDecimal(10));
        bounds.setMaxlon(new BigDecimal(15));
        bounds.setMinlat(new BigDecimal(5));
        bounds.setMinlon(new BigDecimal(6));
        metadata.setBounds(bounds);

        gpx.setMetadata(metadata);

        ArrayList<Trk> tracks = new ArrayList<>();
        Trk track = new Trk();
        track.setName("trackTestName");
        track.setDesc("trackTestDescription");
        track.setType("trackTestType");

        ArrayList<TrackSegment> trackSegments = new ArrayList<>();
        TrackSegment trackSegment = new TrackSegment();

        ArrayList<TrackPoint> trackPoints = new ArrayList<>();

        TrackPoint point0 = new TrackPoint();
        point0.setLat(new BigDecimal(1));
        point0.setLon(new BigDecimal(2));
        point0.setEle(new BigDecimal(3));
        point0.setTime(new Date());
        trackPoints.add(point0);

        TrackPoint point1 = new TrackPoint();
        point1.setLat(new BigDecimal(10));
        point1.setLon(new BigDecimal(20));
        point1.setEle(new BigDecimal(30));
        point1.setTime(new Date());
        trackPoints.add(point1);

        trackSegment.setTrackPoints(trackPoints);
        trackSegments.add(trackSegment);

        track.setTrackSegments(trackSegments);
        tracks.add(track);

        gpx.setTracks(tracks);

        try {
            JAXBContext context = JAXBContext.newInstance(Gpx.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(gpx, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void GpxXml2JavaTest(){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Gpx.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Gpx gpx = (Gpx) unmarshaller.unmarshal(new InputSource(new StringReader(XML)));

            System.out.println("Creator: " + gpx.getCreator());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
