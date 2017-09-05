package info.getbus.servebus.border.periodicity.conversion;

import info.getbus.servebus.web.dto.route.PeriodicityPartDTO;

public class WeekDaysPeriodicityDTOParamSpecialConverter implements PeriodicityDTOStrategyDependSpecialConverter {
    @Override
    public int extractParam(PeriodicityPartDTO periodicity) {
         return arrayToBits(periodicity.getWeekDays());
    }

    @Override
    public void setParam(int param, PeriodicityPartDTO periodicity) {
        periodicity.setWeekDays(bitsToArray(param));
    }

    @Override
    public void copyFields(PeriodicityPartDTO forward, PeriodicityPartDTO reverse) {
        reverse.setStart(forward.getStart());
        reverse.setTZ(forward.getTZ());
    }

    private int arrayToBits(boolean[] bitArray) {
        int bits = 0;
        for (int i = 0; i < bitArray.length; i ++) {
            if (bitArray[i]) {
                bits = bits | (1 << i);
            }
        }
        return bits;
    }

    boolean[] bitsToArray(int bits) {
        boolean[] bitArray = new boolean[7];
        for (int i = 0; i < 7; i++) {
            bitArray[i] = (bits & (1L << i)) != 0;
        }
        return bitArray;
    }

}
