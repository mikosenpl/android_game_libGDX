package com.umg.game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

    public enum moveState{
        NONE,
        RIGHT,
        LEFT,
        UP,
        DOWN
    }

    public enum aliveState{
        ALIVE,
        DYING,
        DEAD
    }

    public aliveState liveState = aliveState.ALIVE;
    public Vector2 position = new Vector2(0, 0);
    public Vector2 size = new Vector2(0, 0);
    public Texture texture = null;
    public Vector2 velocity = new Vector2(0, 0);
    public int damage = 0;
    public int health = 0;
    public Rectangle rectangle = null;
    public boolean isAlive = true;

    public abstract void render(SpriteBatch batch);
    public abstract void update(float dt, MapObjects collidable);
    public abstract void dispose();
    public abstract Rectangle getRectangle();
}
