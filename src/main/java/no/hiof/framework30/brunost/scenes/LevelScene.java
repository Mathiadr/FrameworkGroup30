package no.hiof.framework30.brunost.scenes;

import no.hiof.framework30.brunost.util.Window;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public class LevelScene extends Scene {
    public LevelScene(){
        System.out.println("Inside level scene");
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
        Window.get().a = 1;
    }

    @Override
    public void onUpdate(float deltaTime) {

    }
}
