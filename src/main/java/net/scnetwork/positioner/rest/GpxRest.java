package net.scnetwork.positioner.rest;

import com.topografix.gpx._1._1.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GpxRest {
    @RequestMapping(value = "/rest/gpx/test", method = RequestMethod.GET/*, produces = MediaType.APPLICATION_XML_VALUE*/)
    public GpxType SampleGpx(){
        ObjectFactory factory = new ObjectFactory();
        GpxType gpx = factory.createGpxType();

        gpx.setCreator("sun");
        gpx.setMetadata(factory.createMetadataType());
        gpx.setExtensions(factory.createExtensionsType());
        gpx.setVersion("0.1");

        return gpx;
    }
}
