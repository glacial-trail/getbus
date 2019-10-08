package info.getbus.servebus.web.views;

import com.google.common.base.Joiner;
import info.getbus.servebus.store.Store;
import info.getbus.servebus.store.StoreService;
import info.getbus.servebus.web.dto.store.StoreDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public class CarrierDashboardView extends ModelAndView {

    private static final Joiner joiner = Joiner.on("").skipNulls();

    private String innerViewPrefix;
    private String innerViewSuffix;
    private String innerViewModelParamName;

    @Autowired
    private StoreService storeService;

    public void setInnerViewPrefix(String innerViewPrefix) {
        this.innerViewPrefix = innerViewPrefix;
    }

    public void setInnerViewSuffix(String innerViewSuffix) {
        this.innerViewSuffix = innerViewSuffix;
    }

    public void setInnerViewModelParamName(String innerViewModelParamName) {
        this.innerViewModelParamName = innerViewModelParamName;
    }

    public ModelAndView storeDetails(StoreDetailsDTO store) {
        addObject("storeDetails", store);
        return page("store_details/store_details");
    }

    public ModelAndView page(String name) {
        addObject(innerViewModelParamName, joiner.join(innerViewPrefix, name, innerViewSuffix));
        Store store = storeService.get();
        if (store != null) {
            addObject("store", store);
        }
        return this;
    }
}
