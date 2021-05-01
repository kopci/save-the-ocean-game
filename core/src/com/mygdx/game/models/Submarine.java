package com.mygdx.game.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Submarine extends Actor {

    // TEXTURES
    private Texture head;
    private Texture whirl1;
    private Texture whirl2;

    // WHIRLPOOLS - ROTATION
    private float W1_rotationAmount;
    private float W1_deltaTracker;
    private float W2_rotationAmount;
    private float W2_deltaTracker;
    private float HEAD_rotationAmount;
    private float HEAD_deltaTracker;

    private boolean rotateBackwards = false;
    private float secondsPerRotation;
    private float rotationBoost;

    // WHIRLPOOLS - FADING
    private float whirlpoolAlpha = 1f;
    private boolean whirlpoolFading = false;

    public Submarine(final float x, final float y, final float width, final float height, final Texture head, final Texture whirl1,
                     final Texture whirl2, final float durationOfOneRotation) {
        this.head = head;
        this.whirl1 = whirl1;
        this.whirl2 = whirl2;

        // positioning
        setPosition(x, y);
        setSize(width, height);
        setOrigin(width / 2, height / 2);

        // initialization
        this.secondsPerRotation = durationOfOneRotation;
        this.W1_deltaTracker = 0f;
        this.W2_deltaTracker = 0f;
        this.W1_rotationAmount = 0f;
        this.W2_rotationAmount = 0f;
        this.HEAD_rotationAmount = 0;
        this.HEAD_deltaTracker = 0f;
        this.rotationBoost = 1f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // whirlpools
        batch.setColor(1f, 1f, 1f, whirlpoolAlpha);
        batch.draw(whirl1,this.getX(),this.getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),this.getHeight(),this.getScaleX(),
                this.getScaleY(),W1_rotationAmount,0,0, whirl1.getWidth(),whirl1.getHeight(),false,false);
        batch.draw(whirl2,this.getX(),this.getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),this.getHeight(),this.getScaleX(),
                this.getScaleY(),W2_rotationAmount,0,0, whirl2.getWidth(),whirl2.getHeight(),false,false);
        // head
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(head,this.getX(),this.getY(),this.getOriginX(),this.getOriginY(),this.getWidth(), this.getHeight(),this.getScaleX(),
                this.getScaleY(),HEAD_rotationAmount,0,0, head.getWidth(),head.getHeight(),false,false);
    }

    /**
     * Method updates:
     * - ROTATION of all three components (whirlpools and head), updates and resets delta-trackers and rotation-amount
     * - WHIRLPOOLS ALPHA, whenever fading is in progress
     *
     * @param delta
     */
    public void update(float delta) {
        // DELTA TRACKERS UPDATE (decrement if rotates backwards, otherwise increment)
        if (this.rotateBackwards) {
            this.W1_deltaTracker -= delta;
            this.W2_deltaTracker -= delta;
            this.HEAD_deltaTracker -= delta;
        } else {
            this.W1_deltaTracker += delta;
            this.W2_deltaTracker += delta;
            this.HEAD_deltaTracker += delta;
        }

        // ROTATION amount calculation
        this.W1_rotationAmount = (((this.W1_deltaTracker * 360) / this.secondsPerRotation) * .4f) * this.rotationBoost;
        this.W2_rotationAmount = (((this.W2_deltaTracker * 360) / this.secondsPerRotation) * .8f) * this.rotationBoost;
        this.HEAD_rotationAmount = ((this.HEAD_deltaTracker * 360) / this.secondsPerRotation) * this.rotationBoost;

        // MEMORY MANAGEMENT (prevents from growing to infinity), DELTA TRACKERS reset
        if(this.W1_rotationAmount >= 4320) { // 360 * 12 = 4320 // TODO: you are not checking -4320 in case rotationAmount is negative
            this.W1_rotationAmount = 0;
            this.W1_deltaTracker = 0;
        }
        if(this.W2_rotationAmount >= 4320) {
            this.W2_rotationAmount = 0;
            this.W2_deltaTracker = 0;
        }
        if(this.HEAD_rotationAmount >= 4320) {
            this.HEAD_rotationAmount = 0;
            this.HEAD_deltaTracker = 0;
        }

        // WHIRLPOOLS FADING
        if(whirlpoolFading) {
            whirlpoolAlpha -= .1f;
            if (whirlpoolAlpha <= 0) {
                whirlpoolFading = false;
            }
        } else if (whirlpoolAlpha < 1) {
            whirlpoolAlpha += delta;
        }

    }

    /**
     * Method manages speed of rotation.
     *
     * @param newValue of duration of one rotation (in seconds)
     */
    public void setRotationBoost(final float newValue) {
        this.rotationBoost = newValue;
    }

    public void rotateBackward() {
        this.whirlpoolFading = true;
        this.rotateBackwards = true;
    }

    public void rotateFrontward() {
        this.whirlpoolFading = true;
        this.rotateBackwards = false;
    }

    public float getHeadRotation() {
        return this.HEAD_rotationAmount;
    }


}
