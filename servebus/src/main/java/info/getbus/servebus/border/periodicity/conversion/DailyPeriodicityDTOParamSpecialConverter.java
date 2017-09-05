package info.getbus.servebus.border.periodicity.conversion;

import info.getbus.servebus.web.dto.route.PeriodicityPartDTO;

public class DailyPeriodicityDTOParamSpecialConverter implements PeriodicityDTOStrategyDependSpecialConverter {
    @Override
    public int extractParam(PeriodicityPartDTO periodicity) {
        return periodicity.getInterval();
    }

    @Override
    public void setParam(int param, PeriodicityPartDTO periodicity) {
        periodicity.setInterval(param);
    }

    @Override
    public void copyFields(PeriodicityPartDTO forward, PeriodicityPartDTO reverse) {
        reverse.setInterval(forward.getInterval());
    }
}
