package esia.timewatcher.database;

import esia.timewatcher.structures.Hobby;

public class HobbyData extends Data {
    private Hobby hobby;

    HobbyData(int id) {
        super(id);
    }

    public Hobby getHobby() {
        return hobby;
    }
}
