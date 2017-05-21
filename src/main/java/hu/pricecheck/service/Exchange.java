package hu.pricecheck.service;

import hu.pricecheck.model.Price;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * @author Peter_Fazekas on 2017.05.21..
 */
public class Exchange {
    private static final MathContext MATH_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);
    public static final String USD = "USD";

    private final BigDecimal bravosRate;
    private final BigDecimal lisToUsdRate;
    private final BigDecimal lisFromUsdRate;
    private final List<String> currencies;
    private final String separator;

    public Exchange(final BigDecimal bravosRate, final BigDecimal lisToUsdRate, final BigDecimal lisFromUsdRate,
                    final List<String> currencies, final String separator) {
        this.bravosRate = bravosRate;
        this.lisToUsdRate = lisToUsdRate;
        this.lisFromUsdRate = lisFromUsdRate;
        this.currencies = currencies;
        this.separator = separator;
    }

    public BigDecimal compareRates() {
        BigDecimal lisRate = lisFromUsdRate.divide(lisToUsdRate, MATH_CONTEXT);
        return (lisRate.subtract(bravosRate)).abs();
    }

    public List<Price> bravosExchange(final Price origin) {
        BigDecimal priceValue = origin.getValue().multiply(bravosRate, MATH_CONTEXT);
        return Arrays.asList(origin, new Price(priceValue, currencies.get(1), separator));
    }

    public List<Price> lisExchange(final Price origin, final Round round) {
        BigDecimal usdValue = origin.getValue().divide(lisToUsdRate, MATH_CONTEXT);
        BigDecimal roundedValue = round == null ? usdValue : round.round(usdValue);
        BigDecimal displayValue = lisFromUsdRate.multiply(roundedValue, MATH_CONTEXT);
        return Arrays.asList(origin, new Price(usdValue, USD, separator), new Price(roundedValue, USD, separator),
                new Price(displayValue, currencies.get(1), separator));
    }


}
