package esia.timewatcher.structures;

import org.joda.time.DateTime;

public class Event implements Storable {
    private DateTime date;

    public Event(DateTime date) {
        this.date = date;
    }

    public Event(Event event) {
        this.date = event.date;
    }

    public DateTime getDate() {
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
