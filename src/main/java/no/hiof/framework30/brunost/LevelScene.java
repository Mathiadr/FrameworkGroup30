package no.hiof.framework30.brunost;

public class LevelScene extends Scene{
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
