package no.hiof.framework30.brunost.observers;

import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.observers.events.Event;

public interface Observer {
    /**
     * Notifies whenever an event happens
     * @param object
     * @param event
     */
    void onNotify(GameObject object, Event event);
}
