package esia.timewatcher.database;

import esia.timewatcher.structures.Event;

public class EventData extends Data {
    private Event event;

    EventData(int id) {
        super(id);
    }

    public Event getEvent() {
        return event;
    }
}
