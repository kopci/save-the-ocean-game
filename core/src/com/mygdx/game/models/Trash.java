package com.mygdx.game.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Iterator;

public class Trash extends Actor {

    private Texture texture;
    private Rectangle bounds;
    private int startingPoint; // 1, 2, 3 (from left to right)
    private int typeOfTrash; // 1 - electro, 2 - plastic, 3 - glass, 4 - paper
    private boolean caught;

    public Trash(final Texture texture, final float x, final float y, final float width, final float height, final int startingPoint, final int typeOfTrash) {
        this.texture = texture;
        this.startingPoint = startingPoint;
        this.typeOfTrash = typeOfTrash;
        this.caught = false;

        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setOrigin(width / 2, height / 2);

        this.bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
        this.setBounds(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        bounds.setPosition(getX(), getY());
        batch.draw(texture,this.getX(),this.getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
                this.getHeight() * 1.2f,this.getScaleX() * 1.2f, this.getScaleY(),this.getRotation(),0,0,
                texture.getWidth(),texture.getHeight(),false,false);
    }

    @Override
    public void act(float delta) {
        for(Iterator<Action> iter = this.getActions().iterator(); iter.hasNext();){
            iter.next().act(delta);
        }
    }

    public void gotCaught() {
        this.caught = true;
    }

    public boolean isCaught() {
        return this.caught;
    }

    /**
     * Collision detecting method.
     * @param someTrashCatcher
     * @return true if trash bounds overlaps trashCatcher bounds
     */
    public boolean collides(Rectangle someTrashCatcher) {
        return bounds.overlaps(someTrashCatcher);
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    public int getTypeOfTrash() {
        return this.typeOfTrash;
    }

}
