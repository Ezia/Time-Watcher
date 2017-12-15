package esia.timewatcher.structures;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class OccupationType {
    private Bitmap icon;
    private String name;

    public OccupationType(Bitmap icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}
