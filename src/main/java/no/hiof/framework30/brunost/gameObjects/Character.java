package no.hiof.framework30.brunost.gameObjects;

public abstract class Character extends GameObject{

    String name;
    int health;

    public Character(String name) {
        super(name);
    }
}
