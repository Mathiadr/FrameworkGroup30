package no.hiof.framework30.brunost.components;

import no.hiof.framework30.brunost.components.Sprite;
import no.hiof.framework30.brunost.components.SpriteRenderer;
import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.util.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class Animation extends Component{
    private String name;
    private List<Sprite> frames = new ArrayList<>();
    transient int frameIndex = 0;
    transient float spriteChangeRate = 0.23f;
    transient float timer = spriteChangeRate;

    public Animation(String name){
        this.name = name;
    }

    public void addFrame(Sprite sprite){
        frames.add(sprite);
    }

    public void refreshTextures(){
        for(Sprite frame : frames)
            frame.setTexture(AssetPool.getTexture(frame.getTexture().getFilepath()));
    }

    public List<Sprite> getFrames() {
        return frames;
    }

    public Sprite getCurrentFrameSprite(){
        return frames.get(frameIndex);
    }

    public void changeFrame(float deltaTime){
        if (frameIndex < frames.size()){
            timer -= deltaTime;
            if (timer <= 0){
                if (!(frameIndex == frames.size() - 1))
                    frameIndex = (frameIndex + 1) % frames.size();
                timer = spriteChangeRate;
            }
        } else frameIndex = 0;
    }

    public String getName() {
        return name;
    }
}
