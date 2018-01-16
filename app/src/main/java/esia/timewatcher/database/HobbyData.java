package esia.timewatcher.database;

import esia.timewatcher.structures.Hobby;

/**
 * Created by esia on 19/12/17.
 */

public class HobbyData extends Data {
    private Hobby hobby;
    private TypeData typeData;

    HobbyData(long id, Hobby hobby, TypeData typeData) {
        super(id);
        this.hobby = hobby;
        this.typeData = typeData;
    }

    public Hobby getHobby() {
        return hobby;
    }

    public TypeData getTypeData() {
        return typeData;
    }

    @Override
    public boolean equals(Object o) {
        HobbyData other = (HobbyData) o;
        if (o != null) {
            return (super.equals(o)
                && this.hobby.equals(other.hobby)
                && this.typeData.equals(other.typeData));
        }
        return false;
    }
}
