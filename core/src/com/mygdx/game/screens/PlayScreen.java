package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.helpers.GameplayHelper;
import com.mygdx.game.helpers.VectorsHelper;
import com.mygdx.game.mainmenu.ParallaxBackground;
import com.mygdx.game.models.CatcherAnimation;
import com.mygdx.game.models.Trash;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class PlayScreen implements Screen {

    private MyGdxGame game;
    private Stage stage;

    // DIMENSIONS
    private float subHead_size;
    private float subBase_width;
    private float subBase_height;
    private float circle_radius;
    private float trash_width;
    private float trashCatcher_width;
    private float catcherAnim_width;

    // STAGE ACTORS
    private Image submarineHead;
    private Image submarineWhirl1;
    private Image submarineWhirl2;
    private Image subBase;
    private List<Trash> trashArray;
    private CatcherAnimation anim1;
    private CatcherAnimation anim2;
    private CatcherAnimation anim3;
    private CatcherAnimation anim4;
    private Image life1;
    private Image life2;
    private Image life3;
    private Image x1;
    private Image x2;
    private Image x3;
    private Image shadow;
    private ParallaxBackground subParallax;
    private ParallaxBackground parallaxBackground;

    // FOR TESTING PURPOSES
    ShapeRenderer shapeRenderer;

    // TRASH CATCHERS
    private Rectangle redElectroCatcher, yellowPlasticCatcher, greenGlassCatcher, bluePaperCatcher;

    // GAME PLAY
    private float trashGeneratingPERIOD, trashGeneratingDELAY, trashGeneratedALPHA, durationOfOneRotation;
    private int lives, lastStartingPoint, currentlyActiveTrashTypes, trashLeft, trashGenerated;

    List<Trash> toRemove;

    private boolean gamePaused, isTouched;
    private int gameplayType;

    /**
     * CONSTRUCTOR
     */
    public PlayScreen(final MyGdxGame game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT, game.camera));
        this.shapeRenderer = new ShapeRenderer();
        this.trashArray = new ArrayList<Trash>();

        // init dimensions
        this.subHead_size = game.WIDTH * 0.75f;
        this.subBase_width = subHead_size * 0.6455f;
        this.subBase_height = subHead_size * 0.843f;
        this.circle_radius = this.subHead_size * 0.1085f;
        this.trash_width = this.circle_radius * 2.2f;
        this.trashCatcher_width = this.trash_width * .5f;
        this.catcherAnim_width = this.circle_radius * 3f;

        // trash catchers
        this.redElectroCatcher = new Rectangle(0, 0, this.trashCatcher_width, this.trashCatcher_width);
        this.yellowPlasticCatcher = new Rectangle(0, 0, this.trashCatcher_width, this.trashCatcher_width);
        this.greenGlassCatcher = new Rectangle(0, 0, this.trashCatcher_width, this.trashCatcher_width);
        this.bluePaperCatcher = new Rectangle(0, 0, this.trashCatcher_width, this.trashCatcher_width);
    }

    @Override
    public void show() {
        Gdx.app.log("STO_GAME_LOGGER","PlayScreen loaded.");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        gameplayType = game.getGameplayType();
        gamePaused = false;
        isTouched = false;

        // reset lives and toRemove, init lastStartingPoint and lastTypeOfTrash
        // IMPORTANT: sets gameProgress from Preferences
        initializeGameplayVariables();
        initOceanRelief();
        initParallaxBackground();
        initLives();
        initSubmarine();
        initAudio();
        initCatcherAnimations();
        initShadow();
    }

    private void update(float delta) {
        // GAME OVER CHECK
        if (lives <= 0) {
            // audio.stop();
            game.setLastScore(trashLeft + " left to catch"); // last score for gameOverScreen
            if (GameplayHelper.getClassicScore() > trashLeft) GameplayHelper.setClassicScore(trashLeft); // save new best score
            game.setScreen(game.gameOverScreen);
        } else if (trashLeft <= 0) { // CLASSIC; check if win
            // audio.stop();
            GameplayHelper.setClassicScore(trashLeft); // save best score
            game.setScreen(game.winnerScreen);
        }

        // STAGE ACT, UPDATING CUSTOM ACTORS
        if (!gamePaused) {
            stage.act(delta);
            anim1.update(delta);
            anim2.update(delta);
            anim3.update(delta);
            anim4.update(delta);
        }

        // GENERATING TRASH
        if(trashGeneratingDELAY >= trashGeneratingPERIOD && trashGenerated < 100) {
            generateTrash();
            trashGeneratingDELAY = 0;
        } else {
            trashGeneratingDELAY += delta;
        }

        if (trashGeneratedALPHA == 3) {
            // getting harder
            trashGeneratedALPHA = 0f;
            if (trashGeneratingPERIOD > 1f) trashGeneratingPERIOD -= .1f;
            if (durationOfOneRotation > 1.7f) durationOfOneRotation -= .1f;
            submarineHead.clear();
            if (isTouched) submarineHead.addAction(forever(rotateBy(-360f, durationOfOneRotation)));
            else submarineHead.addAction(forever(rotateBy(360f, durationOfOneRotation)));
        }

        /**
         * COLLISION DETECTION
         */
        for(final Trash t : trashArray) {
            if (t.isCaught()) { continue; }

            // CORRECT collecting of trash
            if ((t.collides(redElectroCatcher) && t.getTypeOfTrash() == 1) || (t.collides(yellowPlasticCatcher) && t.getTypeOfTrash() == 2) ||
                    (t.collides(greenGlassCatcher) && t.getTypeOfTrash() == 3) || (t.collides(bluePaperCatcher) && t.getTypeOfTrash() == 4)) {
                t.gotCaught();
                trashLeft--;
                t.addAction(sequence(parallel(
                        rotateBy(120f, .05f),
                        scaleTo(0, 0, .05f),
                        alpha(0f, .05f)
                ), run(new Runnable() {
                    @Override
                    public void run() {
                        toRemove.add(t);
                        t.remove();
                    }
                })));

                // INCORRECT collecting
            } else if ((t.collides(redElectroCatcher) && t.getTypeOfTrash() != 1) || (t.collides(yellowPlasticCatcher) && t.getTypeOfTrash() != 2) ||
                    (t.collides(greenGlassCatcher) && t.getTypeOfTrash() != 3) || (t.collides(bluePaperCatcher) && t.getTypeOfTrash() != 4)) {
                t.gotCaught();
                lives--;
                trashLeft--;
                lightOnX();
                t.addAction(sequence(
                        rotateBy(120f, .05f),
                        scaleTo(0, 0, .05f),
                        alpha(0f, .05f), run(new Runnable() {
                    @Override
                    public void run() {
                        toRemove.add(t);
                        t.remove();
                    }
                })));
            }
        }
        if (!toRemove.isEmpty()) {
            for (Trash tr : toRemove) {
                trashArray.remove(tr);
            }
            toRemove.clear();
        }

        // update catcher objects for collision detection
        updateTrashCatchersV2();
    }

    @Override
    public void render(float delta) {
        game.batch.setProjectionMatrix(game.camera.combined);
        Gdx.gl.glClearColor(.06f, .06f, .2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();

        game.batch.begin();
        game.font36Bold.draw(game.batch, "" + trashLeft, game.WIDTH * .05f, game.HEIGHT - (game.WIDTH * .05f), game.WIDTH * 0.9f, 1, true);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {
        parallaxBackground.setSpeed(0);
        subParallax.setSpeed(0);
        gamePaused = true;
        // audio.pause();
    }
    @Override
    public void resume() {
        parallaxBackground.setSpeed(-1);
        subParallax.setSpeed(-1);
        gamePaused = false;
        // audio.play();
    }
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        stage.dispose();
        game.dispose();
        // audio.dispose();
    }


    /**
     * Follows:
     * Game play functionality
     * - initialization of game play vars
     * - trash generator
     * - trash catchers position update and check
     * - remaining lives indications
     */


    /** Initialization of game play variables */
    private void initializeGameplayVariables() {
        toRemove = new ArrayList<Trash>();
        lives = 3;
        lastStartingPoint = 2;
        trashGeneratingDELAY = 0; // starting delay (0s)
        trashGeneratingPERIOD = 3f;
        trashGeneratedALPHA = 0; // starts from 0
        currentlyActiveTrashTypes = 4;
        durationOfOneRotation = 3f;
        trashLeft = 10;
        trashGenerated = 0;
    }

    /** Trash generator */
    private void generateTrash() {
        Random random = new Random();

        int[] otherStartingPoints = new int[2];
        int index = 0;
        for(int i = 1; i <= 3; i++) {
            if (i != lastStartingPoint) {
                otherStartingPoints[index] = i;
                index++;
            }
        }

        lastStartingPoint = GameplayHelper.getRandomElement(otherStartingPoints);

        // CHOOSE 1 from 10 RANDOM TRASH PIECES
        int typeOfTrash = random.nextInt(currentlyActiveTrashTypes) + 1;
        Texture trashTexture = game.assets.get("trash/" + typeOfTrash + ".png", Texture.class);

        // SETS X,Y STARTING POSITION, default setting for LEFT point
        float trashX = (game.WIDTH - subHead_size) / 2;
        float trashY = game.HEIGHT + 1; // 1px above top edge
        switch(lastStartingPoint) {
            case 1 : break; // default LEFT
            case 2 : trashX = (game.WIDTH / 2) - (trash_width / 2); break; // CENTER
            case 3 : trashX = game.WIDTH - ((game.WIDTH - subHead_size) / 2) - trash_width; break; // RIGHT
        }

        // Generate trash
        final Trash trash = new Trash(trashTexture, trashX, trashY, trash_width, trash_width, lastStartingPoint, typeOfTrash);
        trash.addAction(sequence(parallel(moveBy(0, - game.HEIGHT - trash_width, 4f), rotateBy(60f, 4f)), run(new Runnable() {
            @Override
            public void run() {
                trashArray.remove(trash);
                trash.remove();
                lives--;
                lightOnX();
                trashLeft--;
            }
        })));
        stage.addActor(trash);
        trashArray.add(trash);
        trashGeneratedALPHA++;
        trashGenerated++;

        // Memory management test (deleting trash from stage and array) - TESTED, OK
        // ...

        // shadow on top
        shadow.setZIndex(trash.getZIndex() + 1);
    }


    /** Updates trashCatchers rectangles positions */
    private void updateTrashCatchersV2() {
        float tmp = (game.WIDTH - subHead_size) / 2; // helper (position of submarine head)
        float headRotation = submarineHead.getRotation();

        Vector2 redCatcher = VectorsHelper.getCirclePositionVector(headRotation + 90, (subBase_width / 2) + (circle_radius / 2),
                new Vector2(tmp + (subHead_size / 2), tmp + (subHead_size / 2)));
        Vector2 yellowCatcher = VectorsHelper.getCirclePositionVector(headRotation, (subBase_width / 2) + (circle_radius / 2),
                new Vector2(tmp + (subHead_size / 2), tmp + (subHead_size / 2)));
        Vector2 greenCatcher = VectorsHelper.getCirclePositionVector(headRotation + 270, (subBase_width / 2) + (circle_radius / 2),
                new Vector2(tmp + (subHead_size / 2), tmp + (subHead_size / 2)));
        Vector2 blueCatcher = VectorsHelper.getCirclePositionVector(headRotation + 180, (subBase_width / 2) + (circle_radius / 2),
                new Vector2(tmp + (subHead_size / 2), tmp + (subHead_size / 2)));

        redElectroCatcher.setPosition(redCatcher.x - trashCatcher_width / 2,redCatcher.y - trashCatcher_width / 2);
        yellowPlasticCatcher.setPosition(yellowCatcher.x - trashCatcher_width / 2,yellowCatcher.y - trashCatcher_width / 2);
        greenGlassCatcher.setPosition(greenCatcher.x - trashCatcher_width / 2,greenCatcher.y - trashCatcher_width / 2);
        bluePaperCatcher.setPosition(blueCatcher.x - trashCatcher_width / 2,blueCatcher.y - trashCatcher_width / 2);

        float tmp2 = catcherAnim_width * .5f;
        anim1.setPosition(redCatcher.x - tmp2, redCatcher.y - tmp2);
        anim2.setPosition(yellowCatcher.x - tmp2, yellowCatcher.y - tmp2);
        anim3.setPosition(greenCatcher.x - tmp2, greenCatcher.y - tmp2);
        anim4.setPosition(blueCatcher.x - tmp2, blueCatcher.y - tmp2);
    }


    /** Lives indicator controller; switches on, when player loses one life */
    private void lightOnX() {
        if (lives == 2) {
            life3.addAction(alpha(0f, .3f));
            x3.addAction(alpha(1f, .3f, Interpolation.bounceIn));
        } else if (lives == 1) {
            life2.addAction(alpha(0f, .3f));
            x2.addAction(alpha(1f, .3f, Interpolation.bounceIn));
        } else if (lives == 0) {
            life1.addAction(alpha(0f, .3f));
            x1.addAction(alpha(1f, .3f, Interpolation.bounceIn));
        }
    }


    /**
     * Follows:
     * - initialization of AUDIO
     * - initialization of SUBMARINE, LIVES INDICATORS, BACKGROUNDS, CATCHERS ANIMATIONS, PARALLAX
     *
     *
     */


    /** SOUND */
    private void initAudio() {
        // audio = game.assets.get("...");
        // audio.setVolume(10f);
        // audio.setLooping(true);
        // audio.play();
    }

    /** PARALLAX BACKGROUND */
    private void initParallaxBackground() {
        Array<Texture> pbArray = new Array<Texture>();
        for(int i = 1; i <= 2; i++) pbArray.add(game.assets.get("parallax/" + i + ".png", Texture.class));
        parallaxBackground = new ParallaxBackground(pbArray, game.WIDTH, game.HEIGHT * 1.5f);
        stage.addActor(parallaxBackground);
    }

    /** LIFE INDICATORS */
    private void initLives() {
        Texture texture = game.assets.get("XOff.png", Texture.class);
        Texture texture2 = game.assets.get("XOn.png", Texture.class);
        float size = game.WIDTH * .05f;

        life1 = new Image(texture);
        life2 = new Image(texture);
        life3 = new Image(texture);
        life1.setSize(size, size);
        life2.setSize(size, size);
        life3.setSize(size, size);
        life1.setPosition(-size, size);
        life2.setPosition(-size, size * 2f);
        life3.setPosition(-size, size * 3f);

        x1 = new Image(texture2);
        x2 = new Image(texture2);
        x3 = new Image(texture2);
        x1.setColor(1f, 1f, 1f, 0f);
        x2.setColor(1f, 1f, 1f, 0f);
        x3.setColor(1f, 1f, 1f, 0f);
        x1.setSize(size, size);
        x2.setSize(size, size);
        x3.setSize(size, size);
        x1.setPosition(size, size);
        x2.setPosition(size, size * 2f);
        x3.setPosition(size, size * 3f);

        x1.addAction(forever(sequence(
                delay(3f),
                scaleBy(.2f, .2f, .2f),
                scaleBy(-.2f, -.2f, .2f))));
        x2.addAction(sequence(delay(.15f), forever(sequence(
                delay(3f),
                scaleBy(.2f, .2f, .2f),
                scaleBy(-.2f, -.2f, .2f)
        ))));
        x3.addAction(sequence(delay(.25f), forever(sequence(
                delay(3f),
                scaleBy(.2f, .2f, .2f),
                scaleBy(-.2f, -.2f, .2f)
        ))));

        life1.addAction(sequence(delay(1f), moveTo(size, size, 1f, Interpolation.bounce), forever(sequence(
                delay(3f),
                scaleBy(.2f, .2f, .2f),
                scaleBy(-.2f, -.2f, .2f)))));
        life2.addAction(sequence(delay(1.15f), moveTo(size, size * 2f, 1f, Interpolation.bounce), forever(sequence(
                delay(3f),
                scaleBy(.2f, .2f, .2f),
                scaleBy(-.2f, -.2f, .2f)
        ))));
        life3.addAction(sequence(delay(1.25f), moveTo(size, size * 3f, 1f, Interpolation.bounce), forever(sequence(
                delay(3f),
                scaleBy(.2f, .2f, .2f),
                scaleBy(-.2f, -.2f, .2f)
        ))));

        stage.addActor(life1);
        stage.addActor(life2);
        stage.addActor(life3);

        stage.addActor(x1);
        stage.addActor(x2);
        stage.addActor(x3);
    }

    /** SUBMARINE */
    private void initSubmarine() {
        Array<Texture> pa = new Array<Texture>();
        pa.add(game.assets.get("subParallax.png", Texture.class));
        pa.add(game.assets.get("subParallax.png", Texture.class));
        pa.add(game.assets.get("subParallax.png", Texture.class));
        subParallax = new ParallaxBackground(pa, game.WIDTH * .4f, (game.WIDTH * .4f) * 1.16f);
        subParallax.setOwnParameters(game.WIDTH * .3f, 0, -2);
        stage.addActor(subParallax);

        subBase = new Image(game.assets.get("SubmarineBase.png", Texture.class));
        subBase.setSize(subBase_width, subBase_height);
        subBase.setPosition((game.WIDTH - subBase_width) / 2, ((game.WIDTH - subHead_size) / 2));
        subBase.setOrigin(subBase_width * .5f, subBase_height * .5f);
        subBase.addAction(forever(sequence(
                rotateBy(5f, 2f, Interpolation.smooth),
                delay(1f),
                rotateBy(-10f, 4f, Interpolation.smooth),
                delay(1f),
                rotateBy(5f, 2f, Interpolation.smooth)
        )));
        stage.addActor(subBase);

        submarineWhirl1 = new Image(game.assets.get("WhirlpoolUnder.png", Texture.class));
        submarineWhirl1.setSize(subHead_size, subHead_size);
        submarineWhirl1.setPosition((game.WIDTH - subHead_size) / 2, (game.WIDTH - subHead_size) / 2);
        submarineWhirl1.setOrigin(subHead_size * .5f, subHead_size * .5f);
        submarineWhirl1.addAction(forever(rotateBy(360, durationOfOneRotation * 2f)));
        stage.addActor(submarineWhirl1);

        submarineWhirl2 = new Image(game.assets.get("WhirlpoolAbove.png", Texture.class));
        submarineWhirl2.setSize(subHead_size, subHead_size);
        submarineWhirl2.setPosition((game.WIDTH - subHead_size) / 2, (game.WIDTH - subHead_size) / 2);
        submarineWhirl2.setOrigin(subHead_size * .5f, subHead_size * .5f);
        submarineWhirl2.addAction(forever(rotateBy(360, durationOfOneRotation * 3f)));
        stage.addActor(submarineWhirl2);

        submarineHead = new Image(game.assets.get("SubmarineHead.png", Texture.class));
        submarineHead.setSize(subHead_size, subHead_size);
        submarineHead.setPosition((game.WIDTH - subHead_size) / 2, (game.WIDTH - subHead_size) / 2);
        submarineHead.setOrigin(subHead_size * .5f, subHead_size * .5f);
        submarineHead.addAction(forever(rotateBy(360, durationOfOneRotation)));
        stage.addActor(submarineHead);
        stage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayScreen.this.isTouched = true;
                submarineHead.getActions().clear();
                submarineWhirl1.getActions().clear();
                submarineWhirl2.getActions().clear();
                submarineHead.addAction(forever(rotateBy(-360f, durationOfOneRotation)));
                submarineWhirl1.addAction(sequence(alpha(0f), parallel(alpha(1f, durationOfOneRotation * .4f), forever(rotateBy(-360f, durationOfOneRotation * 2f)))));
                submarineWhirl2.addAction(sequence(alpha(0f), parallel(alpha(1f, durationOfOneRotation * .4f), forever(rotateBy(-360f, durationOfOneRotation * 3f)))));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        stage.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PlayScreen.this.isTouched = false;
                submarineHead.getActions().clear();
                submarineWhirl1.getActions().clear();
                submarineWhirl2.getActions().clear();
                submarineHead.addAction(forever(rotateBy(360f, durationOfOneRotation)));
                submarineWhirl1.addAction(sequence(alpha(0f), parallel(alpha(1f, durationOfOneRotation * .4f), forever(rotateBy(360f, durationOfOneRotation * 2f)))));
                submarineWhirl2.addAction(sequence(alpha(0f), parallel(alpha(1f, durationOfOneRotation * .4f), forever(rotateBy(360f, durationOfOneRotation * 3f)))));
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    /** CATCHERS WHIRLPOOLS */
    private void initCatcherAnimations() {
        anim1 = new CatcherAnimation(game.assets.get("catcherAnim.png", Texture.class), this.catcherAnim_width, this.catcherAnim_width, 0, 0);
        anim1.addAction(sequence(alpha(0), alpha(1f, .5f)));
        stage.addActor(anim1);
        anim2 = new CatcherAnimation(game.assets.get("catcherAnim.png", Texture.class), this.catcherAnim_width, this.catcherAnim_width, 0, 0);
        anim2.addAction(sequence(alpha(0), alpha(1f, .5f)));
        stage.addActor(anim2);
        anim3 = new CatcherAnimation(game.assets.get("catcherAnim.png", Texture.class), this.catcherAnim_width, this.catcherAnim_width, 0, 0);
        anim3.addAction(sequence(alpha(0), alpha(1f, .5f)));
        stage.addActor(anim3);
        anim4 = new CatcherAnimation(game.assets.get("catcherAnim.png", Texture.class), this.catcherAnim_width, this.catcherAnim_width, 0, 0);
        anim4.addAction(sequence(alpha(0), alpha(1f, .5f)));
        stage.addActor(anim4);
    }

    /** OCEAN BOTTOM RELIEF (background) */
    private void initOceanRelief() {
        float underWidth = game.WIDTH * 1.2f;
        float underHeight = game.WIDTH * 1.2f * 1.33f;
        float underX = - game.WIDTH * .1f;

        Image under1 = new Image(game.assets.get("under/under1.png", Texture.class));
        under1.setSize( underWidth, underHeight);
        under1.setPosition(underX, underHeight * 1.5f);
        under1.addAction(forever(sequence(moveBy(underX, -underHeight * 3f, 30f), moveTo(underX, underHeight * 1.5f))));
        stage.addActor(under1);

        Image under2 = new Image(game.assets.get("under/under2.png", Texture.class));
        under2.setSize( underWidth, underHeight);
        under2.setPosition(underX, underHeight * 1.52f);
        under2.addAction(forever(sequence(moveBy(underX, -underHeight * 3.04f, 30f), moveTo(underX, underHeight * 1.52f))));
        stage.addActor(under2);

        Image under3 = new Image(game.assets.get("under/under3.png", Texture.class));
        under3.setSize( underWidth, underHeight);
        under3.setPosition(underX, underHeight * 1.53f);
        under3.addAction(forever(sequence(moveBy(underX, -underHeight * 3.06f, 30f), moveTo(underX, underHeight * 1.53f))));
        stage.addActor(under3);

        Image under4 = new Image(game.assets.get("under/under4.png", Texture.class));
        under4.setSize( underWidth, underHeight);
        under4.setPosition(underX, underHeight * 1.54f);
        under4.addAction(forever(sequence(moveBy(underX, -underHeight * 3.08f, 30f), moveTo(underX, underHeight * 1.54f))));
        stage.addActor(under4);

        Image under5 = new Image(game.assets.get("under/under5.png", Texture.class));
        under5.setSize( underWidth, underHeight);
        under5.setPosition(underX, underHeight * 1.55f);
        under5.addAction(forever(sequence(moveBy(underX, -underHeight * 3.1f, 30f), moveTo(underX, underHeight * 1.55f))));
        stage.addActor(under5);

        Image under10 = new Image(game.assets.get("under/under1.png", Texture.class));
        under10.setSize( underWidth, underHeight);
        under10.setPosition(underX, underHeight * 1.5f);
        under10.setOrigin(underWidth * .5f, underHeight * .5f);
        under10.setRotation(180f);
        under10.addAction(sequence(delay(15f), forever(sequence(moveBy(underX, -underHeight * 3f, 30f), moveTo(underX, underHeight * 1.5f)))));
        stage.addActor(under10);

        Image under20 = new Image(game.assets.get("under/under2.png", Texture.class));
        under20.setSize( underWidth, underHeight);
        under20.setPosition(underX, underHeight * 1.52f);
        under20.setOrigin(underWidth * .5f, underHeight * .5f);
        under20.setRotation(180f);
        under20.addAction(sequence(delay(15f), forever(sequence(moveBy(underX, -underHeight * 3.04f, 30f), moveTo(underX, underHeight * 1.52f)))));
        stage.addActor(under20);

        Image under30 = new Image(game.assets.get("under/under3.png", Texture.class));
        under30.setSize( underWidth, underHeight);
        under30.setPosition(underX, underHeight * 1.53f);
        under30.setOrigin(underWidth * .5f, underHeight * .5f);
        under30.setRotation(180f);
        under30.addAction(sequence(delay(15f), forever(sequence(moveBy(underX, -underHeight * 3.06f, 30f), moveTo(underX, underHeight * 1.53f)))));
        stage.addActor(under30);

        Image under40 = new Image(game.assets.get("under/under4.png", Texture.class));
        under40.setSize( underWidth, underHeight);
        under40.setPosition(underX, underHeight * 1.54f);
        under40.setOrigin(underWidth * .5f, underHeight * .5f);
        under40.setRotation(180f);
        under40.addAction(sequence(delay(15f), forever(sequence(moveBy(underX, -underHeight * 3.08f, 30f), moveTo(underX, underHeight * 1.54f)))));
        stage.addActor(under40);

        Image under50 = new Image(game.assets.get("under/under5.png", Texture.class));
        under50.setSize( underWidth, underHeight);
        under50.setPosition(underX, underHeight * 1.55f);
        under50.setOrigin(underWidth * .5f, underHeight * .5f);
        under50.setRotation(180f);
        under50.addAction(sequence(delay(15f), forever(sequence(moveBy(underX, -underHeight * 3.1f, 30f), moveTo(underX, underHeight * 1.55f)))));
        stage.addActor(under50);

    }

    /** SHADOW AT THE TOP OF THE SCREEN */
    private void initShadow() {
        shadow = new Image(game.assets.get("gameplayShade.png", Texture.class));
        shadow.setSize(game.HEIGHT * .75f, game.HEIGHT);
        shadow.setPosition( 0 - (game.HEIGHT * .75f - game.WIDTH) * .5f , 0);
        shadow.setColor(1f, 1f, 1f, .5f);
        stage.addActor(shadow);
    }


}
