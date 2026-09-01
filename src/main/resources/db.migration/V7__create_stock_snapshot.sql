-- One stock snapshot = one minute of trading for one stock: open/high/low/close are the
-- first, highest, lowest and last price in that minute, and volume is how many shares
-- changed hands.
CREATE TABLE stock_snapshot
(
    id            BIGSERIAL      PRIMARY KEY,
    symbol        VARCHAR(50)    NOT NULL,
    snapshot_time TIMESTAMPTZ    NOT NULL,
    open          NUMERIC(19, 4) NOT NULL,
    high          NUMERIC(19, 4) NOT NULL,
    low           NUMERIC(19, 4) NOT NULL,
    close         NUMERIC(19, 4) NOT NULL,
    volume        NUMERIC(19, 4) NOT NULL,

    CONSTRAINT uq_stock_snapshot_symbol_time UNIQUE (symbol, snapshot_time)
);

-- Reads are "one symbol over a time range", which the primary key already serves. This
-- index covers the other direction: everything that happened across symbols in a window.
CREATE INDEX idx_stock_snapshot_time ON stock_snapshot (snapshot_time);
