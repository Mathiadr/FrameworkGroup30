package no.hiof.framework30.brunost.scenes;


import imgui.ImGui;
import imgui.ImVec2;
import no.hiof.framework30.brunost.MouseControls;
import no.hiof.framework30.brunost.Prefab;
import no.hiof.framework30.brunost.Transform;
import no.hiof.framework30.brunost.components.*;
import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.renderEngine.DebugDraw;
import no.hiof.framework30.brunost.util.AssetPool;
import no.hiof.framework30.brunost.util.Camera;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;


// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public class LevelEditorScene extends Scene {

    private GameObject object1;
    private SpriteSheet spritesSpritesheet;

    GameObject levelEditorStuff = new GameObject("levelEditor", new Transform(new Vector2f()), 0);


    public LevelEditorScene () {
    }

    @Override
    public void init(){
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());

        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        spritesSpritesheet = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        //sprites = new ArrayList<>();
        //Sprite newSprite = new Sprite();
        //newSprite.setTexture(AssetPool.getTexture("Assets/images/grassTile.png"));
        //sprites.add(newSprite);



        if (levelLoaded) {
            if (gameObjects.size() > 0)
                this.activeGameObject = gameObjects.get(0);
            return;
        }
        /*

        object1 = new GameObject("Object 1",
                new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 2);
        SpriteRenderer obj1Sprite = new SpriteRenderer();
        obj1Sprite.setSprite(spritesSpritesheet.getSprite(0));
        object1.addComponent(obj1Sprite);
        object1.addComponent(new RigidBody());
        this.addGameObjectToScene(object1);
        this.activeGameObject = object1;

        GameObject object2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), -1);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("Assets/images/test.png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);
        object2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(object2);
        */
    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                1000, 1000, 8, 0));

        // Avoids missing textures issues when it comes to saving textures
        for (GameObject gameObject: gameObjects) {
            if (gameObject.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
                if (spriteRenderer.getTexture() != null)
                    spriteRenderer.setTexture(AssetPool.getTexture(spriteRenderer.getTexture().getFilepath()));
            }
        }
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.1f;
    private float spriteFlipTimeLeft = 0.0f;

    float x = 0.0f;
    float y = 0.0f;
    @Override
    public void onUpdate(float deltaTime) {
        levelEditorStuff.onUpdate(deltaTime);
        DebugDraw.addBox2D(new Vector2f(400, 200), new Vector2f(64, 32), 30, new Vector3f(0, 1, 0), 1);
        DebugDraw.addCircle2D(new Vector2f(x, y), 64, new Vector3f(0,1,0), 1);
        x += 50f * deltaTime;
        y += 50f * deltaTime;

        /*
        spriteFlipTimeLeft -= deltaTime;
        if(spriteFlipTimeLeft <= 0){
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 7){
                spriteIndex = 2;
            }
            this.activeGameObject.getComponent(SpriteRenderer.class).setSprite(spritesSpritesheet.getSprite(spriteIndex));
        }
        */


        //System.out.println("FPS : " + (1.0f / deltaTime));
        for(GameObject gameObject : this.gameObjects){
            gameObject.onUpdate(deltaTime);
        }

        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Spritesheet");
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for(int i = 0; i < spritesSpritesheet.size(); i++ ){
            Sprite sprite = spritesSpritesheet.getSprite(i);
            float spriteWidth = sprite.getWidth() / 6;
            float spriteHeight = sprite.getHeight() / 6;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)){
                GameObject object = Prefab.generateSpriteObject(sprite, 32*2, 32*2);
                // Attach to mouse cursor
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float  lastButtonX2 = lastButtonPos.x;
            float  nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i+1 < spritesSpritesheet.size() && nextButtonX2 < windowX2)
                ImGui.sameLine();
       }

        ImGui.end();
    }
}
