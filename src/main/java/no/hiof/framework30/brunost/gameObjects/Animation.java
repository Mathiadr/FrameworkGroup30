package no.hiof.framework30.brunost.gameObjects;

import no.hiof.framework30.brunost.components.Sprite;
import no.hiof.framework30.brunost.components.SpriteRenderer;

import java.util.List;

public class Animation {
    private String name;
    private List<Sprite> frames;

    public Animation(String name){
        this.name = name;
    }

    public void addFrame(Sprite sprite){
        frames.add(sprite);
    }

    public List<Sprite> getFrames() {
        return frames;
    }

    public void play(GameObject gameObject){
        /*
        int spriteIndex = 0;
        float spriteFlipTime = 0.1f;
        float spriteFlipTimeLeft = 0.0f;

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
        //gameObject.getComponent(SpriteRenderer.class).setSprite();
    }

    public String getName() {
        return name;
    }
}
