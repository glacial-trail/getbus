package info.getbus.servebus.web.controllers;

import info.getbus.servebus.web.views.CarrierDashboardView;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/t/")
public class TransporterMainController {

    @Lookup("carrierDashboardView")
    public CarrierDashboardView view() {
        return null;
    }

    @GetMapping("/")
    public ModelAndView welcome() {
        return view().page("welcome");
    }

    @GetMapping("/voyages")
    public ModelAndView listVoyages() {
        return view().page("voyage-list");
    }

    @GetMapping("/about")
    public ModelAndView about() {
        return view().page("about");
    }
}
