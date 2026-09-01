package com.dev.jwt_login.ingester.outbound.persistence.mapper;

import com.dev.jwt_login.ingester.domain.model.entity.StockSnapshot;
import com.dev.jwt_login.ingester.domain.model.valueobject.Symbol;
import com.dev.jwt_login.ingester.outbound.persistence.entity.StockSnapshotEntity;
import com.dev.jwt_login.ingester.outbound.persistence.entity.StockSnapshotId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Translates between the domain object and the database row. The row carries a nested
 * identifier object; the domain object keeps those two fields flat.
 *
 * <p>The id is built by hand because {@link StockSnapshotId} is immutable — MapStruct can only
 * populate a nested target through setters, which it deliberately does not have.
 */
@Mapper(componentModel = "spring")
public interface StockSnapshotEntityMapper {

    @Mapping(target = "id", expression = "java(toId(domain))")
    StockSnapshotEntity toEntity(StockSnapshot domain);

    @Mapping(target = "symbol", source = "id.symbol")
    @Mapping(target = "snapshotTime", source = "id.snapshotTime")
    StockSnapshot toDomain(StockSnapshotEntity entity);

    default StockSnapshotId toId(StockSnapshot domain) {
        return new StockSnapshotId(domain.getSymbol().value(), domain.getSnapshotTime());
    }

    default Symbol mapSymbol(String value) {
        return value == null ? null : new Symbol(value);
    }

    default String mapSymbol(Symbol symbol) {
        return symbol == null ? null : symbol.value();
    }
}
