package esia.timewatcher.database;

public abstract class Data {
    private long id;

    Data(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        Data other = (Data) o;
        if (other == null) {
            return false;
        }
        return (this.id == other.id);
    }
}
