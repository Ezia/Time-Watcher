package esia.timewatcher.database;

public abstract class Data {

    private int id;

    Data(int id) {
        this.id = id;
    }

    int getId() {
        return id;
    }
}
