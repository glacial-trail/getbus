package info.getbus.servebus.border.periodicity;

import info.getbus.servebus.border.periodicity.conversion.PeriodicityDTOStrategyDependSpecialConverter;
import info.getbus.servebus.route.model.PeriodicityStrategy;

import java.util.Map;

public class MapPeriodicityDTOSpecialConverterFactory implements PeriodicityDTOSpecialConverterFactory {
    private Map<PeriodicityStrategy, PeriodicityDTOStrategyDependSpecialConverter> converters;

    public MapPeriodicityDTOSpecialConverterFactory(Map<PeriodicityStrategy, PeriodicityDTOStrategyDependSpecialConverter> converters) {
        this.converters = converters;
    }

    @Override
    public PeriodicityDTOStrategyDependSpecialConverter get(PeriodicityStrategy strategy) {
        PeriodicityDTOStrategyDependSpecialConverter constructor = converters.get(strategy);
        if (null != constructor) {
            return constructor;
        } else {
            //TODO think about exception
            throw new RuntimeException("Unsupported strategy");
        }
    }
}
