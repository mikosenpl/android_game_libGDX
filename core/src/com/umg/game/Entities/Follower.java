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
import com.umg.game.Screens.PlayState;

public class Follower extends Enemy{

    private static final float TIME_BETWEEN_FRAMES = 0.1f;
    private static final float BASIC_SPEED = 2;
    private static final float TILEMAP_SCALE = PlayState.TILEMAP_SCALE;
    public static final Vector2 BODY_SIZE = new Vector2(Isaac.BODY_SIZE);
    private static final float TIME_BETWEEN_FRAMES_DESTROY = 0.02f;
    private static final Vector2 HEAD_SHIFT = new Vector2(5, 26);

    private moveState followMoveState;

    private Texture headTexture;

    private float stateTime;

    private Animation animation;
    private Animation walkUpDownAnimation;
    private Animation walkRightAnimation;
    private Animation walkLeftAnimation;
    private Animation deathAnimation;

    public Follower(ResourceManager resources, Vector2 startPos, int room) {
        this.room = room;
        this.texture = resources.getTexture(ResourceManager.isaacTexture);
        headTexture = resources.getTexture(ResourceManager.followHead);
        this.position = startPos;
        damage = 15;
        health = 55;
        stateTime = 0;
        followMoveState = moveState.DOWN;
        rectangle = new Rectangle(startPos.x, startPos.y, 60, 60);
        velocity = new Vector2(0, 0);

        deathSound = resources.getSound(ResourceManager.enemyDies);

        createAnimations(resources);
        animation = walkUpDownAnimation;
    }

    private void createAnimations(ResourceManager resources){

        TextureRegion[][] splited = TextureRegion.split(texture, 32, 32);
        TextureRegion[][] mirrored = TextureRegion.split(texture, 32, 32);

        for (TextureRegion[] regionRow: mirrored){
            for (TextureRegion regionCol: regionRow) {
                regionCol.flip(true, false);
            }
        }

        walkUpDownAnimation = new Animation(TIME_BETWEEN_FRAMES, splited[0][6], splited[0][7], splited[1][0],
                splited[1][1], splited[1][2], splited[1][3], splited[1][4], splited[1][5],
                splited[1][6], splited[1][7]);

        walkRightAnimation = new Animation(TIME_BETWEEN_FRAMES, splited[2][0], splited[2][1], splited[2][2],
                splited[2][3], splited[2][4], splited[2][5], splited[2][6], splited[2][7],
                splited[3][0], splited[3][1]);

        walkLeftAnimation = new Animation(TIME_BETWEEN_FRAMES, mirrored[2][0], mirrored[2][1], mirrored[2][2],
                mirrored[2][3], mirrored[2][4], mirrored[2][5], mirrored[2][6], mirrored[2][7],
                mirrored[3][0], mirrored[3][1]);

        Texture destroyTexture = resources.getTexture(ResourceManager.enemyDestroyTexture);
        TextureRegion[] deathFrames = com.umg.game.ResourcesStorage.Animation.getFramesArray1D(destroyTexture, 3, 4);
        deathAnimation = new Animation(TIME_BETWEEN_FRAMES_DESTROY, deathFrames);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (liveState == aliveState.ALIVE) {
            batch.draw((TextureRegion) animation.getKeyFrame(stateTime, true), position.x, position.y,BODY_SIZE.x * 2, BODY_SIZE.y * 2);
            batch.draw(headTexture, position.x + HEAD_SHIFT.x, position.y + HEAD_SHIFT.y);
        }
        else {
            batch.draw((TextureRegion) animation.getKeyFrame(stateTime, true), position.x - 50, position.y - 10);
        }
    }

    @Override
    public void updatePosition(Vector2 playerPos){

        float diffX = playerPos.x - position.x;
        float diffY = playerPos.y - position.y;

        double angle = Math.atan2(diffY, diffX);
        velocity.x = (float)(BASIC_SPEED * Math.cos(angle));
        velocity.y = (float)(BASIC_SPEED * Math.sin(angle));


        if (velocity.y > 0.8){
            followMoveState = moveState.UP;
        }
        else if (velocity.y < -0.8){
            followMoveState = moveState.DOWN;
        }
        else if (velocity.x > 0.5){
            followMoveState = moveState.RIGHT;
        }
        else if (velocity.x < -0.5){
            followMoveState = moveState.LEFT;
        }
        else{
            followMoveState = moveState.NONE;
            this.velocity.x = 0;
            this.velocity.y = 0;
        }

    }

    private void setPosition(){
        position.x += velocity.x;
        position.y += velocity.y;
        rectangle.setPosition(this.position);
    }

    private boolean isCollides(MapObjects collidable){
        for (MapObject wall : collidable) {
            Rectangle rect = ((RectangleMapObject) wall).getRectangle();
            if (getRectangle().overlaps(new Rectangle(rect.getX() * TILEMAP_SCALE, rect.getY() * TILEMAP_SCALE, rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE))){
                return true;
            }
        }
        return false;
    }

    private void setAnimation(){
        switch (followMoveState){
            case UP:
                animation = walkUpDownAnimation;
                break;
            case DOWN:
                animation = walkUpDownAnimation;
                break;
            case RIGHT:
                animation = walkRightAnimation;
                break;
            case LEFT:
                animation = walkLeftAnimation;
                break;
        }
    }

    @Override
    public void update(float dt, MapObjects collidable) {

        isAlive = health > 0;

        stateTime += dt;

        if (isAlive) {

            Vector2 oldPosition = new Vector2(rectangle.getX(), rectangle.getY());
            setPosition();
            rectangle.setPosition(position);
            if (isCollides(collidable)) {
                rectangle.setPosition(oldPosition);
                if (position.x != oldPosition.x && position.y != oldPosition.y) {
                    rectangle.setPosition(oldPosition.x, position.y);
                    if (isCollides(collidable)) {
                        rectangle.setPosition(position.x, oldPosition.y);
                        if (isCollides(collidable)) {
                            rectangle.setPosition(oldPosition);
                        }
                    }
                }
            }

            position.x = rectangle.getX();
            position.y = rectangle.getY();
            setAnimation();
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
    }

    @Override
    public void dispose() {

    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(rectangle.getX() + 15, rectangle.getY() + 10,
                rectangle.getWidth() * 0.65f, rectangle.getHeight() * 1.25f);
    }

    @Override
    public void getDamage(int dmg) {
        this.health -= dmg;
    }
}
