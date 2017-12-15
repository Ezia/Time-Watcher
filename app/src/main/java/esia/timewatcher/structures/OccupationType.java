package esia.timewatcher.structures;

import android.graphics.drawable.Drawable;

public class OccupationType {
    private Drawable icon;
    private String name;

    public OccupationType(Drawable icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}
