package no.hiof.framework30.brunost.components;

import no.hiof.framework30.brunost.gameObjects.Component;

public class FontRenderer extends Component {


    @Override
    public void onStart(){
        if (gameObject.getComponent((SpriteRenderer.class)) != null){
            System.out.println("Found font Renderer");
        }
    }

    @Override
    public void onUpdate(float deltaTime) {

    }
}
