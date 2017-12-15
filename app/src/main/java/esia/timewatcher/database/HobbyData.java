package esia.timewatcher.database;

import esia.timewatcher.structures.Hobby;

public class HobbyData extends Data {
    private Hobby hobby;

    HobbyData(long id, Hobby hobby) {
        super(id);
        this.hobby = hobby;
    }

    void setHobby(Hobby hobby) {
        this.hobby = hobby;
    }

    public Hobby getHobby() {
        return hobby;
    }
}
