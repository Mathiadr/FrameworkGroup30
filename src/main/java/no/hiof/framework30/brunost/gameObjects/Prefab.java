package no.hiof.framework30.brunost.gameObjects;

import no.hiof.framework30.brunost.components.*;
import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.util.AssetPool;
import no.hiof.framework30.brunost.util.Window;
import org.joml.Vector2f;

public class Prefab {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){
        GameObject block = Window.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }

    public static GameObject generateDefaultCharacter(){
        SpriteSheet ratgirlSprites = AssetPool.getSpriteSheet("assets/images/RatGirlSpritesheet.png");
        GameObject ratgirl = generateSpriteObject(ratgirlSprites.getSprite(0), 0.25f, 0.25f);

        Animator animator = new Animator();

        Animation idleAnim = new Animation("idle");
        idleAnim.addFrame(ratgirlSprites.getSprite(0));
        idleAnim.addFrame(ratgirlSprites.getSprite(1));
        animator.addAnimation(idleAnim);

        Animation runAnim = new Animation("run");
        for(int i=2; i<8; i++)
            runAnim.addFrame(ratgirlSprites.getSprite(i));
        animator.addAnimation(runAnim);

        ratgirl.addComponent(animator);

        return ratgirl;
    }
}
