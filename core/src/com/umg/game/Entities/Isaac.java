package com.umg.game.Entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

import java.util.Vector;

public class Isaac extends Entity{

    private enum headState{
        DOWN,
        DOWN_SHOOT,
        RIGHT,
        RIGHT_SHOOT,
        UP,
        UP_SHOOT,
        LEFT,
        LEFT_SHOOT
    }

    public static final Vector2 BODY_SIZE = new Vector2(36, 29);
    private static final int BASIC_VELOCITY = 5;
    private static final float TIME_BETWEEN_FRAMES = 0.1f;
    private static final float TILEMAP_SCALE = PlayState.TILEMAP_SCALE;
    private static final float TIME_BETWEEN_SHOOTS = 1f;
    private static final float DAMAGE_TAKE_TIME_PAUSE = 1;
    private static final int MAX_HEALTH = 150;

    private float velocityBonus;
    private float stateTime;
    private float lastHitTime;
    private float lastShootTime;
    private float timeBetweenShoots;

    private ResourceManager manager;

    private moveState playerMoveState;

    private Animation animation;
    private Animation walkUpDownAnimation;
    private Animation walkRightAnimation;
    private Animation walkLeftAnimation;
    private Animation standAnimation;

    private TextureRegion[] headFrames;
    private headState shootState = headState.DOWN;

    public Isaac (ResourceManager resources, Vector2 startPosition){

        this.manager = resources;
        this.texture = manager.getTexture(ResourceManager.isaacTexture);
        Texture headTexture = manager.getTexture(ResourceManager.isaacHeadTexture);
        headFrames = com.umg.game.ResourcesStorage.Animation.getFramesArray1D(headTexture, 8, 1);

        playerMoveState = moveState.NONE;
        initPosition(startPosition);
        velocity = new Vector2();
        velocityBonus = 1;
        stateTime = 0;
        lastHitTime = 0;
        timeBetweenShoots = TIME_BETWEEN_SHOOTS;
        size = BODY_SIZE;
        damage = 20;
        health = 50;

        rectangle = new Rectangle(position.x, position.y, size.x * 1.75f, size.y * 3f);

        createAnimations();
        animation = standAnimation;

    }

    public void initPosition(Vector2 pos){
        position = pos;
    }

    private void createAnimations(){

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

        standAnimation = new Animation(TIME_BETWEEN_FRAMES, splited[1][0]);
    }

    @Override
    public Rectangle getRectangle(){
        return rectangle;
    }

    public void inputHandler(Vector2 velocity){

        this.velocity.x = velocity.x * BASIC_VELOCITY * velocityBonus;
        this.velocity.y = velocity.y * BASIC_VELOCITY * velocityBonus;

        if (velocity.y > 0.8){
            playerMoveState = moveState.UP;
        }
        else if (velocity.y < -0.8){
            playerMoveState = moveState.DOWN;
        }
        else if (velocity.x > 0.5){
            playerMoveState = moveState.RIGHT;
        }
        else if (velocity.x < -0.5){
            playerMoveState = moveState.LEFT;
        }
        else{
            playerMoveState = moveState.NONE;
            this.velocity.x = 0;
            this.velocity.y = 0;
        }

    }

    public void collisionWithDoors(MapObjects doors, OrthographicCamera camera, boolean isRoomCleared){
        if (!isRoomCleared){
            return;
        }

        for (MapObject door : doors) {
            Rectangle rect = ((RectangleMapObject) door).getRectangle();
            if (getRectangle().overlaps(new Rectangle(rect.getX() * TILEMAP_SCALE, rect.getY() * TILEMAP_SCALE, rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE))){
                if (door.getName().equals("right")){
                    position.x += 400;
                    camera.position.x += camera.viewportWidth + 40;
                }
                else if (door.getName().equals("left")){
                    position.x -= 400;
                    camera.position.x -= camera.viewportWidth + 40;
                }
                else if (door.getName().equals("up")){
                    position.y += 280;
                    camera.position.y += camera.viewportHeight - 15;
                }
                else if (door.getName().equals("down")){
                    position.y -= 280;
                    camera.position.y -= camera.viewportHeight - 15;
                }
            }
        }
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
        switch (playerMoveState){
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
            case NONE:
                animation = standAnimation;
                break;
        }
    }

    private void setPosition(){
        position.x += velocity.x;
        position.y += velocity.y;
        rectangle.setPosition(this.position);
    }

    public void setVelocity(Vector2 velocity){
        this.velocity = velocity;
    }

    public void update(float dt, MapObjects collidable){

        isAlive = health > 0;

        if (isAlive) {
            stateTime += dt;
            inputHandler(velocity);
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
    }

    @Override
    public void render(SpriteBatch batch){
        if (isAlive) {
            batch.draw((TextureRegion) animation.getKeyFrame(stateTime, true), rectangle.getX() - 20, rectangle.getY() - 20, BODY_SIZE.x * 3, BODY_SIZE.y * 3);
            batch.draw(headFrames[shootState.ordinal()], rectangle.getX() - 15, rectangle.getY() + 5, 32 * 3, 32 * 3);
        }
        else{
            batch.draw((TextureRegion) animation.getKeyFrame(stateTime, true), rectangle.getX() - 20, rectangle.getY() - 20, BODY_SIZE.x * 3, BODY_SIZE.y * 3);
        }
    }

    public int getHealth(){
        return health;
    }

    public void getDamage(int dmg){
        if (stateTime > lastHitTime + DAMAGE_TAKE_TIME_PAUSE || lastHitTime == 0){
            Sound hurtSound = manager.getSound(ResourceManager.playerHurts);
            hurtSound.play();
            health -= dmg;
            lastHitTime = stateTime;
        }
    }

    public int getCurrentDamage(){
        return damage;
    }

    public void shoot(Vector<Bullet> bullets, Vector2 bulletVelocity){
        if (bulletVelocity.x != 0f && bulletVelocity.y != 0f) {
            if (stateTime > lastShootTime + timeBetweenShoots || lastShootTime == 0) {
                bullets.add(new Bullet(manager, new Vector2(this.position.x + size.x / 2
                        , this.position.y + size.y), bulletVelocity));
                lastShootTime = stateTime;
            }
        }

        if (bulletVelocity.y > 0.8){
            shootState = stateTime > lastShootTime + timeBetweenShoots / 5 ? headState.UP : headState.UP_SHOOT;

        }
        else if (bulletVelocity.y < -0.8){
            shootState = stateTime > lastShootTime + timeBetweenShoots / 5 ? headState.DOWN : headState.DOWN_SHOOT;
        }
        else if (bulletVelocity.x > 0.5){
            shootState = stateTime > lastShootTime + timeBetweenShoots / 5 ? headState.RIGHT : headState.RIGHT_SHOOT;
        }
        else if (bulletVelocity.x < -0.5){
            shootState = stateTime > lastShootTime + timeBetweenShoots / 5 ? headState.LEFT : headState.LEFT_SHOOT;
        }
        else {
            shootState = stateTime > lastShootTime + timeBetweenShoots / 5 ? headState.DOWN : headState.DOWN_SHOOT;

        }
    }

    public void increaseHealth(int upgradeBonus){
        if (health < MAX_HEALTH) {
            this.health += upgradeBonus;
        }
    }

    public void increaseSpeed(float upgradeBonus){
        this.velocityBonus += upgradeBonus;
    }

    public void decreaseTimeBetweenShoots(float upgradeBonus){
        this.timeBetweenShoots -= upgradeBonus;
    }

    public void increaseDamage(float upgradeBonus){
        this.damage += upgradeBonus;
    }

    @Override
    public void dispose(){

    }
}
