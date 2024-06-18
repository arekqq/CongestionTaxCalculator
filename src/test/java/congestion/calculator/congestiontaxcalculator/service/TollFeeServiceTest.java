package congestion.calculator.congestiontaxcalculator.service;

import congestion.calculator.congestiontaxcalculator.enums.Vehicle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TollFeeServiceTest {

    private final TollFeeService tollFeeService = new TollFeeService();

    @ParameterizedTest
    @EnumSource(value = Vehicle.class,
        names = {"MOTORCYCLE", "BUS", "EMERGENCY", "DIPLOMAT", "FOREIGN", "MILITARY"})
    void shouldReturnZeroForTollFreeVehicle(Vehicle vehicle) {

        // Given
        LocalDateTime date = LocalDateTime.of(2013, 1, 2, 15, 0); // Wednesday

        // When
        int result = tollFeeService.getTollFee(date, vehicle);

        // Then
        assertThat(result).isZero();
    }

    @ParameterizedTest
    @CsvSource({
        // Dates from ASSIGNMENT.md and weekend days
        "2013-01-14T21:00:00, 0", // Monday
        "2013-01-15T21:00:00, 0", // Tuesday
        "2013-02-07T06:23:27, 8", // Thursday
        "2013-02-07T15:27:00, 13", // Thursday
        "2013-02-08T06:27:00, 8", // Friday
        "2013-02-08T06:20:27, 8", // Friday
        "2013-02-08T14:35:00, 8", // Friday
        "2013-02-08T15:29:00, 13", // Friday
        "2013-02-08T15:47:00, 18", // Friday
        "2013-02-08T16:01:00, 18", // Friday
        "2013-02-08T16:48:00, 18", // Friday
        "2013-02-08T17:49:00, 13", // Friday
        "2013-02-08T18:29:00, 8", // Friday
        "2013-02-08T18:35:00, 0", // Friday
        "2013-03-26T14:25:00, 8", // Thursday
        "2013-03-28T14:07:27, 0", // toll-free date
        "2013-04-06T15:01:00, 0", // Saturday
        "2013-04-07T15:01:00, 0", // Sunday
    })
    void shouldReturnSingleFeeForRegularVehicle(LocalDateTime date, int expectedFee) {

        // When
        int result = tollFeeService.getTollFee(date, Vehicle.REGULAR);

        // Then
        assertThat(result).isEqualTo(expectedFee);
    }
}
