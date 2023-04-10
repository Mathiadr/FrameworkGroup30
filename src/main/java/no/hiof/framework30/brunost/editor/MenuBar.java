package no.hiof.framework30.brunost.editor;

import imgui.ImGui;
import no.hiof.framework30.brunost.observers.EventSystem;
import no.hiof.framework30.brunost.observers.events.Event;
import no.hiof.framework30.brunost.observers.events.EventType;

public class MenuBar {

    public void imgui(){
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File")){
            if(ImGui.menuItem("Save", "Ctrl+S")){
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if (ImGui.menuItem("Load", "Ctrl+O")){
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }

            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }
}
