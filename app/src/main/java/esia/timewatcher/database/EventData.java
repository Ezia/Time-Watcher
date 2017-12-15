package esia.timewatcher.database;

import esia.timewatcher.structures.Event;

public class EventData extends Data {
    private Event event;

    EventData(long id, Event event) {
        super(id);
        this.event = event;
    }

    void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
