package no.hiof.framework30.brunost.renderEngine;

import no.hiof.framework30.brunost.components.SpriteRenderer;
import no.hiof.framework30.brunost.gameObjects.GameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Handles the rendering of objects and usage of shaders
 * @see RenderBatch
 * @see Shader
 * @see Texture
 */
public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;
    private static Shader currentShader;

    public Renderer(){
        this.batches = new ArrayList<>();
    }

    public void add(GameObject gameObject){
        SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
        if(spriteRenderer != null){
            add(spriteRenderer);
        }
    }

    private void add(SpriteRenderer sprite){
        boolean added = false;
        for (RenderBatch batch:
             batches) {
            if (batch.hasRoom() && batch.zIndex() == sprite.gameObject.transform.zIndex) {
                Texture texture = sprite.getTexture();
                if (texture == null || (batch.hasTexture(texture) || batch.hasTextureRoom())) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }
        if (!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.transform.zIndex);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void destroyGameObject(GameObject gameObject){
        if (gameObject.getComponent(SpriteRenderer.class) == null) return;
        for (RenderBatch batch : batches){
            if(batch.destroyIfExists(gameObject)){
                return;
            }
        }
    }

    public void render() {
        currentShader.use();
        for (RenderBatch batch:
             batches) {
            batch.render();
        }
    }

    public static void bindShader(Shader shader){
        currentShader = shader;
    }

    public static Shader getBoundShader(){
        return currentShader;
    }
}
