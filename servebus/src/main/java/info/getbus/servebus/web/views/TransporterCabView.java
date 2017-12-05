package info.getbus.servebus.web.views;

import org.springframework.web.servlet.ModelAndView;

public /*abstract*/ class TransporterCabView {
    public static final String CAB_URL = "/t";
    public static final String FTL_VIEW_SUFFIX = ".ftl";

    private String viewSuffix = FTL_VIEW_SUFFIX;
//    private String viewSuffix;
    private String cabPage;

    protected ModelAndView mav = new ModelAndView("tr/cab");

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
        mav.addObject("cabPageName", getCabPage() + viewSuffix);
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
