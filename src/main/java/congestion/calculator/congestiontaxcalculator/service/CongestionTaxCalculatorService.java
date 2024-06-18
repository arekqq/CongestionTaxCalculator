package congestion.calculator.congestiontaxcalculator.service;

import congestion.calculator.congestiontaxcalculator.dto.TaxResponse;
import congestion.calculator.congestiontaxcalculator.enums.Vehicle;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

import static congestion.calculator.congestiontaxcalculator.enums.Vehicle.BUS;
import static congestion.calculator.congestiontaxcalculator.enums.Vehicle.DIPLOMAT;
import static congestion.calculator.congestiontaxcalculator.enums.Vehicle.EMERGENCY;
import static congestion.calculator.congestiontaxcalculator.enums.Vehicle.FOREIGN;
import static congestion.calculator.congestiontaxcalculator.enums.Vehicle.MILITARY;
import static congestion.calculator.congestiontaxcalculator.enums.Vehicle.MOTORCYCLE;

@Service
public class CongestionTaxCalculatorService {

    private static final EnumSet<Vehicle> tollFreeVehicles = EnumSet.of(
        MOTORCYCLE, BUS, EMERGENCY, DIPLOMAT, FOREIGN, MILITARY
    );

    public TaxResponse getTax(Vehicle vehicle, Date[] dates) {
        Date intervalStart = dates[0];
        int totalFee = 0;

        for (Date date : dates) {
            int nextFee = getTollFee(date, vehicle);
            int tempFee = getTollFee(intervalStart, vehicle);

            long diffInMillies = date.getTime() - intervalStart.getTime();
            long minutes = diffInMillies / 1000 / 60;

            if (minutes <= 60) {
                if (totalFee > 0) totalFee -= tempFee;
                if (nextFee >= tempFee) tempFee = nextFee;
                totalFee += tempFee;
            } else {
                totalFee += nextFee;
            }
        }

        if (totalFee > 60) totalFee = 60;
        return new TaxResponse(totalFee);
    }

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        if (vehicle == null) return false;
        return tollFreeVehicles.contains(vehicle);
    }

    public int getTollFee(Date date, Vehicle vehicle) {
        if (isTollFreeDate(date) || isTollFreeVehicle(vehicle)) return 0;

        int hour = date.getHours();
        int minute = date.getMinutes();

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

    private boolean isTollFreeDate(Date date) {
        int year = date.getYear();
        int month = date.getMonth() + 1;
        int day = date.getDay() + 1;
        int dayOfMonth = date.getDate();

        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) return true;

        if (year == 2013) {
            if ((month == 1 && dayOfMonth == 1) ||
                (month == 3 && (dayOfMonth == 28 || dayOfMonth == 29)) ||
                (month == 4 && (dayOfMonth == 1 || dayOfMonth == 30)) ||
                (month == 5 && (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9)) ||
                (month == 6 && (dayOfMonth == 5 || dayOfMonth == 6 || dayOfMonth == 21)) ||
                (month == 7) ||
                (month == 11 && dayOfMonth == 1) ||
                (month == 12 && (dayOfMonth == 24 || dayOfMonth == 25 || dayOfMonth == 26 || dayOfMonth == 31))) {
                return true;
            }
        }
        return false;
    }
}
