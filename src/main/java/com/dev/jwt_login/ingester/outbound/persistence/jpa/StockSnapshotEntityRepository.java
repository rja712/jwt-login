package com.dev.jwt_login.ingester.outbound.persistence.jpa;

import com.dev.jwt_login.ingester.outbound.persistence.entity.StockSnapshotEntity;
import com.dev.jwt_login.ingester.outbound.persistence.entity.StockSnapshotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockSnapshotEntityRepository
        extends JpaRepository<StockSnapshotEntity, StockSnapshotId> {

    long countByIdSymbol(String symbol);
}
