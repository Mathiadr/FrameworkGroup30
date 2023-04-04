package no.hiof.framework30.brunost.components;

import no.hiof.framework30.brunost.gameObjects.Component;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class RigidBody extends Component {
    private int colliderType = 0;
    private float friction = 0.8f;
    public Vector3f velocity = new Vector3f(0, 0.5f, 0);
    public transient Vector4f temp = new Vector4f(0,0,0,0);

    public int getColliderType() {
        return colliderType;
    }

    public void setColliderType(int colliderType) {
        this.colliderType = colliderType;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public Vector4f getTemp() {
        return temp;
    }

    public void setTemp(Vector4f temp) {
        this.temp = temp;
    }
}
