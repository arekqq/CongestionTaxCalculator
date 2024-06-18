package congestion.calculator.congestiontaxcalculator.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Vehicle {
    MOTORCYCLE,
    BUS,
    DIPLOMAT,
    EMERGENCY,
    FOREIGN,
    MILITARY;

    @JsonCreator
    public static Vehicle fromString(String key) {
        return Vehicle.valueOf(key.toUpperCase());
    }
}
