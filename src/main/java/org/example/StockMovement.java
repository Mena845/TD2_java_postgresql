package org.example;

import java.time.Instant;

public class StockMovement {

    final int id;
    final StockValue value;
    final MovementTypeEnum type;
    final Instant creationDatetime;

    public StockMovement(
            int id,
            StockValue value,
            MovementTypeEnum type,
            Instant creationDatetime
    ) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.creationDatetime = creationDatetime;
    }

    public int getId() {
        return id;
    }

    public StockValue getValue() {
        return value;
    }

    public MovementTypeEnum getType() {
        return type;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }
}
