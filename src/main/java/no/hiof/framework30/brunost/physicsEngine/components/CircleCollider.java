package no.hiof.framework30.brunost.physicsEngine.components;

import no.hiof.framework30.brunost.components.Component;

public class CircleCollider extends Collider {
    private float radius = 1f;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
