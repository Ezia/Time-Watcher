package esia.timewatcher.database;

import esia.timewatcher.structures.OccupationType;

public class OccupationTypeData extends Data {
    private OccupationType occupationType;

    OccupationTypeData(long id, OccupationType occupationType) {
        super(id);
        this.occupationType = occupationType;
    }

    void setOccupationType(OccupationType occupationType) {
        this.occupationType = occupationType;
    }

    public OccupationType getOccupationType() {
        return occupationType;
    }
}
