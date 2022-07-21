package com.umg.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.umg.game.Handlers.ButtonHandler;
import com.umg.game.IsaacGame;
import com.umg.game.ResourcesStorage.ResourceManager;

public class PauseState extends Screen{

    private static final Vector2 RESUME_BTN_POS = new Vector2(-90, -90);
    private static final Vector2 MENU_BTN_POS = new Vector2(-75, -100);

    private Texture background;

    private ButtonHandler resumeButton;
    private ButtonHandler menuButton;

    public PauseState(GameScreenManager gsm, ResourceManager resources){
        super(gsm, resources);
        camera.setToOrtho(false, IsaacGame.V_WIDTH, IsaacGame.V_HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background = resources.getTexture(ResourceManager.halfBlackTexture);
        resumeButton = new ButtonHandler(resources.getTexture(ResourceManager.resumeButtonTexture),
                new Vector2(camera.position.x + camera.viewportWidth / 2 + RESUME_BTN_POS.x,
                        camera.position.y + camera.viewportHeight / 2 + RESUME_BTN_POS.y),
                new Vector2(55, 55));

        menuButton = new ButtonHandler(resources.getTexture(ResourceManager.menuButtonTexture),
                new Vector2(camera.position.x + MENU_BTN_POS.x, camera.position.y + MENU_BTN_POS.y),
                new Vector2(150, 70));
    }

    @Override
    public void inputHandler() {

        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        if (resumeButton.getRectangle().contains(mousePosition.x, mousePosition.y) && Gdx.input.justTouched()){
            resumeButton.onClick();
        }
        else{
            resumeButton.notTouched();
        }

        if (resumeButton.isOnRelease()){
            gsm.pop();
        }

        if (menuButton.getRectangle().contains(mousePosition.x, mousePosition.y) && Gdx.input.justTouched()){
            menuButton.onClick();
        }
        else{
            menuButton.notTouched();
        }

        if (menuButton.isOnRelease()){
            gsm.set(new MenuState(gsm, resources));
        }
    }

    @Override
    public void update(float dt) {
        camera.update();
        inputHandler();
    }

    @Override
    public void render(SpriteBatch batch) {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        resumeButton.render(batch);
        menuButton.render(batch);
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
