package net.scnetwork.positioner.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceiveRest {
    @RequestMapping("/rest/put/test")
    public String EchoTest(@RequestParam(value = "name", defaultValue = "test") String name){
        return name;
    }
}
