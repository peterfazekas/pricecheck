package hu.pricecheck.service;

import hu.pricecheck.App;
import hu.pricecheck.model.Price;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;

/**
 * @author Peter_Fazekas on 2017.05.21..
 */
public class Exchange {
    private static final BigDecimal BRAVOS_RATE = App.BRAVOS_RATE;
    private static final BigDecimal LIS_TO_USD_RATE = App.LIS_TO_USD_RATE;
    private static final BigDecimal LIS_FROM_USD_RATE = App.LIS_FROM_USD_RATE;
    private static final List<String> CURRENCIES = App.CURRENCIES;
    private static final String SEPARATOR = App.SEPARATOR;
    private static final String USD = "USD";

    private final MathContext mathContext;

    public Exchange(final MathContext mathContext) {
        this.mathContext = mathContext;
    }

    public BigDecimal compareRates() {
        BigDecimal lisRate = LIS_FROM_USD_RATE.divide(LIS_TO_USD_RATE, mathContext);
        return (lisRate.subtract(BRAVOS_RATE)).abs();
    }

    public List<Price> bravosExchange(final Price origin) {
        BigDecimal priceValue = origin.getValue().multiply(BRAVOS_RATE, mathContext);
        return Arrays.asList(origin, new Price(priceValue, CURRENCIES.get(1)));
    }

    public List<Price> lisExchange(final Price origin, final Round round) {
        BigDecimal usdValue = origin.getValue().divide(LIS_TO_USD_RATE, mathContext);
        BigDecimal roundedValue = round == null ? usdValue : round.round(usdValue);
        BigDecimal displayValue = LIS_FROM_USD_RATE.multiply(roundedValue, mathContext);
        return Arrays.asList(origin, new Price(usdValue, USD), new Price(roundedValue, USD), new Price(displayValue, CURRENCIES.get(1)));
    }


}
