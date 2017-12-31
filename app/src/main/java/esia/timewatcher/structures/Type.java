package esia.timewatcher.structures;

import android.graphics.Bitmap;

public class Type implements Storable {
    private String name;
    private Bitmap icon;

    public Type(String name, Bitmap icon) {
        this.icon = icon;
        this.name = name;
    }

    public Type(Type type) {
        this.name = type.name;
        this.icon = type.icon;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        Type other = (Type) obj;
        return other != null && other.name.equals(this.name) && other.icon.sameAs(this.icon);
    }

    @Override
    public String toString() {
        return "Type{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                '}';
    }

    @Override
    public boolean isValid() {
        return name != null && !name.isEmpty() && icon != null;
    }
}
