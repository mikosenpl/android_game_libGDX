package com.umg.game.Entities;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy extends Entity {

    public int room;
    protected Sound deathSound;

    public abstract void getDamage(int dmg);
    public abstract void updatePosition(Vector2 neededPosition);
}
