package com.umg.game.Entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.umg.game.ResourcesStorage.ResourceManager;

public class Bonus extends Entity {

    public enum bonusType{
        HP,
        MOVE_SPEED,
        ATACK_SPEED,
        DAMAGE
    }

    public bonusType type;
    private int room;
    private BitmapFont font;
    public boolean isTaken;

    public Bonus(ResourceManager resources, Vector2 position, int room){
        this.room = room;
        rectangle = new Rectangle(position.x, position.y, 50, 50);
        type = bonusType.values()[(int)(Math.random()*4)];
        setTexture(resources);
        font = resources.getFont();
        isTaken = false;
    }

    private void setTexture(ResourceManager resources){
        switch (type){
            case HP:
                texture = resources.getTexture(ResourceManager.hpBonusTexture);
                break;
            case MOVE_SPEED:
                texture = resources.getTexture(ResourceManager.msBonusTexture);
                break;
            case ATACK_SPEED:
                texture = resources.getTexture(ResourceManager.asBonusTexture);
                break;
            case DAMAGE:
                texture = resources.getTexture(ResourceManager.dmgBonusTexture);
                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {

        if (!isTaken) {
            batch.draw(texture, rectangle.getX(), rectangle.getY(), 50, 50);

            String bonusStr = "";
            switch (type) {
                case HP:
                    bonusStr = "HEAL";
                    break;
                case MOVE_SPEED:
                    bonusStr = "MOVE SPEED UPGRADE";
                    break;
                case ATACK_SPEED:
                    bonusStr = "ATACK SPEED UPGRADE";
                    break;
                case DAMAGE:
                    bonusStr = "DAMAGE UPGRADE";
                    break;
            }
            font.draw(batch, bonusStr, rectangle.getX() - 200, rectangle.getY() + 350);
        }
    }

    @Override
    public void update(float dt, MapObjects collidable) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getRoom(){
        return room;
    }

}
