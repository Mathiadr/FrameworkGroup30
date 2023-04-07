package no.hiof.framework30.brunost.gameObjects;

import no.hiof.framework30.brunost.components.Component;

import java.util.List;
import java.util.Objects;

public class Animator extends Component {
    // AnimationSet
    private List<Animation> animations;

    public void addAnimation(Animation animation){
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
