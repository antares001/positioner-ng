package net.scnetwork.positioner.rest;

import com.google.earth.kml._2.KmlType;
import com.topografix.gpx._1._1.GpxType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@RestController
@RequestMapping("/rest/put")
public class ReceiveRest {
    @RequestMapping("/echo")
    public String getTest(@RequestParam(value = "name", defaultValue = "test") String name){
        return name;
    }

    @RequestMapping("/data/{id}/{name}")
    public String insertData(@PathVariable(value = "id") String id,
                                 @RequestParam(value = "lat") BigDecimal lat,
                                 @RequestParam(value = "lon") BigDecimal lon,
                                 @RequestParam(value = "alt") BigDecimal alt,
                                 @RequestParam(value = "name", defaultValue = "origin") String name){
        return "success";
    }

    @RequestMapping(value = "/data/gpx", method = RequestMethod.POST)
    public String insertGpx(@RequestParam(value = "gpx") GpxType type){
        return "success";
    }

    @RequestMapping(value = "/data/kml", method = RequestMethod.POST)
    public String insertKml(@RequestParam(value = "kml")KmlType type){
        return "success";
    }
}
