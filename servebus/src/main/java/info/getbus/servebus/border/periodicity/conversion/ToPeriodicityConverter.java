package info.getbus.servebus.border.periodicity.conversion;

import info.getbus.servebus.border.periodicity.PeriodicityDTOSpecialConverterFactory;
import info.getbus.servebus.model.periodicity.Periodicity;
import info.getbus.servebus.model.periodicity.PeriodicityStrategy;
import info.getbus.servebus.model.route.PeriodicityPair;
import info.getbus.servebus.model.route.RoutePartId;
import info.getbus.servebus.model.route.RoutePeriodicity;
import info.getbus.servebus.web.dto.route.PeriodicityPartDTO;
import info.getbus.servebus.web.dto.route.PeriodicityPairDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ToPeriodicityConverter implements Converter<PeriodicityPairDTO, PeriodicityPair> {

    @Autowired
    private PeriodicityDTOSpecialConverterFactory specialConverterFactory;

    @Override
    public PeriodicityPair convert(PeriodicityPairDTO source) {
        return new PeriodicityPair(source.getRouteId(), makeForwardFart(source), makeReversePart(source));
    }

    private RoutePeriodicity makeForwardFart(PeriodicityPairDTO dto) {
        return makeRoutePeriodicity(dto.getForward(), RoutePartId.forward(dto.getRouteId()), dto.getStrategy());
    }

    private RoutePeriodicity makeReversePart(PeriodicityPairDTO dto) {
        PeriodicityDTOStrategyDependSpecialConverter specialConverter = specialConverterFactory.get(dto.getStrategy());
        specialConverter.copyFields(dto.getForward(), dto.getReverse());
        return makeRoutePeriodicity(dto.getReverse(), RoutePartId.reverse(dto.getRouteId()), dto.getStrategy());
    }

    private RoutePeriodicity makeRoutePeriodicity(PeriodicityPartDTO periodicityPart, RoutePartId routePartId, PeriodicityStrategy strategy) {
        return new RoutePeriodicity(
                routePartId,
                new Periodicity(periodicityPart.getId(),
                        makeZonedStart(periodicityPart.getStart(), periodicityPart.getTZ()),
                        strategy,
                        convertParam(strategy, periodicityPart)
                )
        );
    }

    private ZonedDateTime makeZonedStart(LocalDate start, String TZ) {
        return ZonedDateTime.of(start, LocalTime.MIDNIGHT, ZoneId.of(TZ));
    }

    private int convertParam(PeriodicityStrategy strategy, PeriodicityPartDTO periodicityPart) {
        PeriodicityDTOStrategyDependSpecialConverter specialConverter = specialConverterFactory.get(strategy);
        return specialConverter.extractParam(periodicityPart);
    }
}
