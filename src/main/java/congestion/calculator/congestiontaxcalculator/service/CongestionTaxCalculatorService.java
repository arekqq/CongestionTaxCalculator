package congestion.calculator.congestiontaxcalculator.service;

import congestion.calculator.congestiontaxcalculator.dto.TaxResponse;
import congestion.calculator.congestiontaxcalculator.enums.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CongestionTaxCalculatorService {

    @Value("${toll.max-fee:60}")
    private int maxFee;
    @Value("${toll.short-time-period:60}")
    private int shortTimePeriod;

    private final TollFeeService tollFeeService;

    public TaxResponse getTax(Vehicle vehicle, List<LocalDateTime> dates) {
        LocalDateTime intervalStart = dates.getFirst();
        int totalFee = 0;

        for (LocalDateTime date : dates) {
            int nextFee = tollFeeService.getTollFee(date, vehicle);
            int tempFee = tollFeeService.getTollFee(intervalStart, vehicle);

            long minutes = Duration.between(date, intervalStart).toMinutes();

            if (minutes <= shortTimePeriod) {
                if (totalFee > 0) totalFee -= tempFee;
                if (nextFee >= tempFee) tempFee = nextFee;
                totalFee += tempFee;
            } else {
                totalFee += nextFee;
            }
        }

        if (totalFee > maxFee) totalFee = maxFee;
        return new TaxResponse(totalFee);
    }
}
