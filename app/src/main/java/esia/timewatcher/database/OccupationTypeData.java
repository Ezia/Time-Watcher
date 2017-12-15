package esia.timewatcher.database;

import esia.timewatcher.structures.OccupationType;

public class OccupationTypeData extends Data {
    private OccupationType occupationType;

    OccupationTypeData(int id) {
        super(id);
    }

    public OccupationType getOccupationType() {
        return occupationType;
    }
}
