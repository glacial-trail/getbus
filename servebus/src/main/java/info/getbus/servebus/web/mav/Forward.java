package info.getbus.servebus.web.mav;

import org.springframework.web.servlet.ModelAndView;

public class Forward  extends Redirect {

    public Forward(String basePath, String path) {
        super(basePath, path);
    }

    public ModelAndView forward() {
        return redirect();
    }

    @Override
    protected String actionPrefix() {
        return "forward:";
    }
}
