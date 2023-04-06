package no.hiof.framework30.brunost.renderEngine;


import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import no.hiof.framework30.brunost.scenes.Scene;

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

    public void onUpdate(float deltaTime, Scene currentScene){
        currentScene.sceneImgui();
    }



    public void setupFont(){
        final ImGuiIO io = ImGui.getIO();
        io.setIniFilename("imgui.ini");
        ImFontAtlas fontAtlas = io.getFonts();
        ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());


        // Default font
        // fontAtlas.addFontDefault();

        // Fonts merge example
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets/fonts/Silkscreen/slkscr.ttf", 16, fontConfig);



        fontConfig.destroy();
    }
}