package esia.timewatcher.structures;

import java.util.Date;

public class Event implements Storable {
    private Date date;

    public Event(Date date) {
        this.date = date;
    }

    public Event(Event event) {
        this.date = event.date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean isValid() {
        return date != null;
    }

    @Override
    public boolean equals(Object obj) {
        Event other = (Event) obj;
        return other != null && other.date.equals(this.date);
    }

    @Override
    public String toString() {
        return "Event{" +
                "date=" + date +
                '}';
    }
}
