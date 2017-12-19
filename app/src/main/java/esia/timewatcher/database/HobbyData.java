package esia.timewatcher.database;

import esia.timewatcher.structures.Hobby;

/**
 * Created by esia on 19/12/17.
 */

public class HobbyData extends Data {
    private Hobby hobby;
    private OccupationTypeData occupationTypeData;

    HobbyData(long id, Hobby hobby, OccupationTypeData occupationTypeData) {
        super(id);
        this.hobby = this.hobby;
        this.occupationTypeData = occupationTypeData;
    }

    public Hobby getHobby() {
        return hobby;
    }

    public OccupationTypeData getOccupationTypeData() {
        return occupationTypeData;
    }
}
