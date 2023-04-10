package no.hiof.framework30.brunost.scenes;


import imgui.ImGui;
import imgui.ImVec2;
import no.hiof.framework30.brunost.MouseControls;
import no.hiof.framework30.brunost.Prefab;
import no.hiof.framework30.brunost.components.*;
import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.util.AssetPool;
import no.hiof.framework30.brunost.util.EditorCamera;
import org.joml.Vector2f;


// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public class LevelEditorSceneInitializer extends SceneInitializer {
    private SpriteSheet spritesSpritesheet;
    private SpriteSheet defaultTilesSpritesheet;
    private GameObject levelEditorStuff;

    public LevelEditorSceneInitializer() {

    }

    @Override
    public void init(Scene scene){
        spritesSpritesheet = AssetPool.getSpriteSheet("assets/images/RatGirlSpritesheet.png");
        SpriteSheet gizmos = AssetPool.getSpriteSheet("assets/images/gizmos.png");
        defaultTilesSpritesheet = AssetPool.getSpriteSheet("assets/images/defaultTiles.png");


        levelEditorStuff = scene.createGameObject("LevelEditor");
        levelEditorStuff.setNoSerialization();
        levelEditorStuff.addComponent(new MouseControls());
        //levelEditorStuff.addComponent(new KeyControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));
        scene.addGameObjectToScene(levelEditorStuff);
    }

    @Override
    public void loadResources(Scene scene){
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/images/RatGirlSpritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/RatGirlSpritesheet.png"),
                70, 75, 8, 0));
        AssetPool.addSpriteSheet("assets/images/defaultTiles.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/defaultTiles.png"),
                64, 64, 4, 1));
        AssetPool.addSpriteSheet("assets/images/gizmos.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/gizmos.png"),
                        24, 48, 3, 0));

        // Avoids missing textures issues when it comes to saving textures
        for (GameObject gameObject: scene.getGameObjects()) {
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
    public void imgui() {
        ImGui.begin("Level Editor Stuff");
        levelEditorStuff.imgui();
        ImGui.end();


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
            // TODO: Change these
            float spriteWidth = sprite.getWidth();
            float spriteHeight = sprite.getHeight();
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)){
                GameObject object = Prefab.generateSpriteObject(sprite, 32*3, 32*3);
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
        for(int i = 0; i < defaultTilesSpritesheet.size(); i++ ){
            Sprite sprite = defaultTilesSpritesheet.getSprite(i);
            // TODO: Change these
            float spriteWidth = sprite.getWidth();
            float spriteHeight = sprite.getHeight();
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)){
                GameObject object = Prefab.generateSpriteObject(sprite, 64, 64);
                // Attach to mouse cursor
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float  lastButtonX2 = lastButtonPos.x;
            float  nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i+1 < defaultTilesSpritesheet.size() && nextButtonX2 < windowX2)
                ImGui.sameLine();
        }

        ImGui.end();
    }
}
