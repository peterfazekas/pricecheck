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
    private final String separator;

    public Price(final BigDecimal value, final String currency, final String separator) {
        this.value = value;
        this.currency = currency;
        this.separator = separator;
        round = new Round(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getRoundedValue() {
        return round.round(value);
    }

    public String printValue() {
        String actualSeparator = separator.isEmpty() ? "," : separator;
        return getValue().toString() + actualSeparator + currency + actualSeparator
               + getRoundedValue().toString() + actualSeparator + currency;
    }

}
