package no.hiof.framework30.brunost.components;

import no.hiof.framework30.brunost.gameObjects.Component;

public class SpriteRenderer extends Component {

    private boolean firstTime = false;

    @Override
    public void onStart() {
        System.out.println("Updating!");
    }

    @Override
    public void onUpdate(float deltaTime) {
        if (!firstTime)
            System.out.println("Updating!");
        firstTime = true;
    }
}
