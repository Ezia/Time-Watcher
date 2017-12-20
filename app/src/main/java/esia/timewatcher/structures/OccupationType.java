package esia.timewatcher.structures;

import android.graphics.Bitmap;

public class OccupationType implements Storable {
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

    @Override
    public boolean equals(Object obj) {
        OccupationType other = (OccupationType) obj;
        return other != null && other.name.equals(this.name) && other.icon.sameAs(this.icon);
    }

    @Override
    public boolean isValid() {
        return name != null && !name.isEmpty() && icon != null;
    }
}
