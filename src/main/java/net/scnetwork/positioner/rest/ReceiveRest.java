package net.scnetwork.positioner.rest;

import com.topografix.gpx._1._1.GpxType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class ReceiveRest {
    @RequestMapping("/rest/put/test")
    public String EchoTest(@RequestParam(value = "name", defaultValue = "test") String name){
        return name;
    }

    @RequestMapping("/rest/put/data/{id}/{name}")
    public String ReceiveGetData(@PathVariable(value = "id") String id,
                                 @RequestParam(value = "lat") BigDecimal lat,
                                 @RequestParam(value = "lon") BigDecimal lon,
                                 @RequestParam(value = "alt") BigDecimal alt,
                                 @RequestParam(value = "name", defaultValue = "origin") String name){
        return "success";
    }
}
