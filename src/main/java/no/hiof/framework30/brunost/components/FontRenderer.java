package no.hiof.framework30.brunost.components;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE


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
