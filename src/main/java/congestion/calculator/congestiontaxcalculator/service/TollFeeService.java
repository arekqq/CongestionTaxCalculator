package congestion.calculator.congestiontaxcalculator.service;

import congestion.calculator.congestiontaxcalculator.enums.Vehicle;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.NavigableMap;
import java.util.TreeMap;

@Service
public class TollFeeService {

    private static final NavigableMap<LocalTime, Integer> feeSchedule = new TreeMap<>();
    static {
        feeSchedule.put(LocalTime.of(6, 0), 8);
        feeSchedule.put(LocalTime.of(6, 30), 13);
        feeSchedule.put(LocalTime.of(7, 0), 18);
        feeSchedule.put(LocalTime.of(8, 0), 13);
        feeSchedule.put(LocalTime.of(8, 30), 8);
        feeSchedule.put(LocalTime.of(15, 0), 13);
        feeSchedule.put(LocalTime.of(15, 30), 18);
        feeSchedule.put(LocalTime.of(17, 0), 13);
        feeSchedule.put(LocalTime.of(18, 0), 8);
        feeSchedule.put(LocalTime.of(18, 30), 0);
    }
    public int getTollFee(LocalDateTime date, Vehicle vehicle) {
        if (isTollFreeDate(date) || isTollFreeVehicle(vehicle)) return 0;
        LocalTime time = date.toLocalTime();
        LocalTime next = feeSchedule.ceilingKey(time);
        if (next == null) return 0;
        LocalTime prev = feeSchedule.floorKey(time);
        if (next.isAfter(prev)) return feeSchedule.get(prev);
        return 0;
    }

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        if (vehicle == null) return false;
        return vehicle.isTollFree();
    }

    private boolean isTollFreeDate(LocalDateTime date) {
        int year = date.getYear();
        Month month = date.getMonth();
        DayOfWeek day = date.getDayOfWeek();
        int dayOfMonth = date.getDayOfMonth();

        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) return true;

        if (year == 2013) {
            if ((month == JANUARY && dayOfMonth == 1) ||
                (month == MARCH && (dayOfMonth == 28 || dayOfMonth == 29)) ||
                (month == APRIL && (dayOfMonth == 1 || dayOfMonth == 30)) ||
                (month == MAY && (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9)) ||
                (month == JUNE && (dayOfMonth == 5 || dayOfMonth == 6 || dayOfMonth == 21)) ||
                (month == JULY) ||
                (month == NOVEMBER && dayOfMonth == 1) ||
                (month == DECEMBER && (dayOfMonth == 24 || dayOfMonth == 25 || dayOfMonth == 26 || dayOfMonth == 31))) {
                return true;
            }
        }
        return false;
    }
}
