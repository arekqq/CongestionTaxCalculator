package congestion.calculator.congestiontaxcalculator.service;

import congestion.calculator.congestiontaxcalculator.enums.Vehicle;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;

import static java.time.Month.APRIL;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.JUNE;
import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;

@Service
public class TollFeeService {

    public int getTollFee(LocalDateTime date, Vehicle vehicle) {
        if (isTollFreeDate(date) || isTollFreeVehicle(vehicle)) return 0;

        int hour = date.getHour();
        int minute = date.getMinute();

        if (hour == 6 && minute >= 0 && minute <= 29) return 8;
        else if (hour == 6 && minute >= 30 && minute <= 59) return 13;
        else if (hour == 7 && minute >= 0 && minute <= 59) return 18;
        else if (hour == 8 && minute >= 0 && minute <= 29) return 13;
            //else if (hour >= 8 && hour <= 14 && minute >= 30 && minute <= 59) return 8; // possible bug
        else if (hour == 8 && minute >= 30) return 8;
        else if (hour >= 9 && hour < 15) return 8; // fixed - TODO write test cases
        else if (hour == 15 && minute >= 0 && minute <= 29) return 13;
            //else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59) return 18; // it should be 15:30
        else if (hour == 15 && minute >= 30) return 18;
        else if (hour == 16) return 18; // fixed - TODO write test cases
        else if (hour == 17 && minute >= 0 && minute <= 59) return 13;
        else if (hour == 18 && minute >= 0 && minute <= 29) return 8;
        else return 0;
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
