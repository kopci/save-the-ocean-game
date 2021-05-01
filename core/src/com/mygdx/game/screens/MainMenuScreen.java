package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class MainMenuScreen implements Screen {

    private MyGdxGame game;
    private Stage stage;

    private Image play;
    private Image background;
    private Image turtleAndBag;
    private Image bag;
    private Image cup;

    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT, game.camera));
    }

    @Override
    public void show() {
        Gdx.app.log("STO_GAME_LOGGER","MainMenuScreen loaded.");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        initBackground();
        initAudio();
        initPlayButton();
        initOtherActors();

        if (game.MENU_LOADED_FIRSTTIME) game.MENU_LOADED_FIRSTTIME = false;
    }

    public void update(float delta) {
        float xAxis = Gdx.input.getAccelerometerX();
        float yAxis = Gdx.input.getAccelerometerY();

        if (turtleAndBag.getX() + xAxis * .5f > game.WIDTH * .14f || turtleAndBag.getX() + xAxis * .5f < .6f) { /* do nothing */ }
        else turtleAndBag.setX(turtleAndBag.getX() + xAxis * .5f);

        if (bag.getX() + xAxis * 1.5f > game.WIDTH * .85f || bag.getX() + xAxis * 1.5f < game.WIDTH * .45f) { /* do nothing */ }
        else bag.setX(bag.getX() + xAxis * 1.5f);

        if (cup.getX() + xAxis * .75f > 0 || cup.getX() + xAxis * .75f < - game.WIDTH * .2f) { /* do nothing */ }
        else cup.setX(cup.getX() + xAxis * .75f);

        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        stage.dispose();
        game.dispose();
        // audio.dispose();
    }

    private void initAudio() {
        // audio = game.assets.get("...");
        // audio.setVolume(.25f);
        // audio.setLooping(true);
        // audio.play();
    }

    private void initBackground() {
        background = new Image(game.assets.get("background.png", Texture.class));
        background.setSize(game.HEIGHT * .75f, game.HEIGHT);
        background.setPosition( 0 - (game.HEIGHT * .75f - game.WIDTH) * .5f , 0);
        stage.addActor(background);
    }

    private void initOtherActors() {
        turtleAndBag = new Image(game.assets.get("menu/turtleAndBag.png", Texture.class));
        turtleAndBag.setSize(game.WIDTH * .8f, game.WIDTH * .8f);
        turtleAndBag.setPosition(game.WIDTH * .1f, game.HEIGHT - (game.WIDTH * .8f));
        stage.addActor(turtleAndBag);

        cup = new Image(game.assets.get("menu/cup.png", Texture.class));
        cup.setSize(game.WIDTH * .4f * .59f, game.WIDTH * .4f);
        cup.setPosition(- game.WIDTH * .1f, game.HEIGHT * .2f);
        stage.addActor(cup);

        bag = new Image(game.assets.get("menu/bag.png", Texture.class));
        bag.setSize(game.WIDTH * .5f, game.WIDTH * .7f);
        bag.setPosition(game.WIDTH * .65f, - game.WIDTH * .3f);
        stage.addActor(bag);
    }

    private void initPlayButton() {
        float PLAYBUTTON_SIZE = game.WIDTH * .35f;
        float PLAYBUTTON_Y_FINAL = game.HEIGHT * .25f;
        float PLAYBUTTON_Y_INITIAL = PLAYBUTTON_Y_FINAL - PLAYBUTTON_Y_FINAL * .25f; // moving from position

        play = new Image(game.assets.get("menu/play.png", Texture.class));
        play.setSize(PLAYBUTTON_SIZE, PLAYBUTTON_SIZE);
        play.setPosition( game.WIDTH * .325f , PLAYBUTTON_Y_INITIAL);
        play.setOrigin(PLAYBUTTON_SIZE * .5f, PLAYBUTTON_SIZE * .5f);

        if (game.MENU_LOADED_FIRSTTIME) {
            // ACTIONS
            play.addAction(sequence(
                    // default setting
                    alpha(0f), delay(.5f),
                    // fading in
                    parallel(moveTo(game.WIDTH * .325f , PLAYBUTTON_Y_FINAL, 1f, Interpolation.smooth),alpha(1f, 1f, Interpolation.pow3In)),
                    // continuous pulsing
                    forever(sequence(
                            parallel(scaleBy(.2f, .2f, 2f, Interpolation.smooth)),
                            parallel(scaleBy(-.2f, -.2f, 2f, Interpolation.smooth))
                    ))
            ));
        } else {
            play.setPosition(game.WIDTH * .325f , PLAYBUTTON_Y_FINAL);
            play.addAction(
                    // continuous pulsing
                    forever(sequence(
                            parallel(scaleBy(.2f, .2f, 2f, Interpolation.smooth)),
                            parallel(scaleBy(-.2f, -.2f, 2f, Interpolation.smooth))
                    )));
        }

        // LISTENER
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // audio.stop();
                game.setScreen(game.playScreen);
            }
        });

        stage.addActor(play);
    }

}
