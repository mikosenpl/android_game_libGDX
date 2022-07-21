package com.umg.game.ResourcesStorage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {

    public static TextureRegion[] getFramesArray1D(Texture texture, int frameCols, int frameRows){
        TextureRegion[][] tmp = getFramesArray2D(texture, texture.getWidth() / frameCols, texture.getHeight() / frameRows);
        TextureRegion[] frames = new TextureRegion[frameCols * frameRows];
        short index = 0;
        for (int i = 0; i < frameRows; i++){
            for (int j = 0; j < frameCols; j++){
                frames[index] = tmp[i][j];
                index++;
            }
        }

        return frames;
    }

    public static TextureRegion[][] getFramesArray2D(Texture texture, int width, int height){
        return TextureRegion.split(texture, width, height);
    }

}
