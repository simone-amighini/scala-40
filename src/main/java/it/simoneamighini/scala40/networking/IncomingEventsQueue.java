package it.simoneamighini.scala40.networking;

import java.util.LinkedList;
import java.util.Queue;

class IncomingEventsQueue {
    private final Queue<Event> events;
    private boolean pause;

    IncomingEventsQueue() {
        this.events = new LinkedList<>();
        this.pause = false;
    }

    synchronized Event poll() {
        while (events.isEmpty() || pause) {
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

    synchronized void stopReadAccess() {
        pause = true;
    }

    synchronized void grantReadAccess() {
        pause = false;
        notifyAll();
    }

    synchronized void reset() {
        events.clear();
        pause = false;
    }
}
