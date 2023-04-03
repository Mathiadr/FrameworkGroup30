package no.hiof.framework30.brunost.util;


import imgui.ImGui;
import no.hiof.framework30.brunost.ImGuiLayer;
import no.hiof.framework30.brunost.Transform;
import no.hiof.framework30.brunost.components.Sprite;
import no.hiof.framework30.brunost.components.SpriteRenderer;
import no.hiof.framework30.brunost.components.SpriteSheet;
import no.hiof.framework30.brunost.gameObjects.GameObject;
import org.joml.Vector2f;
import org.joml.Vector4f;


// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public class LevelEditorScene extends Scene {

    private GameObject object1;
    private SpriteSheet sprites;


    public LevelEditorScene () {
    }

    @Override
    public void init(){
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");
        object1 = new GameObject("Object 1",
                new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 2);
        object1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(object1);
        this.activeGameObject = object1;

        GameObject object2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), -1);
        object2.addComponent(new SpriteRenderer(sprites.getSprite(6)));
        this.addGameObjectToScene(object2);

        loadResources();
    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                1000, 1000, 8, 0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.1f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void onUpdate(float deltaTime) {

        spriteFlipTimeLeft -= deltaTime;
        if(spriteFlipTimeLeft <= 0){
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 7){
                spriteIndex = 2;
            }
            object1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }

        //System.out.println("FPS : " + (1.0f / deltaTime));
        for(GameObject gameObject : this.gameObjects){
            gameObject.onUpdate(deltaTime);
        }

        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Test");
        ImGui.text("Text");
        ImGui.end();
    }
}
