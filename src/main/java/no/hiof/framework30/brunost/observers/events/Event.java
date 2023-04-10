package no.hiof.framework30.brunost.observers.events;

public class Event {
    public EventType type;

    public Event(EventType type){
        this.type = type;
    }
    public Event(){
        this.type = EventType.UserEvent;
    }
}
