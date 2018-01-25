package info.getbus.servebus.web.views;

import com.google.common.base.Joiner;
import org.springframework.web.servlet.ModelAndView;

public class TransporterCabView extends ModelAndView {

    private static final Joiner joiner = Joiner.on("").skipNulls();

    private String innerViewPrefix;
    private String innerViewSuffix;
    private String innerViewModelParamName;

    public void setInnerViewPrefix(String innerViewPrefix) {
        this.innerViewPrefix = innerViewPrefix;
    }

    public void setInnerViewSuffix(String innerViewSuffix) {
        this.innerViewSuffix = innerViewSuffix;
    }

    public void setInnerViewModelParamName(String innerViewModelParamName) {
        this.innerViewModelParamName = innerViewModelParamName;
    }

    public ModelAndView page(String name) {
        addObject(innerViewModelParamName, joiner.join(innerViewPrefix, name, innerViewSuffix));
        return this;
    }
}
