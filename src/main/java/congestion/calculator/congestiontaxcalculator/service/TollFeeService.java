package congestion.calculator.congestiontaxcalculator.service;

import congestion.calculator.congestiontaxcalculator.enums.Vehicle;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;
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

    private static final Set<LocalDate> tollFreeDates2013 = new HashSet<>();
    static {
        tollFreeDates2013.add(LocalDate.of(2013, Month.JANUARY, 1));
        tollFreeDates2013.add(LocalDate.of(2013, Month.MARCH, 28));
        tollFreeDates2013.add(LocalDate.of(2013, Month.MARCH, 29));
        tollFreeDates2013.add(LocalDate.of(2013, Month.APRIL, 1));
        tollFreeDates2013.add(LocalDate.of(2013, Month.APRIL, 30));
        tollFreeDates2013.add(LocalDate.of(2013, Month.MAY, 1));
        tollFreeDates2013.add(LocalDate.of(2013, Month.MAY, 8));
        tollFreeDates2013.add(LocalDate.of(2013, Month.MAY, 9));
        tollFreeDates2013.add(LocalDate.of(2013, Month.JUNE, 5));
        tollFreeDates2013.add(LocalDate.of(2013, Month.JUNE, 6));
        tollFreeDates2013.add(LocalDate.of(2013, Month.JUNE, 21));
        // Add all days in July
        for (int day = 1; day <= Month.JULY.maxLength(); day++) {
            tollFreeDates2013.add(LocalDate.of(2013, Month.JULY, day));
        }
        tollFreeDates2013.add(LocalDate.of(2013, Month.NOVEMBER, 1));
        tollFreeDates2013.add(LocalDate.of(2013, Month.DECEMBER, 24));
        tollFreeDates2013.add(LocalDate.of(2013, Month.DECEMBER, 25));
        tollFreeDates2013.add(LocalDate.of(2013, Month.DECEMBER, 26));
        tollFreeDates2013.add(LocalDate.of(2013, Month.DECEMBER, 31));
    }


    public int getTollFee(LocalDateTime date, Vehicle vehicle) {
        if (isTollFreeDate(date) || isTollFreeVehicle(vehicle)) {
            return 0;
        }
        LocalTime time = date.toLocalTime();
        LocalTime next = feeSchedule.ceilingKey(time);
        if (next == null) {
            return 0;
        }
        LocalTime prev = feeSchedule.floorKey(time);
        if (next.isAfter(prev)) {
            return feeSchedule.get(prev);
        }
        return 0;
    }

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return false;
        }
        return vehicle.isTollFree();
    }

    public boolean isTollFreeDate(LocalDateTime date) {
        if (isWeekend(date.getDayOfWeek())) {
            return true;
        }

        return date.getYear() == 2013 && tollFreeDates2013.contains(date.toLocalDate());
    }

    private boolean isWeekend(DayOfWeek day) {
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }}
