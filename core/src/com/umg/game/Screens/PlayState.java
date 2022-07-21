package com.umg.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.Iterator;
import java.util.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.umg.game.Entities.Bonus;
import com.umg.game.Entities.Bullet;
import com.umg.game.Entities.Enemy;
import com.umg.game.Entities.Entity;
import com.umg.game.Entities.Fly;
import com.umg.game.Entities.Follower;
import com.umg.game.Entities.Isaac;
import com.umg.game.Entities.Worm;
import com.umg.game.Handlers.ButtonHandler;
import com.umg.game.IsaacGame;
import com.umg.game.ResourcesStorage.ResourceManager;
import com.umg.game.UserInterface.TouchPad;
import com.umg.game.UserInterface.UiHearts;

public class PlayState extends Screen {

    public static final float TILEMAP_SCALE = 2.75f;
    public static final Vector2 PAUSE_BTN_POS = new Vector2(-90, -90);

    public static final int INCREASE_HEALTH_BONUS = 10;
    public static final float INCREASE_SPEED = 0.15f;
    public static final float INCREASE_TIME_BETWEEN_SHOOTS = 0.05f;
    public static final int INCREASE_DAMAGE = 5;

    private TouchPad joystick;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;

    private Music music;
    private Sound newLvl;

    private MapObjects rooms;
    private MapObjects doors;
    private MapObjects solidObjects;
    private MapObject playerBox;
    private ShapeRenderer shapeRenderer;

    private int level = 1;
    private int room = 1;
    private Rectangle manholeRect;

    private boolean isRoomCleared;

    private Isaac player;
    private UiHearts uiHealthBar;
    private ButtonHandler pauseButton;

    private Vector<Enemy> enemies;
    private Vector<Bullet> bullets;
    private Vector<Bonus> bonuses;


    public PlayState(GameScreenManager gsm, ResourceManager resources){
        super(gsm, resources);

        onNewLevel();
        newLvl = resources.getSound(ResourceManager.levelStart);
        newLvl.play();
        music = resources.getMusic(ResourceManager.bgMusic);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        player = new Isaac(resources, new Vector2(((RectangleMapObject) playerBox).getRectangle().getX() * TILEMAP_SCALE,
                ((RectangleMapObject) playerBox).getRectangle().getY() * TILEMAP_SCALE));
    }

    private void initializeData(){

        if (level == 3){
            gsm.push(new EndGameState(gsm, resources, true));
        }

        camera.position.set(IsaacGame.V_WIDTH, IsaacGame.V_HEIGHT, 0);
        camera.setToOrtho(false, IsaacGame.V_WIDTH, IsaacGame.V_HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        joystick = new TouchPad(camera);
        bullets = new Vector<Bullet>();
        enemies = new Vector<Enemy>();
        bonuses = new Vector<Bonus>();
        shapeRenderer = new ShapeRenderer();
        renderer = new OrthogonalTiledMapRenderer(map, TILEMAP_SCALE);
        isRoomCleared = false;

        readFromMap();

        uiHealthBar = new UiHearts(resources.getTexture(ResourceManager.uiHeartTexture));

        pauseButton = new ButtonHandler(resources.getTexture(ResourceManager.pauseBtnTexture),
                new Vector2(camera.position.x + camera.viewportWidth / 2 + PAUSE_BTN_POS.x,
                        camera.position.y + camera.viewportHeight / 2 + PAUSE_BTN_POS.y),
                new Vector2(55, 55));

        if (level > 1) {
            player.initPosition(new Vector2(((RectangleMapObject) playerBox).getRectangle().getX() * TILEMAP_SCALE,
                    ((RectangleMapObject) playerBox).getRectangle().getY() * TILEMAP_SCALE));
        }

    }

    private void readFromMap(){
        rooms = map.getLayers().get("rooms").getObjects();
        doors = map.getLayers().get("doors").getObjects();
        solidObjects = map.getLayers().get("solid").getObjects();
        playerBox = map.getLayers().get("player").getObjects().get(0);
        MapObject manhole = map.getLayers().get("manhole").getObjects().get(0);
        manholeRect = new Rectangle(((RectangleMapObject) manhole).getRectangle());
        manholeRect.setPosition(manholeRect.getX() * TILEMAP_SCALE, manholeRect.getY() * TILEMAP_SCALE);
        manholeRect.setSize(manholeRect.getWidth() * TILEMAP_SCALE, manholeRect.getHeight() * TILEMAP_SCALE);
    }

    private void onNewLevel(){
        switch (level){
            case 1:
                map = resources.getLevel(ResourceManager.mapLevel1);
                break;
            case 2:
                map = resources.getLevel(ResourceManager.mapLevel2);
                break;
        }

        initializeData();
        initializeEnemies();
    }


    private void initializeEnemies(){
        MapObjects flyOnMap;
        flyOnMap =  map.getLayers().get("fly").getObjects();
        for (MapObject f: flyOnMap){
            int enemyRoom = Integer.decode(f.getProperties().get("room", String.class));
            enemies.add(new Fly(resources, new Vector2(((RectangleMapObject)f).getRectangle().getX() * TILEMAP_SCALE,
                ((RectangleMapObject)f).getRectangle().getY() * TILEMAP_SCALE), enemyRoom));
        }

        MapObjects wormsOnMap;
        wormsOnMap =  map.getLayers().get("worms").getObjects();
        for (MapObject worm: wormsOnMap){
            int enemyRoom = Integer.decode(worm.getProperties().get("room", String.class));
            enemies.add(new Worm(resources, new Vector2(((RectangleMapObject)worm).getRectangle().getX() * TILEMAP_SCALE,
                    ((RectangleMapObject)worm).getRectangle().getY() * TILEMAP_SCALE), enemyRoom));
        }

        MapObjects followOnMap;
        followOnMap =  map.getLayers().get("follow").getObjects();
        for (MapObject follow: followOnMap){
            int enemyRoom = Integer.decode(follow.getProperties().get("room", String.class));
            enemies.add(new Follower(resources, new Vector2(((RectangleMapObject)follow).getRectangle().getX() * TILEMAP_SCALE,
                    ((RectangleMapObject)follow).getRectangle().getY() * TILEMAP_SCALE), enemyRoom));
        }
    }

    @Override
    public void inputHandler() {

        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        if (pauseButton.getRectangle().contains(mousePosition.x, mousePosition.y) && Gdx.input.justTouched()){
            pauseButton.onClick();
        }
        else{
            pauseButton.notTouched();
        }

        if (pauseButton.isOnRelease()){
            music.stop();
            gsm.push(new PauseState(gsm, resources));
        }
    }

    private void checkIntersectionsOfEntities(){

        for (Enemy entity: enemies){
            if (entity.getRectangle().overlaps(player.getRectangle())){
                player.getDamage(entity.damage);
            }

            for (Bullet bullet : bullets){
                if (entity.getRectangle().overlaps(bullet.getRectangle()) && bullet.liveState == Entity.aliveState.ALIVE){
                    entity.getDamage(player.getCurrentDamage());
                    bullet.onDestroy();
                }
            }
        }

        if (player.getRectangle().overlaps(manholeRect)){

            newLvl.play();
            level++;
            onNewLevel();
        }

        for (Bonus bonus : bonuses){
            if (bonus.getRectangle().overlaps(player.getRectangle()) && !bonus.isTaken){
                Sound pickUp = resources.getSound(ResourceManager.pickUp);
                pickUp.play();

                switch (bonus.type){
                    case HP:
                        player.increaseHealth(INCREASE_HEALTH_BONUS);
                        bonus.isTaken = true;
                        break;
                    case MOVE_SPEED:
                        player.increaseSpeed(INCREASE_SPEED);
                        bonus.isTaken = true;
                        break;
                    case ATACK_SPEED:
                        player.decreaseTimeBetweenShoots(INCREASE_TIME_BETWEEN_SHOOTS);
                        bonus.isTaken = true;
                        break;
                    case DAMAGE:
                        player.increaseDamage(INCREASE_DAMAGE);
                        bonus.isTaken = true;
                        break;
                }
            }
        }

    }

    private void calculateRoom(){
        for (MapObject r : rooms){
            Rectangle rect = ((RectangleMapObject) r).getRectangle();
            if (player.getRectangle().overlaps(new Rectangle(rect.getX() * TILEMAP_SCALE,
                    rect.getY() * TILEMAP_SCALE, rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE))){
                room = Integer.parseInt(r.getName());
            }
        }
    }

    private void checkForCleaningRoom(){
        boolean isCleared = true;
        for (Enemy enemy : enemies){
            if (enemy.room == room){
                isCleared = false;
            }
        }

        isRoomCleared = isCleared;
    }

    private void updateBonuses(){
        if (isRoomCleared){
            boolean isAlreadyInRoom = false;
            for (Bonus bonus : bonuses){
                if (room == bonus.getRoom()){
                    isAlreadyInRoom = true;
                }
            }
            if (!isAlreadyInRoom && room != 1) {
                Sound drop = resources.getSound(ResourceManager.bonusDrop);
                drop.play();
                bonuses.add(new Bonus(resources, new Vector2(camera.position.x, camera.position.y), room));
            }
        }
    }

    @Override
    public void update(float dt) {

        if (!music.isPlaying())
        {
            music.play();
        }

        pauseButton.updatePosition(camera.position.x + camera.viewportWidth / 2 + PAUSE_BTN_POS.x,
                camera.position.y + camera.viewportHeight / 2 + PAUSE_BTN_POS.y);

        inputHandler();
        camera.update();
        renderer.setView(camera);
        calculateRoom();

        player.setVelocity(joystick.getKnobPercentWalk());
        player.update(dt, solidObjects);
        player.collisionWithDoors(doors, camera, isRoomCleared);
        player.shoot(bullets, joystick.getKnobPercentShoot());
        if (!player.isAlive){
            gsm.push(new EndGameState(gsm, resources, false));
        }

        uiHealthBar.update(dt, player.getHealth());
        checkForCleaningRoom();

        for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();){
            Enemy entity = it.next();
            if (entity.room == room) {
                entity.update(dt, solidObjects);
                entity.updatePosition(player.position);
            }
            if (entity.liveState == Entity.aliveState.DEAD){
                it.remove();
            }
        }

        for (Iterator<Bullet> it = bullets.iterator(); it.hasNext();){
            Bullet bullet = it.next();
            bullet.update(dt, solidObjects);
            if (bullet.liveState == Entity.aliveState.DEAD){
                it.remove();
            }
        }

        checkIntersectionsOfEntities();
        updateBonuses();
    }

    public void debugRender(){

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (MapObject o: solidObjects){
            if (o instanceof RectangleMapObject){
                Rectangle rect = ((RectangleMapObject) o).getRectangle();
                shapeRenderer.rect(rect.getX() * TILEMAP_SCALE, rect.getY() * TILEMAP_SCALE, rect.getWidth() * TILEMAP_SCALE, rect.getHeight() * TILEMAP_SCALE);
            }
        }

        Rectangle rectP =  player.getRectangle();
        shapeRenderer.rect(rectP.getX(), rectP.getY(), rectP.getWidth(), rectP.getHeight());

        for (Enemy entity : enemies){
            Rectangle rectF =  entity.getRectangle();
            shapeRenderer.rect(rectF.getX(), rectF.getY(), rectF.getWidth(), rectF.getHeight());
        }

        for (Bullet bullet: bullets) {
            Rectangle rectF =  bullet.getRectangle();
            shapeRenderer.rect(rectF.getX(), rectF.getY(), rectF.getWidth(), rectF.getHeight());
        }

        for (Bonus bonus: bonuses) {
            Rectangle rectF =  bonus.getRectangle();
            shapeRenderer.rect(rectF.getX(), rectF.getY(), rectF.getWidth(), rectF.getHeight());
        }

        shapeRenderer.rect(pauseButton.getRectangle().getX(), pauseButton.getRectangle().getY(),
                pauseButton.getRectangle().getWidth(), pauseButton.getRectangle().getHeight());

        shapeRenderer.end();
    }

    @Override
    public void render(SpriteBatch batch) {


        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        int[] backgroundLayers = { 0, 1 };
        renderer.render(backgroundLayers);

        if (isRoomCleared){
            int[] openedDoorsLayer = { 2 };
            renderer.render(openedDoorsLayer);
        }
        else{
            int[] closedDoorsLayer = { 3 };
            renderer.render(closedDoorsLayer);
        }

        batch.begin();
        player.render(batch);

        for (Enemy entity : enemies){
            entity.render(batch);
        }

        for (Bullet bullet: bullets) {
            bullet.render(batch);
        }

        for (Bonus bonus: bonuses) {
            bonus.render(batch);
        }

        uiHealthBar.render(batch, new Vector2(camera.position.x,
                camera.position.y));

        pauseButton.render(batch);


        batch.end();
        joystick.render(batch);

        //debugRender();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}