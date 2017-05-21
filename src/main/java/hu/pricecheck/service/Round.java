package hu.pricecheck.service;

import hu.pricecheck.model.Price;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Peter_Fazekas on 2017.05.21..
 */
public class Round {

    private final int scale;
    private final RoundingMode roundMode;

    public Round(int scale, RoundingMode roundMode) {
        this.scale = scale;
        this.roundMode = roundMode;
    }

    public BigDecimal round(final BigDecimal value) {
        return value.setScale(scale, RoundingMode.HALF_UP);
    }
}
