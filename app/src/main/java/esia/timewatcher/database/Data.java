package esia.timewatcher.database;

public abstract class Data {

    protected final long id;

    Data(long id) {
        this.id = id;
    }

    long getId() {
        return id;
    }
}
