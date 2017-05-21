package hu.pricecheck;

import hu.pricecheck.data.FileLogger;
import hu.pricecheck.model.Price;
import hu.pricecheck.service.Exchange;
import hu.pricecheck.service.PriceCompare;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

/**
 * @author Peter_Fazekas on 2017.05.21..
 */
public class App {

    private static final MathContext MATH_CONTEXT = new MathContext(8, RoundingMode.HALF_UP);
    private static final BigDecimal BRAVOS_RATE = BigDecimal.valueOf(309970798, 6);
    private static final BigDecimal LIS_TO_USD_RATE = BigDecimal.valueOf(900630, 6);
    private static final BigDecimal LIS_FROM_USD_RATE = BigDecimal.valueOf(279169000, 6);
    private static final List<String> CURRENCIES = Arrays.asList("EUR", "HUF");
    private static final String OUTPUT = "pricecompare.csv";
    private static final String SEPARATOR = ",";

    private final Exchange exchange;
    private final Price baseRoomPrice;
    private final PriceCompare priceCompare;
    private final FileLogger log;

    public App() {
        exchange = new Exchange(BRAVOS_RATE, LIS_TO_USD_RATE, LIS_FROM_USD_RATE, MATH_CONTEXT, CURRENCIES, SEPARATOR);
        baseRoomPrice = new Price(BigDecimal.valueOf(33), CURRENCIES.get(0), SEPARATOR);
        priceCompare = new PriceCompare(exchange, baseRoomPrice, SEPARATOR);
        log = new FileLogger(OUTPUT);
    }

    public static void main(String[] args) {
        App app = new App();
        app.getResult();
    }

    public void getResult() {
        StringBuilder[] lines = priceCompare.getData();
        for (StringBuilder line : lines) {
            log.println(line.toString());
        }
    }


}
