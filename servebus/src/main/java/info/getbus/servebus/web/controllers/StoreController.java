package info.getbus.servebus.web.controllers;

import info.getbus.servebus.store.Store;
import info.getbus.servebus.store.StoreService;
import info.getbus.servebus.web.dto.store.StoreDetailsDTO;
import info.getbus.servebus.web.mav.Redirect;
import info.getbus.servebus.web.views.CarrierDashboardView;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/t/store/")
@RequiredArgsConstructor
public class StoreController {
    private final ModelMapper modelMapper;
    private final StoreService storeService;

    @Lookup("carrierDashboardView")
    public CarrierDashboardView view() { return null; }

    @GetMapping("/{id}")
    public ModelAndView get(@PathVariable("id") long id)  {
        Store store = storeService.get();
        if (id != store.getId()) {
            throw new RuntimeException("Wrong store ID"); // TODO implement security
        }
        return view()
                .storeDetails(
                        modelMapper.map(store, StoreDetailsDTO.class)
                );
    }

    @GetMapping("/")
    public ModelAndView getNew() {
        if(null == storeService.get()) {
            return view().storeDetails(new StoreDetailsDTO());
        }
        throw new RuntimeException("Store already exist");
    }

    @PostMapping("/save")
    public ModelAndView simpleSave(@ModelAttribute("storeDetails") @Validated StoreDetailsDTO dto,
                                   BindingResult errors) {
        if (errors.hasErrors()) {
            return view().storeDetails(dto);
        }

        Store store = modelMapper.map(dto, Store.class);
        storeService.saveStore(store);

        return path("/" + store.getId()).redirect();
    }

    private Redirect path(String path) {
        return new Redirect("/t/store/", path);
    }
}
