package com.dev.jwt_login.ingester.outbound.persistence.port.impl;

import com.dev.jwt_login.ingester.domain.model.entity.StockSnapshot;
import com.dev.jwt_login.ingester.domain.port.StockSnapshotPersistencePort;
import com.dev.jwt_login.ingester.outbound.persistence.entity.StockSnapshotEntity;
import com.dev.jwt_login.ingester.outbound.persistence.jpa.StockSnapshotEntityRepository;
import com.dev.jwt_login.ingester.outbound.persistence.mapper.StockSnapshotEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Postgres side of {@link StockSnapshotPersistencePort}.
 *
 * <p>The save is synchronous on purpose. The endpoint promises that a 200 means the snapshot
 * is durably stored, so the request must not complete until the database has committed —
 * Postgres flushes its write-ahead log before acknowledging, so the commit is on disk.
 *
 * <p>The key is natural (symbol + minute) rather than generated, so saving the same snapshot
 * again updates the existing row instead of inserting a duplicate.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JpaStockSnapshotPersistencePort implements StockSnapshotPersistencePort {

    private final StockSnapshotEntityRepository stockSnapshotEntityRepository;
    private final StockSnapshotEntityMapper stockSnapshotEntityMapper;

    @Override
    public StockSnapshot save(StockSnapshot snapshot) {
        StockSnapshotEntity entity = stockSnapshotEntityMapper.toEntity(snapshot);
        StockSnapshotEntity saved = stockSnapshotEntityRepository.save(entity);

        log.debug("Stored snapshot {}", snapshot.getSymbol());
        return stockSnapshotEntityMapper.toDomain(saved);
    }

    @Override
    public long countBySymbol(String symbol) {
        return stockSnapshotEntityRepository.countByIdSymbol(symbol);
    }
}
