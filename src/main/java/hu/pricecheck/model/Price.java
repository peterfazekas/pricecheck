package hu.pricecheck.model;

import java.math.BigDecimal;

/**
 * @author Peter_Fazekas on 2017.05.21..
 */
public class Price {

    private final BigDecimal value;
    private final String currency;

    public Price(final BigDecimal value, final String currency) {
        this.value = value;
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }
}
