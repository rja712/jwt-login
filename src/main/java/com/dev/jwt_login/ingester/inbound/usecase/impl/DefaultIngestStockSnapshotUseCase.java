package com.dev.jwt_login.ingester.inbound.usecase.impl;

import com.dev.jwt_login.ingester.domain.model.entity.StockSnapshot;
import com.dev.jwt_login.ingester.domain.port.StockSnapshotPersistencePort;
import com.dev.jwt_login.ingester.inbound.model.mapper.StockSnapshotDomainMapper;
import com.dev.jwt_login.ingester.inbound.model.request.StockSnapshotRequest;
import com.dev.jwt_login.ingester.inbound.usecase.IngestStockSnapshotUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DefaultIngestStockSnapshotUseCase implements IngestStockSnapshotUseCase {

    private final StockSnapshotDomainMapper stockSnapshotDomainMapper;
    private final StockSnapshotPersistencePort stockSnapshotPersistencePort;

    @Override
    public void ingest(StockSnapshotRequest request) {
        StockSnapshot snapshot = stockSnapshotDomainMapper.toDomain(request);

        stockSnapshotPersistencePort.save(snapshot);
        log.debug("Ingested seq={} {}", request.seq(), snapshot);
    }
}
