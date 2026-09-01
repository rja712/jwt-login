package com.dev.jwt_login.ingester.inbound.rest;

import com.dev.jwt_login.ingester.inbound.model.request.StockSnapshotRequest;
import com.dev.jwt_login.ingester.inbound.usecase.IngestStockSnapshotUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Receives stock snapshots from the emitter.
 *
 * <p>The controller only adapts HTTP to the use case — validation, domain rules and storage
 * all live behind {@link IngestStockSnapshotUseCase}. Returning 200 means the snapshot is
 * durably stored, because the use case does not return until storage has acknowledged it.
 */
@RestController
@RequestMapping("/api/ingester")
@RequiredArgsConstructor
@Slf4j
public class StockSnapshotIngestionController {

    private final IngestStockSnapshotUseCase ingestStockSnapshotUseCase;

    private final AtomicLong received = new AtomicLong();

    @PostMapping("/receive-stock-snapshot")
    @ResponseStatus(HttpStatus.OK)
    public void receive(@Valid @RequestBody StockSnapshotRequest stockSnapshotRequest) {
        ingestStockSnapshotUseCase.ingest(stockSnapshotRequest);

        log.info("snapshot #{} {}", received.incrementAndGet(), stockSnapshotRequest);
    }

    /**
     * Does nothing, on purpose — a measuring instrument, not a feature.
     *
     * <p>No {@code @RequestBody}, so the JSON is never parsed. No validation, no database, and
     * no logging (a log line per request would itself become the bottleneck at these rates).
     * Whatever rate this reaches is the fastest this application can ever answer; everything
     * real does strictly more work and is therefore slower.
     */
    @PostMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public void ping() {
    }
}
