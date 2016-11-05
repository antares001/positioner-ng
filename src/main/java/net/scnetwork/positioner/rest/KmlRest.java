package net.scnetwork.positioner.rest;

import com.google.earth.kml._2.KmlType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/kml")
public class KmlRest {
    @RequestMapping("/echo")
    public String getTest(@RequestParam(value = "name", defaultValue = "test") String name){
        return name;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public KmlType getKml(@PathVariable(value = "id") String id){
        return null;
    }
}
