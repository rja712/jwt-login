package com.dev.jwt_login.ingester.inbound.usecase;

import com.dev.jwt_login.ingester.inbound.model.request.StockSnapshotRequest;

public interface IngestStockSnapshotUseCase {

    /**
     * Validates and durably stores one snapshot. Returns only once storage has acknowledged
     * the write, so the caller can safely answer 200.
     */
    void ingest(StockSnapshotRequest request);
}
