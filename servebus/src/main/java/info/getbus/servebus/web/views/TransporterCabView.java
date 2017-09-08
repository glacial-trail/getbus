package info.getbus.servebus.web.views;

import org.springframework.web.servlet.ModelAndView;

public /*abstract*/ class TransporterCabView {
    public static final String CAB_URL = "/tr";
    public static final String DEFAULT_VIEW_SUFFIX = ".ftl";
    public static final String TRANSPORTER_CAB_PAGE_NAME = "cabPageName";
    public static final String TRANSPORTER_CAB_VIEW = "tr/cab";

    private String viewSuffix = DEFAULT_VIEW_SUFFIX;
    private String cabPage;

    public TransporterCabView() {
    }

    public TransporterCabView(String cabPage) {
        this.cabPage = cabPage;
    }

    protected TransporterCabView page(String cabPage) {
        this.cabPage = cabPage;
        return this;
    }

    public ModelAndView build() {
        ModelAndView mav = new ModelAndView(TRANSPORTER_CAB_VIEW);
        mav.addObject(TRANSPORTER_CAB_PAGE_NAME, getCabPage() + viewSuffix);
        return mav;
    }

    protected String getCabPage() {
        return cabPage;
    }

    public String getViewSuffix() {
        return viewSuffix;
    }

    public void setViewSuffix(String viewSuffix) {
        this.viewSuffix = viewSuffix;
    }
}
