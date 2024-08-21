package it.simoneamighini.scala40.networking;

public interface NetworkObserver {
    void networkStopUpdate();
    void connectionClosingUpdate(String remoteAddress);
}
