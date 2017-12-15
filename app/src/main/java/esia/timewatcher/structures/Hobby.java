package esia.timewatcher.structures;

import java.util.Date;

public class Hobby {
    private Date startDate;
    private Date endDate = null;
    private OccupationType type;

    public Hobby(Date startDate, OccupationType type) {
        this.startDate = startDate;
        this.type = type;
    }

    public Hobby(Date startDate, Date endDate, OccupationType type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public OccupationType getType() {
        return type;
    }
}
