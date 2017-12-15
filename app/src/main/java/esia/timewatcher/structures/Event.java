package esia.timewatcher.structures;

import java.util.Date;

public class Event {
    private Date date;
    private OccupationType type;

    public Event(Date date, OccupationType type) {
        this.date = date;
        this.type = type;
    }

    public Event(Event event) {
        this.date = new Date(event.date.getTime());
        this.type = event.type;
    }

    public Date getDate() {
        return date;
    }

    public OccupationType getType() {
        return type;
    }
}
