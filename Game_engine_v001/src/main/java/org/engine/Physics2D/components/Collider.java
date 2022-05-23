package org.engine.Physics2D.components;

import org.engine.Rendering.Objects.Components.Component;
import org.jbox2d.collision.Collision;
import org.joml.Vector2f;

public abstract class Collider extends Component {
    protected Vector2f offset = new Vector2f();//смещение для более удобной настройки коллайдера

    public Vector2f getOffset() {
        return offset;
    }
}
