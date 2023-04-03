package no.hiof.framework30.brunost;


import imgui.ImGui;
import no.hiof.framework30.brunost.util.Window;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiLayer {
    private boolean showText = false;

    public void imgui(){
        ImGui.begin("Cool Window");

        if (ImGui.button("I am a button"))
            showText = true;

        if (showText){
            ImGui.text("You clicked a button");
            ImGui.sameLine();
            if (ImGui.button("Stop showing text"))
                showText = false;
        }

        ImGui.end();
    }
}