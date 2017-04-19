package info.getbus.servebus.service.transporter;

import info.getbus.servebus.persistence.datamappers.route.RouteMapper;
import info.getbus.servebus.model.route.CompactRoute;
import info.getbus.servebus.service.security.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private SecurityHelper securityHelper;

/*
    public RouteServiceImpl(
            @Autowired RouteMapper routeMapper,
            @Autowired RoutePointMapper routePointMapper,
            @Autowired SecurityHelper securityHelper
    ) {
        this.routeMapper = routeMapper;
        this.routePointMapper = routePointMapper;
        this.securityHelper = securityHelper;
    }
*/

    private String resolveCurrentUser() {
        return securityHelper.getCurrentUsername();
    }

    @Override
//    @PreAuthorize()
    public List<CompactRoute> listRoutes() {
        //TODO editable if no tickets
        return routeMapper.selectCompactRoutesByUsername(resolveCurrentUser());
    }
}
