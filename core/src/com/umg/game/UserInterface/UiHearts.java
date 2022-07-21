package com.umg.game.UserInterface;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.umg.game.IsaacGame;

public class UiHearts {

    private Texture texture;
    private int healthCount;

    public UiHearts(Texture texture){
        this.texture = texture;

    }

    public void update(float dt, int playerHealth){
        healthCount = playerHealth;
    }

    public void render(SpriteBatch batch, Vector2 cameraPos){

        int shift = 50;
        for (int i = 0; i < healthCount / 10; i++) {
            batch.draw(texture, cameraPos.x + shift * i - 300, cameraPos.y - IsaacGame.V_HEIGHT / 2  + 50, 32, 32);

        }
    }

    public void dispose(){
        texture.dispose();
    }
}
