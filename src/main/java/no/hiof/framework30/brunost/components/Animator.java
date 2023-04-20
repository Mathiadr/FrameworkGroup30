package no.hiof.framework30.brunost.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Animator extends Component {
    // AnimationSet
    private List<Animation> animations = new ArrayList<>();


    @Override
    public void editorUpdate(float deltaTime) {
        animations.get(0).changeFrame(deltaTime);
        SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);
        if (spriteRenderer != null) {
            spriteRenderer.setSprite(animations.get(0).getCurrentFrameSprite());
        }
    }

    public void refreshTextures(){
        for (Animation animation : animations)
            animation.refreshTextures();
    }

    public void addAnimation(Animation animation){
        animation.gameObject = this.gameObject;
        animations.add(animation);
    }

    public Animation getAnimation(String name){
        for (int i=0; i< animations.size(); i++){
           if (Objects.equals(animations.get(i).getName(), name))
               return animations.get(i);
        }
        System.err.println("Could not find Animation called '" + name +"'.");
        return null;
    }
}
