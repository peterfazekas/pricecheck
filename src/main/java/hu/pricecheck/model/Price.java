package hu.pricecheck.model;

import hu.pricecheck.service.Round;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Peter_Fazekas on 2017.05.21..
 */
public class Price {

    private final BigDecimal value;
    private final String currency;
    private final Round round;

    public Price(final BigDecimal value, final String currency) {
        this.value = value;
        this.currency = currency;
        round = new Round(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getRoundedValue() {
        return round.round(value);
    }

    public String printValue() {
        return getValue().toString() + "," + currency + "," + getRoundedValue().toString() + "," + currency;
    }

}
