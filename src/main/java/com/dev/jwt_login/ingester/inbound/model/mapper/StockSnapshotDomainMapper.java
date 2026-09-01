package com.dev.jwt_login.ingester.inbound.model.mapper;

import com.dev.jwt_login.ingester.domain.model.entity.StockSnapshot;
import com.dev.jwt_login.ingester.domain.model.valueobject.Symbol;
import com.dev.jwt_login.ingester.inbound.model.request.StockSnapshotRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Turns the incoming request into a domain object.
 *
 * <p>Lives in the inbound layer because the wire format is the caller's concern: the emitter's
 * timestamp text is parsed here, so the domain only ever deals with an {@link Instant}.
 * The domain constructor validates, so an invalid payload fails here rather than reaching
 * storage.
 */
@Mapper(componentModel = "spring")
public interface StockSnapshotDomainMapper {

    /**
     * The emitter sends "2017-01-02T09:15:00+0530" — an offset with no colon, which is why
     * 'xx' is used. The ISO default ('XXX') expects "+05:30" and would reject it.
     */
    DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxx");

    @Mapping(target = "snapshotTime", source = "ts")
    StockSnapshot toDomain(StockSnapshotRequest request);

    default Symbol mapSymbol(String value) {
        return value == null ? null : new Symbol(value);
    }

    default Instant mapSnapshotTime(String ts) {
        return ts == null ? null : OffsetDateTime.parse(ts, TIMESTAMP_FORMAT).toInstant();
    }
}
