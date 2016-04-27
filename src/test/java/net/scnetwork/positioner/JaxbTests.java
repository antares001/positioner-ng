package net.scnetwork.positioner;

import net.scnetwork.positioner.bean.Gpx.*;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class JaxbTests {

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
        point0.setLat(new BigDecimal(10));
        point0.setLon(new BigDecimal(20));
        point0.setEle(new BigDecimal(30));
        point0.setTime(new Date());
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
}
