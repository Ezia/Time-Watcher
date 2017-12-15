package esia.timewatcher.structures;

import android.graphics.Bitmap;

public class OccupationType {
    private String name;
    private Bitmap icon;

    public OccupationType(String name, Bitmap icon) {
        this.icon = icon;
        this.name = name;
    }

    public OccupationType(OccupationType occupationType) {
        this.name = occupationType.name;
        this.icon = occupationType.icon;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}
