package com.dev.jwt_login.ingester.inbound.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * One stock snapshot as sent by the emitter.
 *
 * <p>Checks here are about the shape of the payload — present, right type, not negative.
 * Rules that need more than one field together (high cannot be below low) live in the domain
 * object, because they are business rules rather than request formatting.
 *
 * <p>{@code ts} stays a String because the emitter sends {@code 2017-01-02T09:15:00+0530},
 * an offset with no colon that Jackson's default parsing rejects. The inbound mapper parses it.
 *
 * <p>{@code seq} counts up by one per request within an emitter run, so gaps in the log mean
 * requests were lost.
 */
public record StockSnapshotRequest(

        @NotBlank(message = "Symbol is required")
        @Size(max = 50, message = "Symbol must be at most 50 characters")
        String symbol,

        @NotBlank(message = "Timestamp is required")
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+-]\\d{4}",
                message = "Timestamp must look like 2017-01-02T09:15:00+0530")
        String ts,

        @NotNull(message = "Open is required")
        @DecimalMin(value = "0.0", message = "Open cannot be negative")
        BigDecimal open,

        @NotNull(message = "High is required")
        @DecimalMin(value = "0.0", message = "High cannot be negative")
        BigDecimal high,

        @NotNull(message = "Low is required")
        @DecimalMin(value = "0.0", message = "Low cannot be negative")
        BigDecimal low,

        @NotNull(message = "Close is required")
        @DecimalMin(value = "0.0", message = "Close cannot be negative")
        BigDecimal close,

        @NotNull(message = "Volume is required")
        @DecimalMin(value = "0.0", message = "Volume cannot be negative")
        BigDecimal volume,

        long seq
) {

    /**
     * A record already generates a toString, but it spells out every component
     * ({@code StockSnapshotRequest[symbol=RELIANCE, ts=..., open=...]}), which is noisy in a
     * log. This shorter form keeps the log line to one argument.
     */
    @Override
    public String toString() {
        return "seq=" + seq + " " + symbol + " " + ts
                + " o=" + open + " h=" + high + " l=" + low + " c=" + close + " v=" + volume;
    }
}
