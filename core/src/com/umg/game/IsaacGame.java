package com.umg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.umg.game.ResourcesStorage.ResourceManager;
import com.umg.game.Screens.GameScreenManager;
import com.umg.game.Screens.LoadState;


public class IsaacGame extends ApplicationAdapter {
	public static final int V_WIDTH = 1280;
	public static final int V_HEIGHT = 720;
	public static float SCALE;

	public static final String TITLE = "Isaac";

	private GameScreenManager gsm;
	private SpriteBatch batch;
	ResourceManager resources;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameScreenManager();
		resources = new ResourceManager();
		SCALE = Gdx.graphics.getWidth() / V_WIDTH;

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.input.setCatchBackKey(true);

		gsm.push(new LoadState(gsm, resources));
	}

	@Override
	public void pause() {
		gsm.pause();
		super.pause();
	}


	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose () {
		super.dispose();
		gsm.dispose();
		batch.dispose();
		resources.dispose();
	}
}
