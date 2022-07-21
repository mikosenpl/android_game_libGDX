package com.umg.game.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.umg.game.IsaacGame;


public class TouchPad{

    private OrthographicCamera camera;
    private Touchpad touchpad;
    private Touchpad touchpadShoot;

    public TouchPad(OrthographicCamera camera) {
        //Create camera
        this.camera = camera;

        Viewport viewport = new FitViewport(IsaacGame.V_WIDTH, IsaacGame.V_HEIGHT, camera);

        //Create a touchpad skin
        Skin touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("ui/joystick/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("ui/joystick/touchKnob.png"));
        //Create TouchPad Style
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        touchpadShoot = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        //touchpad.setBounds(camera.position.x, camera.position.y, 200, 200);

        //Create a Stage and add TouchPad
        Stage stage = new Stage(viewport, new SpriteBatch());
        stage.addActor(touchpad);
        stage.addActor(touchpadShoot);
        Gdx.input.setInputProcessor(stage);

    }

    public Vector2 getKnobPercentWalk(){
        return new Vector2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
    }

    public Vector2 getKnobPercentShoot(){
        return new Vector2(touchpadShoot.getKnobPercentX(), touchpadShoot.getKnobPercentY());
    }

    public void render(SpriteBatch batch) {

        touchpad.setPosition(camera.position.x - camera.viewportWidth / 2 + 30,
                camera.position.y - camera.viewportHeight / 2 + 30);

        touchpadShoot.setPosition(camera.position.x + camera.viewportWidth / 2 - 230,
                camera.position.y - camera.viewportHeight / 2 + 30);

        batch.begin();
        touchpad.draw(batch, 1);
        touchpadShoot.draw(batch, 1);
        batch.end();
    }

}
