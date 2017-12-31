package esia.timewatcher.database;

import esia.timewatcher.structures.Event;


public class EventData extends Data {
    private Event event;
    private TypeData typeData;

    EventData(long id, Event event, TypeData typeData) {
        super(id);
        this.event = event;
        this.typeData = typeData;
    }

    public Event getEvent() {
        return event;
    }

    public TypeData getTypeData() {
        return typeData;
    }
}
