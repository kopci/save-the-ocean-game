package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class SplashScreen implements Screen {

    private MyGdxGame game;
    private Stage stage;

    public SplashScreen(final MyGdxGame game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT, game.camera));
    }

    @Override
    public void show() {
        Gdx.app.log("STO_GAME_LOGGER","SplashScreen loaded.");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        Image logo = new Image(new Texture(Gdx.files.internal("logo.png")));
        logo.setSize(game.WIDTH * .5f, (game.WIDTH * .5f) * .453f);
        logo.setPosition( game.WIDTH / 2 - logo.getWidth() / 2, game.HEIGHT / 2 - logo.getHeight() / 2);
        logo.addAction(
                sequence(
                        alpha(0f),
                        fadeIn(1f, Interpolation.pow3In),
                        delay(1f),
                        fadeOut(1f, Interpolation.pow3Out),
                        run(new Runnable() {
                            @Override
                            public void run() {
                                game.setScreen(game.loadingScreen);
                            }
                        })));

        stage.addActor(logo);

    }

    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.094f, .106f, .15f, .16f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {}
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
    }
}
