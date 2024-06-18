package congestion.calculator.congestiontaxcalculator.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.EnumSet;

public enum Vehicle {
    REGULAR,
    MOTORCYCLE,
    BUS,
    DIPLOMAT,
    EMERGENCY,
    FOREIGN,
    MILITARY;

    private static final EnumSet<Vehicle> TOLL_FREE = EnumSet.of(
        MOTORCYCLE, BUS, EMERGENCY, DIPLOMAT, FOREIGN, MILITARY
    );

    @JsonCreator
    public static Vehicle fromString(String key) {
        return Vehicle.valueOf(key.toUpperCase());
    }

    public boolean isTollFree() {
        return TOLL_FREE.contains(this);
    }
}
