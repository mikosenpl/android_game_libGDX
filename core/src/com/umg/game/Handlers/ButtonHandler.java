package com.umg.game.Handlers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ButtonHandler {

    private boolean isTouched;
    private Texture texture;
    private Rectangle rectangle;

    public ButtonHandler(Texture texture, Vector2 position, Vector2 size){
        isTouched = false;
        this.texture = texture;
        this.rectangle = new Rectangle(position.x, position.y, size.x, size.y);
    }

    public void updatePosition(float x, float y){
        rectangle.setPosition(x, y);
    }

    public void onClick(){
        isTouched = true;
    }

    public boolean isOnRelease(){
        return isTouched;
    }

    public void notTouched(){
        isTouched = false;
    }

    public Rectangle getRectangle(){
        return rectangle;
    }

    public void render(SpriteBatch batch){
        batch.draw(texture, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    public void dispose(){
        texture.dispose();
    }
}
