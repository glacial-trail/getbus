package info.getbus.servebus.web.views;

import org.springframework.web.servlet.ModelAndView;

public class TransporterCabView extends ModelAndView {
    public static final String DEFAULT_VIEW_SUFFIX = ".ftl";
    public static final String TRANSPORTER_CAB_PAGE_NAME = "cabPageName";

    private String viewSuffix = DEFAULT_VIEW_SUFFIX;

    public TransporterCabView(String cabPage) {
        super("tr/cab");
        addObject(TRANSPORTER_CAB_PAGE_NAME, cabPage + viewSuffix);
    }

    public String getViewSuffix() {
        return viewSuffix;
    }

    public void setViewSuffix(String viewSuffix) {
        this.viewSuffix = viewSuffix;
    }
}
