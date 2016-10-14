package info.getbus.servebus.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@Controller("/")
public class TransporterMainController {

    @RequestMapping(path = {"/", "/logon/"}, method = RequestMethod.GET)
    public String logon() {
        return "logon";
    }
}
