package com.mygdx.game.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class CatcherAnimation extends Image {

    Animation<TextureRegion> animation;
    float stateTime;

    public CatcherAnimation(final Texture texture, final float width, final float height, final float x, final float y) {
        TextureRegion[][] tmp = TextureRegion.split(texture,texture.getWidth() / 8, texture.getHeight() / 1);
        TextureRegion[] walkFrames = new TextureRegion[8];
        int index = 0;
        for (int j = 0; j < 8; j++) {
            walkFrames[index++] = tmp[0][j];
        }
        this.animation = new Animation<TextureRegion>(0.066f, walkFrames); // 15 fps
        this.stateTime = 0f;

        this.setSize(width, height);
        this.setPosition(x, y);
    }

    public void update(final float delta) {
        stateTime += delta;
        if (stateTime >= 1) stateTime = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame,this.getX(),this.getY(),this.getWidth(),this.getHeight());
    }
}