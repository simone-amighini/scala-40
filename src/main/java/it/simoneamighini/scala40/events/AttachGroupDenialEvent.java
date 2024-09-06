package it.simoneamighini.scala40.events;

import it.simoneamighini.scala40.networking.Event;
import it.simoneamighini.scala40.view.gui.SceneLoader;

public class AttachGroupDenialEvent extends Event {
    public AttachGroupDenialEvent() {
        super("ATTACH_GROUP_DENIAL");
    }

    @Override
    public void callHandler() {
        SceneLoader.getCurrentController().handle(this);
    }
}
