package hu.pricecheck;

import hu.pricecheck.data.FileLogger;
import hu.pricecheck.model.Price;
import hu.pricecheck.service.Exchange;
import hu.pricecheck.service.Round;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * @author Peter_Fazekas on 2017.05.21..
 */
public class App {

    private static final BigDecimal BRAVOS_RATE = BigDecimal.valueOf(309970798, 6);
    private static final BigDecimal LIS_TO_USD_RATE = BigDecimal.valueOf(900630, 6);
    private static final BigDecimal LIS_FROM_USD_RATE = BigDecimal.valueOf(279169000, 6);
    private static final List<String> CURRENCIES = Arrays.asList("EUR", "HUF");
    private static final List<RoundingMode> ROUNDING_MODES = Arrays.asList(RoundingMode.CEILING, RoundingMode.UP, RoundingMode.HALF_UP, RoundingMode.HALF_EVEN, RoundingMode.HALF_DOWN, RoundingMode.DOWN, RoundingMode.FLOOR, RoundingMode.UNNECESSARY);
    private static final String OUTPUT = "pricecompare.csv";


    private final Exchange exchange;
    private final Price baseRoomPrice;
    private final FileLogger log;

    public App() {
        exchange = new Exchange(BRAVOS_RATE, LIS_TO_USD_RATE, LIS_FROM_USD_RATE, CURRENCIES);
        baseRoomPrice = new Price(BigDecimal.valueOf(33), CURRENCIES.get(0));
        log = new FileLogger(OUTPUT);
    }

    public static void main(String[] args) {
        App app = new App();
        app.print();
    }

    public void print() {
        StringBuilder[] lines = getData();
        for (StringBuilder line : lines) {
            log.println(line.toString());
        }
    }

    public StringBuilder[] getData() {
        Round round = new Round(2, RoundingMode.HALF_UP);
        System.out.print("Native Difference:," + exchange.compareRates());
        System.out.println(",Rounded Difference:," + round.round(exchange.compareRates()));
        System.out.println("Room Price:," + baseRoomPrice.printValue());
        StringBuilder[] lines = new StringBuilder[8];
        for (int i = 0; i < 8; i++) {
            lines[i] = new StringBuilder();
        }
        int row;
        for (RoundingMode mode : ROUNDING_MODES) {
            for (int scale = 1; scale < 7; scale++) {
                round = new Round(scale, mode);
                row = 0;
                List<Price> bravosPrices = exchange.bravosExchange(baseRoomPrice);
                List<Price> lisPrices = exchange.lisExchange(baseRoomPrice, round);
                BigDecimal bravosPrice = null;
                BigDecimal lisPrice = null;
                lines[row++].append("Scale:," + scale + ", Rounding Mode:," + mode + ",");
                for (Price bravos : bravosPrices) {
                    lines[row++].append("Bravos Prices:," + bravos.printValue() + ",");
                    bravosPrice = bravos.getRoundedValue();
                }
                for (Price lis : lisPrices) {
                    lines[row++].append("Lis Prices:," + lis.printValue() + ",");
                    lisPrice = lis.getRoundedValue();
                }
                lines[row++].append("Difference:," + bravosPrice.subtract(lisPrice) + ",,,");
            }
        }
        return lines;
    }
}
