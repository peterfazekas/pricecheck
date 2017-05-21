package hu.pricecheck.service;

import hu.pricecheck.App;
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

    private static final String SEPARATOR = App.SEPARATOR;
    private static final List<RoundingMode> ROUNDING_MODES = Arrays.asList(RoundingMode.CEILING, RoundingMode.UP, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN, RoundingMode.HALF_DOWN, RoundingMode.DOWN, RoundingMode.FLOOR);
    private static final int PRECISION = App.PRECISION;
    private final Price baseRoomPrice;
    private Exchange exchange;

    public PriceCompare(final Price baseRoomPrice) {
        this.baseRoomPrice = baseRoomPrice;
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
        exchange = new Exchange(mathContext);
        Round round = new Round(2, RoundingMode.HALF_UP);
        System.out.print("Native Difference:" + SEPARATOR + exchange.compareRates());
        System.out.println(",Rounded Difference:" + SEPARATOR + round.round(exchange.compareRates()));
        System.out.println("Room Price:" + SEPARATOR + printPriceValue(baseRoomPrice));
    }

    private void getPriceCompare(StringBuilder[] lines, RoundingMode mode, int scale) {
        Round round = new Round(scale, mode);
        MathContext mathContext = new MathContext(PRECISION, mode);
        exchange = new Exchange(mathContext);
        List<Price> bravosPrices = exchange.bravosExchange(baseRoomPrice);
        List<Price> lisPrices = exchange.lisExchange(baseRoomPrice, round);
        BigDecimal bravosPrice = null;
        BigDecimal lisPrice = null;
        int row = 0;
        lines[row++].append("Scale:" + SEPARATOR + scale + SEPARATOR + " Rounding Mode:" + SEPARATOR + mode + SEPARATOR);
        for (Price bravos : bravosPrices) {
            lines[row++].append("Bravos Prices:" + SEPARATOR + printPriceValue(bravos) + SEPARATOR);
            bravosPrice = getRoundedValue(bravos.getValue());
        }
        for (Price lis : lisPrices) {
            lines[row++].append("Lis Prices:" + SEPARATOR + printPriceValue(lis) + SEPARATOR);
            lisPrice = getRoundedValue(lis.getValue());
        }
        lines[row++].append("Difference:" + SEPARATOR + bravosPrice.subtract(lisPrice).abs() + SEPARATOR + SEPARATOR + SEPARATOR);
    }

    private StringBuilder[] getStringBuilders() {
        StringBuilder[] lines = new StringBuilder[8];
        for (int i = 0; i < 8; i++) {
            lines[i] = new StringBuilder();
        }
        return lines;
    }

    public BigDecimal getRoundedValue(BigDecimal value) {
        Round round = new Round(2, RoundingMode.HALF_UP);
        return round.round(value);
    }

    public String printPriceValue(final Price price) {
        return price.getValue().toString() + SEPARATOR + price.getCurrency() + SEPARATOR
                + getRoundedValue(price.getValue()).toString() + SEPARATOR + price.getCurrency();
    }

}
