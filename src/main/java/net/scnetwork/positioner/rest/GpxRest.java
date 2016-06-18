package net.scnetwork.positioner.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rest/gpx")
public class GpxRest {
    @RequestMapping(value = "/echo", method = RequestMethod.GET)
    public String  SampleGpx(@RequestParam(value = "name", defaultValue = "-") String name) {
        return name;
    }
}
