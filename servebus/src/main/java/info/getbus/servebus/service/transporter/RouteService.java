package info.getbus.servebus.service.transporter;

import info.getbus.servebus.model.route.CompactRoute;

import java.util.List;

public interface RouteService {
    List<CompactRoute> listRoutes();
}