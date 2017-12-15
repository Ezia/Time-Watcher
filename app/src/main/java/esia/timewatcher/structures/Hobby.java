package esia.timewatcher.structures;

import java.util.Date;

public class Hobby {
    private Date startDate;
    private Date endDate;
    private OccupationType type;

    public Hobby(Date startDate, OccupationType type) {
        this.startDate = startDate;
        this.endDate = new Date(0);
        this.type = type;
    }

    public Hobby(Date startDate, Date endDate, OccupationType type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public Hobby(Hobby hobby) {
        this.startDate = new Date(hobby.startDate.getTime());
        this.endDate = new Date(hobby.endDate.getTime());
        this.type = hobby.type;
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
