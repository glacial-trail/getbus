package info.getbus.servebus.border.periodicity.conversion;

import info.getbus.servebus.border.periodicity.PeriodicityDTOSpecialConverterFactory;
import info.getbus.servebus.model.periodicity.Periodicity;
import info.getbus.servebus.model.periodicity.PeriodicityStrategy;
import info.getbus.servebus.model.route.PeriodicityPair;
import info.getbus.servebus.service.security.SecurityHelper;
import info.getbus.servebus.web.dto.route.PeriodicityPartDTO;
import info.getbus.servebus.web.dto.route.PeriodicityPairDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class FromPeriodicityConverter implements Converter<PeriodicityPair, PeriodicityPairDTO> {

    @Autowired
    private PeriodicityDTOSpecialConverterFactory specialConverterFactory;
    @Autowired
    private SecurityHelper securityHelper;

    @Override
    public PeriodicityPairDTO convert(PeriodicityPair source) {
        PeriodicityPartDTO forwardDto = new PeriodicityPartDTO();
        PeriodicityPartDTO reverseDto = new PeriodicityPartDTO();

        Periodicity forward = source.getForward().getPeriodicity();
        Periodicity reverse = source.getReverse().getPeriodicity();

        forwardDto.setId(forward.getId());
        forwardDto.setStart(adjustZone(forward.getStart()).toLocalDate());
        reverseDto.setId(reverse.getId());
        reverseDto.setStart(adjustZone(reverse.getStart()).toLocalDate());

        PeriodicityStrategy strategy = forward.getStrategy();
        PeriodicityDTOStrategyDependSpecialConverter specialConverter = specialConverterFactory.get(strategy);
        specialConverter.setParam(forward.getParam(), forwardDto);
        specialConverter.setParam(reverse.getParam(), reverseDto);

        PeriodicityPairDTO pair = new PeriodicityPairDTO();
        pair.setRouteId(source.getRouteId());
        pair.setStrategy(strategy);
        pair.setForward(forwardDto);
        pair.setReverse(reverseDto);

        return pair;
    }

    private ZonedDateTime adjustZone(ZonedDateTime dateTime) {
        ZoneId tz = securityHelper.getCurrentUser().getProfile().getTimeZone();
        return dateTime.withZoneSameInstant(tz);
    }
}
