package congestion.calculator.congestiontaxcalculator.service;

import congestion.calculator.congestiontaxcalculator.dto.TaxResponse;
import congestion.calculator.congestiontaxcalculator.enums.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CongestionTaxCalculatorService {

    private final TollFeeService tollFeeService;

    public TaxResponse getTax(Vehicle vehicle, List<LocalDateTime> dates) {
        LocalDateTime intervalStart = dates.getFirst();
        int totalFee = 0;

        for (LocalDateTime date : dates) {
            int nextFee = tollFeeService.getTollFee(date, vehicle);
            int tempFee = tollFeeService.getTollFee(intervalStart, vehicle);

            long diffInMillies = Duration.between(date, intervalStart).toMillis();
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
}
