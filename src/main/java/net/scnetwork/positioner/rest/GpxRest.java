package net.scnetwork.positioner.rest;

import com.topografix.gpx._1._1.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@RestController
public class GpxRest {
    @RequestMapping(value = "/rest/gpx/test", method = RequestMethod.GET/*, produces = MediaType.APPLICATION_XML_VALUE*/)
    public GpxType SampleGpx() throws DatatypeConfigurationException {
        ObjectFactory factory = new ObjectFactory();

        PersonType personType = factory.createPersonType();
        personType.setName("Nikolai Bloshkin");

        EmailType email = factory.createEmailType();
        email.setDomain("gmail.com");
        email.setId("bloshkin.nikolai");
        personType.setEmail(email);

        LinkType link = factory.createLinkType();
        link.setHref("github.com/antares001/positioner-ng");
        link.setType("https");
        link.setText("link");
        personType.setLink(link);

        ExtensionsType extensions = factory.createExtensionsType();
        List<Object> any = new ArrayList<>();
        any.add("gpx");
        extensions.setAny(any);

        GregorianCalendar c = new GregorianCalendar();
        c.setGregorianChange(new Date());
        XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        CopyrightType copyright = factory.createCopyrightType();
        copyright.setAuthor("Nikolai Bloshkin");
        copyright.setLicense("GPL3");
        copyright.setYear(date);

        BoundsType bounds = factory.createBoundsType();
        bounds.setMaxlat(new BigDecimal(1000));
        bounds.setMaxlon(new BigDecimal(1000));
        bounds.setMinlat(new BigDecimal(0));
        bounds.setMinlon(new BigDecimal(0));

        MetadataType metadataType = factory.createMetadataType();
        metadataType.setName("positioner");
        metadataType.setKeywords("some");
        metadataType.setDesc("description");
        metadataType.setAuthor(personType);
        metadataType.setExtensions(extensions);
        metadataType.setCopyright(copyright);
        metadataType.setBounds(bounds);

        List<RteType> retList = new ArrayList<>();
        RteType rteType = factory.createRteType();
        rteType.setType("type");
        rteType.setName("rte");
        rteType.setCmt("cmt");
        rteType.setDesc("desc");
        rteType.setExtensions(extensions);
        rteType.setNumber(new BigInteger("1"));
        rteType.setSrc("src");
        retList.add(rteType);

        List<WptType> wptTypeList = new ArrayList<>();
        WptType wptType = new WptType();
        wptType.setVdop(new BigDecimal(11));
        wptType.setSrc("src");
        wptType.setType("type");
        wptType.setExtensions(extensions);
        wptType.setDesc("desc");
        wptType.setCmt("cmt");
        wptType.setName("name");
        wptType.setAgeofdgpsdata(new BigDecimal(12));
        wptType.setDgpsid(1);
        wptType.setEle(new BigDecimal(13));
        wptType.setFix("fix");
        wptType.setDgpsid(2);
        wptType.setGeoidheight(new BigDecimal(3));
        wptType.setHdop(new BigDecimal(4));
        wptType.setLat(new BigDecimal(5));
        wptType.setLon(new BigDecimal(6));
        wptType.setMagvar(new BigDecimal(7));
        wptType.setPdop(new BigDecimal(8));
        wptType.setSat(new BigInteger("5"));
        wptType.setTime(date);
        wptType.setSym("sym");
        wptTypeList.add(wptType);

        GpxType gpx = factory.createGpxType();
        gpx.setCreator("sun");
        gpx.setExtensions(extensions);
        gpx.setMetadata(metadataType);
        gpx.setVersion("0.1");
        gpx.setRte(retList);
        gpx.setWpt(wptTypeList);

        return gpx;
    }
}
