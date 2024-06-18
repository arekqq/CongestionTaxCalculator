package congestion.calculator.congestiontaxcalculator.service;


import congestion.calculator.congestiontaxcalculator.dto.TaxResponse;
import congestion.calculator.congestiontaxcalculator.enums.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CongestionTaxCalculatorServiceTest {

    private static final int MAX_FEE = 60;
    private static final int SHORT_TIME_PERIOD = 60;
    private CongestionTaxCalculatorService congestionTaxCalculatorService;

    @Mock
    private TollFeeService tollFeeService;

    @BeforeEach
    void setUp() {
        congestionTaxCalculatorService = new CongestionTaxCalculatorService(tollFeeService);
        ReflectionTestUtils.setField(congestionTaxCalculatorService, "maxFee", MAX_FEE);
    }

    @Test
    void shouldReturnMaximumFee() {
        // Given
        when(tollFeeService.getTollFee(any(), any())).thenReturn(10);

        // When
        LocalDateTime dateTime = LocalDateTime.of(2013, 2, 7, 10, 27);
        TaxResponse result = congestionTaxCalculatorService.getTax(Vehicle.REGULAR, List.of(
            dateTime,
            dateTime.plusHours(1),
            dateTime.plusHours(2),
            dateTime.plusHours(3),
            dateTime.plusHours(4),
            dateTime.plusHours(5),
            dateTime.plusHours(6),
            dateTime.plusHours(7),
            dateTime.plusHours(8)
        ));

        // Then
        assertThat(result.tax()).isEqualTo(MAX_FEE);
    }

    @Test
    void shouldReturnMaximumRateInShortTimePeriod() {
        // Given
        ReflectionTestUtils.setField(congestionTaxCalculatorService, "shortTimePeriod", SHORT_TIME_PERIOD);
        when(tollFeeService.getTollFee(any(), any())).thenReturn(10, 20, 30);

        // When
        LocalDateTime dateTime = LocalDateTime.of(2013, 2, 7, 10, 27);
        TaxResponse result = congestionTaxCalculatorService.getTax(Vehicle.REGULAR, List.of(
            dateTime,
            dateTime.plusMinutes(10),
            dateTime.plusMinutes(10),
            dateTime.plusMinutes(10)
        ));

        // Then
        assertThat(result.tax()).isEqualTo(30);
    }
}
