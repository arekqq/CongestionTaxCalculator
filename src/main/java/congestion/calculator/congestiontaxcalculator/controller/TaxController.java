package congestion.calculator.congestiontaxcalculator.controller;

import congestion.calculator.congestiontaxcalculator.dto.TaxResponse;
import congestion.calculator.congestiontaxcalculator.enums.Vehicle;
import congestion.calculator.congestiontaxcalculator.service.CongestionTaxCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tax/congestion")
@RequiredArgsConstructor
public class TaxController {

    private final CongestionTaxCalculatorService congestionTaxCalculatorService;

    @GetMapping
    public TaxResponse getTax(@RequestParam(defaultValue = "REGULAR") Vehicle vehicle,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) List<LocalDateTime> dates) {
        return congestionTaxCalculatorService.getTax(vehicle, dates);
    }
}
