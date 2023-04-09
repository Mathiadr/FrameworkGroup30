package no.hiof.framework30.brunost;

import no.hiof.framework30.brunost.components.Sprite;
import no.hiof.framework30.brunost.components.SpriteRenderer;
import no.hiof.framework30.brunost.gameObjects.GameObject;
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
}
