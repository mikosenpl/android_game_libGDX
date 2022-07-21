package com.umg.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.umg.game.IsaacGame;
import com.umg.game.ResourcesStorage.ResourceManager;

public class LoadState extends Screen {

    private Texture loading;

    public LoadState(GameScreenManager gsm, ResourceManager resources){
        super(gsm, resources);
        camera.setToOrtho(false, IsaacGame.V_WIDTH, IsaacGame.V_HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        loading = new Texture(Gdx.files.internal("images/loading1.png"));

        resources.load();
    }

    @Override
    public void inputHandler() {

    }

    @Override
    public void update(float dt) {
        camera.update();
        if (resources.isLoaded()){
            gsm.set(new MenuState(gsm, resources));
        }
    }

    @Override
    public void render(SpriteBatch batch) {

        Gdx.gl20.glClearColor(1, 1, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        System.out.println("I AM DRAWING");
        batch.draw(loading, 0, 0, 500, 200);
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
