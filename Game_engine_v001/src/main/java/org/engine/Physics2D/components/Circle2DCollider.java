package org.engine.Physics2D.components;

import org.engine.Rendering.Objects.Components.Component;

public class Circle2DCollider extends Collider {
    private float radius = 1f;//радиус кругового коллайдера

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
