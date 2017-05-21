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
public class PriceCompare {

    private static final List<RoundingMode> ROUNDING_MODES = Arrays.asList(RoundingMode.CEILING, RoundingMode.UP, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN, RoundingMode.HALF_DOWN, RoundingMode.DOWN, RoundingMode.FLOOR);
    private static final int PRECISION = 16;
    private final BigDecimal bravosRate;
    private final BigDecimal lisToUsdRate;
    private final BigDecimal lisFromUsdRate;
    private final List<String> currencies;
    private final Price baseRoomPrice;
    private Exchange exchange;
    private final String separator;

    public PriceCompare(final BigDecimal bravosRate, final BigDecimal lisToUsdRate, final BigDecimal lisFromUsdRate,
                        final List<String> currencies, final Price baseRoomPrice, final String separator) {
        this.bravosRate= bravosRate;
        this.lisToUsdRate = lisToUsdRate;
        this.lisFromUsdRate = lisFromUsdRate;
        this.currencies = currencies;
        this.baseRoomPrice = baseRoomPrice;
        this.separator = separator;
    }

    public StringBuilder[] getData() {
        getHeaderInfo();
        StringBuilder[] lines = getStringBuilders();
        for (RoundingMode mode : ROUNDING_MODES) {
            for (int scale = 1; scale < 7; scale++) {
                getPriceCompare(lines, mode, scale);
            }
        }
        return lines;
    }

    private void getHeaderInfo() {
        MathContext mathContext = new MathContext(PRECISION, RoundingMode.HALF_UP);
        exchange = new Exchange(bravosRate, lisToUsdRate, lisFromUsdRate, mathContext, currencies, separator);
        Round round = new Round(2, RoundingMode.HALF_UP);
        System.out.print("Native Difference:" + separator + exchange.compareRates());
        System.out.println(",Rounded Difference:" + separator + round.round(exchange.compareRates()));
        System.out.println("Room Price:" + separator + baseRoomPrice.printValue());
    }

    private void getPriceCompare(StringBuilder[] lines, RoundingMode mode, int scale) {
        Round round = new Round(scale, mode);
        MathContext mathContext = new MathContext(PRECISION, mode);
        exchange = new Exchange(bravosRate, lisToUsdRate, lisFromUsdRate, mathContext, currencies, separator);
        List<Price> bravosPrices = exchange.bravosExchange(baseRoomPrice);
        List<Price> lisPrices = exchange.lisExchange(baseRoomPrice, round);
        BigDecimal bravosPrice = null;
        BigDecimal lisPrice = null;
        int row = 0;
        lines[row++].append("Scale:" + separator + scale + separator + " Rounding Mode:" + separator + mode + separator);
        for (Price bravos : bravosPrices) {
            lines[row++].append("Bravos Prices:" + separator + bravos.printValue() + separator);
            bravosPrice = bravos.getRoundedValue();
        }
        for (Price lis : lisPrices) {
            lines[row++].append("Lis Prices:" + separator + lis.printValue() + separator);
            lisPrice = lis.getRoundedValue();
        }
        lines[row++].append("Difference:" + separator + bravosPrice.subtract(lisPrice).abs() + separator + separator + separator);
    }

    private StringBuilder[] getStringBuilders() {
        StringBuilder[] lines = new StringBuilder[8];
        for (int i = 0; i < 8; i++) {
            lines[i] = new StringBuilder();
        }
        return lines;
    }
}
