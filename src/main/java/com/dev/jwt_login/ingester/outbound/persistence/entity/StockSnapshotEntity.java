package com.dev.jwt_login.ingester.outbound.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * One minute of trading for one stock, as stored in Postgres.
 */
@Entity
@Table(name = "stock_snapshot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockSnapshotEntity {

    @EmbeddedId
    private StockSnapshotId id;

    @Column(name = "open", nullable = false)
    private BigDecimal open;

    @Column(name = "high", nullable = false)
    private BigDecimal high;

    @Column(name = "low", nullable = false)
    private BigDecimal low;

    @Column(name = "close", nullable = false)
    private BigDecimal close;

    @Column(name = "volume", nullable = false)
    private BigDecimal volume;
}
