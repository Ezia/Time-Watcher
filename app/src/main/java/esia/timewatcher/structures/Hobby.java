package esia.timewatcher.structures;

import java.util.Date;

public class Hobby implements Storable {
    private Date startDate;
    private Date endDate;

    public Hobby(Date startDate) {
        this.startDate = startDate;
        this.endDate = new Date(0);
    }

    public Hobby(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Hobby(Hobby hobby) {
        this.startDate = hobby.startDate;
        this.endDate = hobby.endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isRunning() {
        return endDate.getTime()==0;
    }

    @Override
    public boolean isValid() {
        return startDate != null && endDate != null && ( isRunning() || endDate.after(startDate));
    }

    @Override
    public boolean equals(Object obj) {
        Hobby other = (Hobby) obj;
        return obj != null && this.startDate.equals(other.startDate)
                && this.endDate.equals(other.endDate);
    }
}
