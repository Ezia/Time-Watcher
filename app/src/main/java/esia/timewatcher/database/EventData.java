package esia.timewatcher.database;

import esia.timewatcher.structures.Event;


public class EventData extends Data {
    private Event event;
    private OccupationTypeData occupationTypeData;

    EventData(long id, Event event, OccupationTypeData occupationTypeData) {
        super(id);
        this.event = event;
        this.occupationTypeData = occupationTypeData;
    }

    public Event getEvent() {
        return event;
    }

    public OccupationTypeData getOccupationTypeData() {
        return occupationTypeData;
    }
}
