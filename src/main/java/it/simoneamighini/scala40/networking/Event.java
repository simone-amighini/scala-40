package it.simoneamighini.scala40.networking;

import java.io.Serializable;

public abstract class Event implements Serializable {
    private final String ID;
    private String remoteAddress;

    public Event(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return ID;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public abstract void callHandler();
}
