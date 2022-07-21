package com.umg.game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.umg.game.ResourcesStorage.ResourceManager;

public class Worm extends Enemy{

    private static final float CHANGE_DIR_TIME = 3;
    private static final Vector2 BODY_SIZE = new Vector2(64, 64);
    private static final float TIME_BETWEEN_FRAMES = 0.15f;
    private static final float BASIC_SPEED = 2;
    private static final float TILEMAP_SCALE = 2.75f;
    private static final float TIME_BETWEEN_FRAMES_DESTROY = 0.02f;

    private float stateTime;
    private float lastChangeDirTime;

    private Animation animation;
    private Animation walkUp;
    private Animation walkDown;
    private Animation walkLeft;
    private Animation walkRight;
    private Animation deathAnimation;

    private moveState wormMoveState;

    public Worm(ResourceManager resources, Vector2 startPos, int room){
        this.position = startPos;
        this.texture = resources.getTexture(ResourceManager.wormTexture);
        stateTime = 0;
        lastChangeDirTime = 0;
        damage = 20;
        health = 45;
        this.room = room;
        deathSound = resources.getSound(ResourceManager.enemyDies);

        rectangle = new Rectangle(position.x, position.y, BODY_SIZE.x * 0.75f, BODY_SIZE.y * 0.75f);

        wormMoveState = moveState.values()[(int)(Math.random()*4 + 1)];

        createAnimations(resources);
        setAnimation();
    }

    private void createAnimations(ResourceManager resources){
        TextureRegion[][] frames = TextureRegion.split(texture, 64, 64);

        walkUp = new Animation(TIME_BETWEEN_FRAMES, frames[1][0], frames[1][1], frames[1][2], frames[1][3]);
        walkDown = new Animation(TIME_BETWEEN_FRAMES, frames[2][0], frames[2][1], frames[2][2], frames[2][3]);
        walkLeft = new Animation(TIME_BETWEEN_FRAMES, frames[4][0], frames[4][1], frames[4][2], frames[4][3]);
        walkRight = new Animation(TIME_BETWEEN_FRAMES, frames[0][0], frames[0][1], frames[0][2], frames[0][3]);

        Texture destroyTexture = resources.getTexture(ResourceManager.enemyDestroyTexture);
        TextureRegion[] deathFrames = com.umg.game.ResourcesStorage.Animation.getFramesArray1D(destroyTexture, 3, 4);
        deathAnimation = new Animation(TIME_BETWEEN_FRAMES_DESTROY, deathFrames);
    }

    private void setAnimation(){

        switch (wormMoveState){
            case UP:
                animation = walkUp;
                break;
            case DOWN:
                animation = walkDown;
                break;
            case RIGHT:
                animation = walkRight;
                break;
            case LEFT:
                animation = walkLeft;
                break;
        }
    }

    @Override
    public void getDamage(int dmg) {
        health -= dmg;
    }

    @Override
    public void updatePosition(Vector2 neededPosition) {

    }

    @Override
    public void render(SpriteBatch batch) {

        if (liveState == aliveState.ALIVE) {
            batch.draw((TextureRegion) animation.getKeyFrame(stateTime, true), position.x, position.y);
        }
        else {
            batch.draw((TextureRegion) animation.getKeyFrame(stateTime, true), position.x - 50, position.y - 10);
        }
    }

    @Override
    public void update(float dt, MapObjects collidable) {

        isAlive = health > 0;

        stateTime += dt;

        if (stateTime > lastChangeDirTime + CHANGE_DIR_TIME){
            wormMoveState = moveState.values()[(int)(Math.random()*4 + 1)];
            lastChangeDirTime = stateTime;

            setAnimation();
        }

        if (liveState == aliveState.ALIVE) {
            switch (wormMoveState) {
                case UP:
                    position.y += BASIC_SPEED;
                    break;
                case DOWN:
                    position.y -= BASIC_SPEED;
                    break;
                case RIGHT:
                    position.x += BASIC_SPEED;
                    break;
                case LEFT:
                    position.x -= BASIC_SPEED;
                    break;
            }
        }

        if (!isAlive && liveState == aliveState.ALIVE){
            animation = deathAnimation;
            liveState = aliveState.DYING;
            stateTime = 0;
            deathSound.play();
        }

        if (liveState == aliveState.DYING){
            if (deathAnimation.isAnimationFinished(stateTime)){
                liveState = aliveState.DEAD;
            }
        }

        rectangle.setPosition(position.x + 10, position.y + 10);

        checkCollision(collidable);

    }

    public void checkCollision(MapObjects collidable){
        boolean isCollides = false;
        for (MapObject wall : collidable){
            if (wall instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) wall).getRectangle();
                Rectangle wormRect = new Rectangle(rectangle.getX(), rectangle.getY(),
                        rectangle.getWidth(), rectangle.getHeight() * 2);
                if (wormRect.overlaps(new Rectangle(rect.getX() * TILEMAP_SCALE, rect.getY() * TILEMAP_SCALE,
                        rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE))){
                    isCollides = true;
                    break;
                }
            }
        }

        if (isCollides){
            switch (wormMoveState){
                case UP:
                    position.y -= BASIC_SPEED * 2;
                    break;
                case DOWN:
                    position.y += BASIC_SPEED * 2;
                    break;
                case RIGHT:
                    position.x -= BASIC_SPEED * 2;
                    break;
                case LEFT:
                    position.x += BASIC_SPEED * 2;
                    break;
            }

            wormMoveState = moveState.values()[(int)(Math.random()*4 + 1)];
            lastChangeDirTime = stateTime;
            setAnimation();
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }
}
