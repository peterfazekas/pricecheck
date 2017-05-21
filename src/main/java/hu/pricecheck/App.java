package hu.pricecheck;

import hu.pricecheck.data.FileLogger;
import hu.pricecheck.model.Price;
import hu.pricecheck.service.PriceCompare;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author Peter_Fazekas on 2017.05.21..
 */
public class App {

    public static final BigDecimal BRAVOS_RATE = BigDecimal.valueOf(309970798, 6);
    public static final BigDecimal LIS_TO_USD_RATE = BigDecimal.valueOf(900630, 6);
    public static final BigDecimal LIS_FROM_USD_RATE = BigDecimal.valueOf(279169000, 6);
    public static final List<String> CURRENCIES = Arrays.asList("EUR", "HUF");
    public static final int PRECISION = 16;
    public static final String SEPARATOR = ",";

    private static final long ROOM_PRICE = 33;
    private static final String OUTPUT = "pricecompare.csv";

    private final Price baseRoomPrice;
    private final PriceCompare priceCompare;
    private final FileLogger log;

    public App() {
        baseRoomPrice = new Price(BigDecimal.valueOf(ROOM_PRICE), CURRENCIES.get(0));
        priceCompare = new PriceCompare(baseRoomPrice);
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
