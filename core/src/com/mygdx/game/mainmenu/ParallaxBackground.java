package com.mygdx.game.mainmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ParallaxBackground extends Actor {

    private int scroll;
    private Array<Texture> layers;
    private final int LAYER_SPEED_DIFFERENCE = 1; // change this to float and fix calculations if needed

    float x, y, width, height, scaleX, scaleY;
    int originX, originY, rotation, srcX, srcY;
    boolean flipX, flipY;

    private int speed;

    public ParallaxBackground(Array<Texture> textures, final float width, final float height){
        layers = textures;
        for(int i = 0; i <textures.size;i++){
            layers.get(i).setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        }
        scroll = 0;
        speed = -1;

        x = y = rotation = srcY = 0;
        this.width =  width;
        this.height = height;
        scaleX = scaleY = 1;
        flipX = flipY = false;
    }

    public void setSpeed(int newSpeed){
        this.speed = newSpeed;
    }

    public void setOwnParameters(float x, float y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        scroll+=speed;
        for(int i = 0;i<layers.size;i++) {
            srcY = scroll + i * this.LAYER_SPEED_DIFFERENCE * scroll;
            if (scroll <= - (layers.get(i).getHeight())) scroll = 0; // scroll reset (due to memory management; was causing lag before)
            batch.draw(layers.get(i), x, y, originX, originY, width, height,scaleX,scaleY,rotation,srcX,srcY,layers.get(i).getWidth(),layers.get(i).getHeight(),flipX,flipY);
        }

    }

}
