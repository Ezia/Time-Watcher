package esia.timewatcher.structures;

import org.joda.time.DateTime;

public class Hobby implements Storable {
    private DateTime startDate;
    private DateTime endDate;

    public Hobby(DateTime startDate) {
        this.startDate = startDate;
        this.endDate = new DateTime(0);
    }

    public Hobby(DateTime startDate, DateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Hobby(Hobby hobby) {
        this.startDate = hobby.startDate;
        this.endDate = hobby.endDate;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public boolean isRunning() {
        return endDate.getMillis()==0;
    }

    @Override
    public boolean isValid() {
        return startDate != null && endDate != null &&
				( isRunning() || endDate.isAfter(startDate));
    }

    @Override
    public boolean equals(Object obj) {
        Hobby other = (Hobby) obj;
        return obj != null && this.startDate.equals(other.startDate)
                && this.endDate.equals(other.endDate);
    }
}
