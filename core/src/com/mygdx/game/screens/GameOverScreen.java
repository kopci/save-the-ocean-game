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

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class GameOverScreen implements Screen {

    private MyGdxGame game;
    private Stage stage;

    public GameOverScreen(final MyGdxGame game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT, game.camera));
    }

    @Override
    public void show() {
        Gdx.app.log("STO_GAME_LOGGER","GameOverScreen loaded.");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        // game.toggleTopBanner(true);

        Image background = new Image(game.assets.get("gameOver.png", Texture.class));
        background.setSize(game.HEIGHT * .75f, game.HEIGHT);
        background.setPosition( 0 - (game.HEIGHT * .75f - game.WIDTH) * .5f , 0);
        stage.addActor(background);

        float tapWidth = game.WIDTH * .2f;
        float tapHeight = tapWidth * .688f;

        Image tap = new Image(game.assets.get("tap.png", Texture.class));
        tap.setSize(tapWidth, tapHeight);
        tap.setPosition(game.WIDTH * .5f - (tapWidth * .5f), game.WIDTH * .22f);
        tap.setOrigin(tapWidth / 2, tapHeight / 2);
        tap.addAction(forever(sequence(scaleBy(.1f, .1f, .5f, Interpolation.bounceIn),
                scaleBy(-.1f, -.1f, .5f, Interpolation.bounceOut))));
        tap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.toggleTopBanner(false); // HIDE TOP BANNER
                game.setScreen(game.playScreen);
            }
        });
        stage.addActor(tap);

        Image backToMenu = new Image(game.assets.get("close.png", Texture.class));
        backToMenu.setSize(game.WIDTH * .1f, game.WIDTH * .1f);
        backToMenu.setPosition(game.WIDTH * .85f, game.WIDTH * .05f);
        backToMenu.setOrigin(backToMenu.getWidth() * .5f, backToMenu.getHeight() * .5f);
        backToMenu.addAction(forever(sequence(scaleBy(.1f, .1f, .5f, Interpolation.bounceIn),
                scaleBy(-.1f, -.1f, .5f, Interpolation.bounceOut))));
        backToMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // game.toggleTopBanner(false);
                game.setScreen(game.mainMenuScreen);
            }
        });
        stage.addActor(backToMenu);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.094f, .106f, .15f, .16f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();

        float fontX = game.WIDTH * .2f;
        float fontY = (game.WIDTH * .25f) - game.fontSmall.getCapHeight(); // 36/2=18

        game.batch.begin();
        game.fontSmall.draw(game.batch, "Score:\n" + game.getLastScore(), game.WIDTH * .6f, game.HEIGHT * .5f, game.WIDTH * 0.4f, 1, true);
        game.fontSmall.draw(game.batch, "to try again.", fontX, fontY, game.WIDTH * 0.6f, 1, true);
        game.batch.end();
    }

    private void update(float delta) {
        stage.act(delta);
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
    }
}
