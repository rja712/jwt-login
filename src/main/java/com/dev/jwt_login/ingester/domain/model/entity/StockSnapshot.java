package com.dev.jwt_login.ingester.domain.model.entity;

import com.dev.jwt_login.ingester.domain.exception.InvalidSnapshotException;
import com.dev.jwt_login.ingester.domain.model.valueobject.Symbol;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * One minute of trading for one stock: the first, highest, lowest and last price during that
 * minute, plus how many shares changed hands.
 *
 * <p>The object cannot exist in an invalid state — every rule is checked in the constructor,
 * so anything that reaches persistence is already trustworthy.
 */
@Getter
public class StockSnapshot {


    private final Symbol symbol;
    private final Instant snapshotTime;
    private final BigDecimal open;
    private final BigDecimal high;
    private final BigDecimal low;
    private final BigDecimal close;
    private final BigDecimal volume;

    public StockSnapshot(Symbol symbol,
                         Instant snapshotTime,
                         BigDecimal open,
                         BigDecimal high,
                         BigDecimal low,
                         BigDecimal close,
                         BigDecimal volume) {
        if (symbol == null) {
            throw new InvalidSnapshotException("Symbol is required");
        }
        if (snapshotTime == null) {
            throw new InvalidSnapshotException("Snapshot time is required");
        }
        requirePresentAndNotNegative(open, "Open");
        requirePresentAndNotNegative(high, "High");
        requirePresentAndNotNegative(low, "Low");
        requirePresentAndNotNegative(close, "Close");
        requirePresentAndNotNegative(volume, "Volume");
        if (high.compareTo(low) < 0) {
            throw new InvalidSnapshotException(
                    "High (" + high + ") cannot be below low (" + low + ") for " + symbol);
        }

        this.symbol = symbol;
        this.snapshotTime = snapshotTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }



    private static void requirePresentAndNotNegative(BigDecimal value, String field) {
        if (value == null) {
            throw new InvalidSnapshotException(field + " is required");
        }
        if (value.signum() < 0) {
            throw new InvalidSnapshotException(field + " cannot be negative, got " + value);
        }
    }

    @Override
    public String toString() {
        return symbol + " " + snapshotTime + " o=" + open + " h=" + high + " l=" + low + " c=" + close + " v=" + volume;
    }
}
