package com.dev.jwt_login.ingester.domain.port;

import com.dev.jwt_login.ingester.domain.model.entity.StockSnapshot;

/**
 * What the domain needs from storage. The domain never learns which database is behind it.
 */
public interface StockSnapshotPersistencePort {

    /**
     * Stores the snapshot and returns only once storage has durably acknowledged it.
     * Storage is keyed by symbol + time, so re-saving the same snapshot replaces it rather
     * than creating a duplicate.
     */
    StockSnapshot save(StockSnapshot snapshot);

    long countBySymbol(String symbol);
}
