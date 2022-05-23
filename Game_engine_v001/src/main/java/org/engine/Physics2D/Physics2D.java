package org.engine.Physics2D;

import org.engine.Physics2D.components.Box2DCollider;
import org.engine.Physics2D.components.Circle2DCollider;
import org.engine.Physics2D.components.RigidBody2D;
import org.engine.Rendering.Objects.Components.Transform;
import org.engine.Rendering.Objects.GameObject;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

public class Physics2D {
    //настройка библиотек jbox
    private Vec2 gravity = new Vec2(0, -10.0f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f; //скорость обновления физики
    private float physicsTimeStep = 1.0f / 60.0f; //шаг обновления физики
    private int velocityIterations = 8;//скоростаня итерация для jbox
    private int positionIterations = 3;//позиционная итерация для jbox


    public void update(float dt){
        physicsTime += dt;
        if(physicsTime >= 0.0f){
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);//проброс настроек
        }


    }


    public void add(GameObject gameObject){//добавление физического движка к объекту
        RigidBody2D rb = gameObject.getComponent(RigidBody2D.class);
        if(rb != null && rb.getRawBody() == null){
            Transform transform = gameObject.transform;

            BodyDef bodyDef = new BodyDef();//body definition описание тела
            bodyDef.angle = (float)Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();//угловое трение
            bodyDef.linearDamping = rb.getLinearDamping();//линейное трение
            bodyDef.fixedRotation = rb.isFixedRotation();//можно или нельзя вращаться
            bodyDef.bullet = rb.isContinuousCollision();//если включено, то объекту будет сложно на высокой скорости вылететь за коллизию

            switch (rb.getBodyType()){
                case Kinematic -> {
                    bodyDef.type = BodyType.KINEMATIC;
                }
                case Dynamic -> {
                    bodyDef.type = BodyType.DYNAMIC;
                }
                case Static -> {
                    bodyDef.type = BodyType.STATIC;
                }
            }

            PolygonShape polygonShape = new PolygonShape();
            Circle2DCollider circle2DCollider = null;
            Box2DCollider box2DCollider = null;

            if((circle2DCollider = gameObject.getComponent(Circle2DCollider.class)) != null){
                polygonShape.setRadius(circle2DCollider.getRadius());
            }else if((box2DCollider = gameObject.getComponent(Box2DCollider.class)) != null){
                Vector2f halfSize = new Vector2f(box2DCollider.getHalfSize()).mul(0.5f);//?
                Vector2f offset = box2DCollider.getOffset();
                Vector2f origin = new Vector2f(box2DCollider.getOrigin());
                polygonShape.setAsBox(halfSize.x, halfSize.y, new Vec2(origin.x, origin.y), 0);//форма нашего полигона(коллайдера)

                //учет настройки смещения
                Vec2 pos = bodyDef.position;
                float xPos = pos.x + offset.x;
                float yPos = pos.y + offset.y;
                bodyDef.position.set(xPos, yPos);
            }

            Body body = this.world.createBody(bodyDef);//добавление в на сцену
            rb.setRawBody(body);
            body.createFixture(polygonShape, rb.getMass());//создаем форму полигона
        }

    }

    public void destroyGameObject(GameObject go) {//удаление физического тела из игровой сцены
        RigidBody2D rb = go.getComponent(RigidBody2D.class);
        if(rb != null){
            if(rb.getRawBody() != null){
                world.destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
    }
}
