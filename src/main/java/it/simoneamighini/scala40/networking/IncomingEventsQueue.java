package it.simoneamighini.scala40.networking;

import java.util.LinkedList;
import java.util.Queue;

public class IncomingEventsQueue {
    private final Queue<Event> events;

    IncomingEventsQueue() {
        this.events = new LinkedList<>();
    }

    public synchronized Event poll() {
        while (events.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException exception) {
                return null;
            }
        }

        return events.poll();
    }

    synchronized void add(Event event) {
        events.add(event);
        notifyAll();
    }

    synchronized void clear() {
        events.clear();
    }
}
