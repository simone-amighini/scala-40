package it.simoneamighini.scala40.networking;

class EchoEvent extends Event {
    public EchoEvent() {
        super("ECHO");
    }

    @Override
    public void callHandler() {}
}
