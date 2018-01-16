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

    @Override
    public boolean equals(Object o) {
        TypeData other = (TypeData) o;
        if (o != null) {
            return (super.equals(o)
                    && this.type.equals(other.type));
        }
        return false;
    }
}
