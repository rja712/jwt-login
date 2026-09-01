package com.dev.jwt_login.ingester.outbound.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * What identifies one snapshot: a symbol and the minute it covers.
 *
 * <p>Using a natural key rather than a generated id is deliberate — saving the same snapshot
 * twice updates the existing row instead of inserting a duplicate, so replaying the feed is
 * safe.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StockSnapshotId implements Serializable {

    @Column(name = "symbol", nullable = false, length = 50)
    private String symbol;

    @Column(name = "snapshot_time", nullable = false)
    private Instant snapshotTime;
}
