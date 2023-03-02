package info.getbus.servebus.web.mav;

import org.springframework.web.util.UriComponentsBuilder;

public class CustomUriComponentsBuilder extends UriComponentsBuilder {
    public UriComponentsBuilder path(Object path) {
        return path(path.toString());
    }
}
