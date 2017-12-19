package esia.timewatcher.database;

import esia.timewatcher.structures.OccupationType;

public class OccupationTypeData extends Data {
    private OccupationType occupationType;

    OccupationTypeData(long id, OccupationType type) {
        super(id);
        this.occupationType = type;
    }

    public OccupationType getOccupationType() {
        return occupationType;
    }
}
