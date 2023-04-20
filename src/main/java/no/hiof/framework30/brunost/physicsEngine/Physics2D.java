package no.hiof.framework30.brunost.physicsEngine;

import no.hiof.framework30.brunost.components.Transform;
import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.physicsEngine.components.Box2DCollider;
import no.hiof.framework30.brunost.physicsEngine.components.CircleCollider;
import no.hiof.framework30.brunost.physicsEngine.components.RigidBody2D;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

public class Physics2D {

    private Vec2 gravity = new Vec2(0, -10.0f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 60.0f;
    private int velocityIterations = 8;
    private int positionIterations = 3;


    public void onUpdate(float deltaTime){
        physicsTime += deltaTime;
        if (physicsTime >= 0.0f){
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }

    public void add (GameObject gameObject){
        RigidBody2D rb = gameObject.getComponent(RigidBody2D.class);
        if (rb != null && rb.getRawBody() == null){
            Transform transform = gameObject.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float)Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();
            bodyDef.linearDamping = rb.getLinearDamping();
            bodyDef.fixedRotation = rb.isFixedRotation();
            bodyDef.bullet = rb.isContinuousCollision();

            switch (rb.getBodyType()) {
                case Kinematic: bodyDef.type = BodyType.KINEMATIC;   break;
                case Static:    bodyDef.type = BodyType.STATIC;      break;
                case Dynamic:   bodyDef.type = BodyType.DYNAMIC;     break;
            }

            PolygonShape shape = new PolygonShape();
            CircleCollider circleCollider;
            Box2DCollider boxCollider;
            if ((circleCollider = gameObject.getComponent(CircleCollider.class)) != null){
                shape.setRadius(circleCollider.getRadius());
            } else if ((boxCollider = gameObject.getComponent(Box2DCollider.class)) != null){
                Vector2f halfSize = new Vector2f(boxCollider.getHalfSize()).mul(0.5f);
                Vector2f offset = boxCollider.getOffset();
                Vector2f origin = new Vector2f(boxCollider.getOrigin());
                shape.setAsBox(halfSize.x, halfSize.y, new Vec2(origin.x, origin.y), 0);

                Vec2 pos = bodyDef.position;
                float xPos = pos.x + offset.x;
                float yPos = pos.y + offset.y;
                bodyDef.position.set(xPos, yPos);
            }

            Body body = this.world.createBody(bodyDef);
            rb.setRawBody(body);
            body.createFixture(shape, rb.getMass());
        }
    }

    public void destroyGameObject(GameObject gameObject){
        RigidBody2D rb = gameObject.getComponent(RigidBody2D.class);
        if (rb != null){
            if (rb.getRawBody() != null){
                world.destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
    }

}
