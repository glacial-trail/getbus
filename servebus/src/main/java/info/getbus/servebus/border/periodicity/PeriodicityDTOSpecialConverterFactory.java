package info.getbus.servebus.border.periodicity;

import info.getbus.servebus.border.periodicity.conversion.PeriodicityDTOStrategyDependSpecialConverter;
import info.getbus.servebus.route.model.PeriodicityStrategy;

public interface PeriodicityDTOSpecialConverterFactory {
    PeriodicityDTOStrategyDependSpecialConverter get(PeriodicityStrategy strategy);
}
