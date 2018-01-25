package info.getbus.servebus.web.mav;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.function.Consumer;

public class Redirect {

    private String basePath;
    private String path;
    private Consumer<UriComponentsBuilder> uriAdjuster;

    public Redirect(String basePath, String path) {
        this.basePath = basePath;
        this.path = path;
    }

    public Redirect with(Consumer<UriComponentsBuilder> uriAdjuster) {
        this.uriAdjuster = uriAdjuster;
        return this;
    }

    public ModelAndView redirect() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromPath(basePath)
                .path(path);
        if (null != uriAdjuster) {
            uriAdjuster.accept(uriComponentsBuilder);
        }
        return new ModelAndView("redirect:" + uriComponentsBuilder.toUriString());
    }
}
