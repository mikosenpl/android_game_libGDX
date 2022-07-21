package com.umg.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.umg.game.Handlers.ButtonHandler;
import com.umg.game.IsaacGame;
import com.umg.game.ResourcesStorage.ResourceManager;

public class EndGameState extends Screen {

    private static final Vector2 MENU_BTN_POS = new Vector2(-75, -300);
    private static final Vector2 TEXT_SHIFT = new Vector2(-100, 300);
    private static final Vector2 PLAYER_TEXTURE_SHIFT = new Vector2(-60, -160);

    private Texture background;
    private Texture playerTexture;

    private BitmapFont font;
    private boolean isWin;

    private ButtonHandler menuButton;

    public EndGameState(GameScreenManager gsm, ResourceManager resources, boolean isWin){
        super(gsm, resources);
        camera.setToOrtho(false, IsaacGame.V_WIDTH, IsaacGame.V_HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background = resources.getTexture(ResourceManager.finalBG);
        font = resources.getFont();
        this.isWin = isWin;

        playerTexture = isWin ? resources.getTexture(ResourceManager.goodFinal) : resources.getTexture(ResourceManager.badFinal);

        menuButton = new ButtonHandler(resources.getTexture(ResourceManager.menuButtonTexture),
                new Vector2(camera.position.x + MENU_BTN_POS.x, camera.position.y + MENU_BTN_POS.y),
                new Vector2(150, 70));
    }

    @Override
    public void inputHandler() {
        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

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
        inputHandler();
        camera.update();
    }

    @Override
    public void render(SpriteBatch batch) {

        String text = isWin ? "YOU WON" : "YOU LOSE";
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.draw(playerTexture, camera.position.x + PLAYER_TEXTURE_SHIFT.x,
                camera.position.y + PLAYER_TEXTURE_SHIFT.y, 128, 128);
        font.draw(batch, text, camera.position.x + TEXT_SHIFT.x, camera.position.y + TEXT_SHIFT.y);
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
