package com.umg.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.umg.game.Handlers.ButtonHandler;
import com.umg.game.IsaacGame;
import com.umg.game.ResourcesStorage.ResourceManager;


public class MenuState extends Screen {

    private final Vector2 PLAY_BTN_POS = new Vector2(-75, 100);
    private final Vector2 EXIT_BTN_POS = new Vector2(-75, -200);

    private Texture background;
    private ButtonHandler startGameButton;
    private ButtonHandler exitButton;

    private Music music;

    public MenuState(GameScreenManager gsm, ResourceManager resources){
        super(gsm, resources);
        camera.setToOrtho(false, IsaacGame.V_WIDTH, IsaacGame.V_HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background = resources.getTexture(ResourceManager.menuBackgroundTexture);
        music = resources.getMusic(ResourceManager.menuMusic);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        startGameButton = new ButtonHandler(resources.getTexture(ResourceManager.playButtonTexture),
                new Vector2(camera.position.x + PLAY_BTN_POS.x, camera.position.y + PLAY_BTN_POS.y),
                new Vector2(150, 70));

        exitButton = new ButtonHandler(resources.getTexture(ResourceManager.exitButtonTexture),
                new Vector2(camera.position.x + EXIT_BTN_POS.x, camera.position.y + EXIT_BTN_POS.y),
                new Vector2(150, 70));
    }

    @Override
    public void inputHandler() {

        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        if (startGameButton.getRectangle().contains(mousePosition.x, mousePosition.y) && Gdx.input.justTouched()){
            startGameButton.onClick();
        }
        else{
            startGameButton.notTouched();
        }

        if (startGameButton.isOnRelease()){
            music.stop();
            gsm.push(new PlayState(gsm, resources));
        }

        if (exitButton.getRectangle().contains(mousePosition.x, mousePosition.y) && Gdx.input.justTouched()){
            exitButton.onClick();
        }
        else{
            exitButton.notTouched();
        }

        if (exitButton.isOnRelease()){
            music.stop();
            Gdx.app.exit();
        }
    }

    @Override
    public void update(float dt) {
        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        inputHandler();
        camera.update();
    }

    @Override
    public void render(SpriteBatch batch) {

        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        startGameButton.render(batch);
        exitButton.render(batch);
        batch.end();
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
        resources.reload();

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}