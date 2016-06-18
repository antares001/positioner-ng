package net.scnetwork.positioner.rest;

import com.topografix.gpx._1._1.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GpxRest {
    @RequestMapping(value = "/rest/gpx/test", method = RequestMethod.GET/*, produces = MediaType.APPLICATION_XML_VALUE*/)
    public ObjectFactory SampleGpx(){
        ObjectFactory gpx = new ObjectFactory();

        GpxType gpxType = new GpxType();
        gpxType.setCreator("positioner-ng");
        gpxType.setVersion("0.1");

        gpxType.setExtensions(new ExtensionsType());

        MetadataType metadataType = new MetadataType();
        metadataType.setName("some_name");
        metadataType.setDesc("positioner system");
        metadataType.setKeywords("keywords");

        PersonType personType = new PersonType();
        personType.setName("Nikolai Bloshkin");

        EmailType emailType = new EmailType();
        emailType.setId("bloshkin.nikolai");
        emailType.setDomain("gmail.com");
        personType.setEmail(emailType);

        LinkType linkType = new LinkType();
        linkType.setHref("github.com/antares001/positioner-ng");
        linkType.setText("positioner-ng");
        linkType.setType("https");
        personType.setLink(linkType);
        metadataType.setAuthor(personType);
        gpxType.setMetadata(metadataType);
        gpx.createGpx(gpxType);

        return gpx;
    }
}
