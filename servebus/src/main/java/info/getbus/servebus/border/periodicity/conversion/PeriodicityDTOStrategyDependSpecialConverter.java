package info.getbus.servebus.border.periodicity.conversion;

import info.getbus.servebus.web.dto.route.PeriodicityPartDTO;

public interface PeriodicityDTOStrategyDependSpecialConverter {
    int extractParam(PeriodicityPartDTO periodicity);
    void setParam(int param, PeriodicityPartDTO periodicity);
    void copyFields(PeriodicityPartDTO forward, PeriodicityPartDTO reverse);
}
