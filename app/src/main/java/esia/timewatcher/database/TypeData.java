package esia.timewatcher.database;

import esia.timewatcher.structures.Type;

public class TypeData extends Data {
    private Type type;

    TypeData(long id, Type type) {
        super(id);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
