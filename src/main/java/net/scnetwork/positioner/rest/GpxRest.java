package net.scnetwork.positioner.rest;

import com.topografix.gpx._1._1.GpxType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rest/gpx")
public class GpxRest {
    @RequestMapping(value = "/echo", method = RequestMethod.GET)
    public String  getTest(@RequestParam(value = "name", defaultValue = "-") String name) {
        return name;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public GpxType getGpx(@PathVariable(value = "id") String id){
        return null;
    }
}
