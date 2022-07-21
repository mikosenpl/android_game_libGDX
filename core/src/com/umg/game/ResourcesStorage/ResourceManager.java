package com.umg.game.ResourcesStorage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.umg.game.Entities.Isaac;

public class ResourceManager {

    private static final int playerBodyFrameWidth = (int) Isaac.BODY_SIZE.x;

    private static final String imgPath = "images/";
    private static final String soundPath = "sounds/";
    private static final String musicPath = "music/";
    private static final String fontsPath = "fonts/";
    private static final String levelsPath = "levels/";

    public static final String isaacTexture = imgPath + "body1.png";
    public static final String isaacHeadTexture = imgPath + "playerHead.png";
    public static final String backgoundTexture = imgPath + "background.png";
    public static final String menuBackgroundTexture = imgPath + "menu_background.png";
    public static final String uiHeartTexture = imgPath + "ui_hearts.png";
    public static final String loadingTexture = imgPath + "loading1.png";
    public static final String flyTexture = imgPath + "fly.png";
    public static final String bulletTexture = imgPath + "bullet.png";
    public static final String bulletDestroyTexture = imgPath + "tear_destroy.png";
    public static final String enemyDestroyTexture = imgPath + "enemyDestroyTexture.png";
    public static final String wormTexture = imgPath + "worm.png";
    public static final String halfBlackTexture = imgPath + "black.png";
    public static final String followHead = imgPath + "followHead.png";

    public static final String pauseBtnTexture = imgPath + "pauseBtn1.png";
    public static final String resumeButtonTexture = imgPath + "resumeBtn.png";
    public static final String menuButtonTexture = imgPath + "menuBtn.png";
    public static final String playButtonTexture = imgPath + "playBtn.png";
    public static final String exitButtonTexture = imgPath + "exitBtn.png";

    public static final String finalBG = imgPath + "finalBg.png";
    public static final String goodFinal = imgPath + "finalGood.png";
    public static final String badFinal = imgPath + "finalBad.png";

    public static final String hpBonusTexture = imgPath + "bonuses/hpBonus.png";
    public static final String asBonusTexture = imgPath + "bonuses/asBonus.png";
    public static final String msBonusTexture = imgPath + "bonuses/msBonus.png";
    public static final String dmgBonusTexture = imgPath + "bonuses/dmgBonus.png";

    public static final String menuMusic = musicPath + "menuMusic.ogg";
    public static final String bgMusic = musicPath + "bgMusic.ogg";

    public static final String playerShoot = soundPath + "playerShoot.wav";
    public static final String shootDestroy = soundPath + "shootDestroy.wav";
    public static final String enemyDies = soundPath + "enemyDies.wav";
    public static final String bonusDrop = soundPath + "bonusDrop.wav";
    public static final String pickUp = soundPath + "pickUp.wav";
    public static final String levelStart = soundPath + "levelStart.wav";
    public static final String playerHurts = soundPath + "playerHurts.wav";

    public static final String mapLevel1 = levelsPath + "level1.tmx";
    public static final String mapLevel2 = levelsPath + "level2.tmx";

    public Texture texture;

    private BitmapFont font;

    private AssetManager manager;

    public void load(){

        manager = new AssetManager();
        loadMaps();
        loadTextures();
        loadFonts();
        loadMusic();
        loadSounds();

        while(!manager.update())
        {
            //System.out.println("Loaded: " + (int)(manager.getProgress() * 100) + "%");
        }
    }

    private void loadTextures(){

        manager.setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        manager.load(isaacTexture, Texture.class);
        manager.load(backgoundTexture, Texture.class);
        manager.load(menuBackgroundTexture, Texture.class);
        manager.load(uiHeartTexture, Texture.class);
        manager.load(loadingTexture, Texture.class);
        manager.load(flyTexture, Texture.class);
        manager.load(bulletTexture, Texture.class);
        manager.load(bulletDestroyTexture, Texture.class);
        manager.load(isaacHeadTexture, Texture.class);
        manager.load(enemyDestroyTexture, Texture.class);
        manager.load(wormTexture, Texture.class);
        manager.load(pauseBtnTexture, Texture.class);
        manager.load(resumeButtonTexture, Texture.class);
        manager.load(halfBlackTexture, Texture.class);
        manager.load(menuButtonTexture, Texture.class);
        manager.load(playButtonTexture, Texture.class);
        manager.load(exitButtonTexture, Texture.class);
        manager.load(followHead, Texture.class);

        manager.load(hpBonusTexture, Texture.class);
        manager.load(asBonusTexture, Texture.class);
        manager.load(msBonusTexture, Texture.class);
        manager.load(dmgBonusTexture, Texture.class);

        manager.load(finalBG, Texture.class);
        manager.load(goodFinal, Texture.class);
        manager.load(badFinal, Texture.class);
    }

    private void loadMaps(){

        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load(mapLevel1, TiledMap.class);
        manager.load(mapLevel2, TiledMap.class);
    }

    private void loadFonts(){

        font = new BitmapFont();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontsPath + "main_font.ttf"));
        parameter.size = 25;
        parameter.color = new Color(1, 1, 1, 1);
        font = generator.generateFont(parameter);
    }

    private void loadMusic(){
        manager.setLoader(Music.class, new MusicLoader(new InternalFileHandleResolver()));
        manager.load(menuMusic, Music.class);
        manager.load(bgMusic, Music.class);
    }

    private void loadSounds(){
        manager.setLoader(Sound.class, new SoundLoader(new InternalFileHandleResolver()));
        manager.load(playerShoot, Sound.class);
        manager.load(shootDestroy, Sound.class);
        manager.load(enemyDies, Sound.class);
        manager.load(bonusDrop, Sound.class);
        manager.load(pickUp, Sound.class);
        manager.load(levelStart, Sound.class);
        manager.load(playerHurts, Sound.class);
    }

    public BitmapFont getFont(){
        return font;
    }

    public boolean isLoaded(){
        return (manager.getProgress() >= 1);
    }

    public void reload(){
        this.load();
    }

    public Texture getTexture(String name){
        return manager.get(name);
    }

    public TiledMap getLevel(String name){
        return manager.get(name);
    }

    public Music getMusic(String name){
        return manager.get(name);
    }

    public Sound getSound(String name){
        return manager.get(name);
    }

    public void dispose(){

        manager.clear();
        manager.dispose();
        font.dispose();
    }
}
