package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class WinnerScreen implements Screen {

    private Stage stage;
    private MyGdxGame game;
    private Image background;

    public WinnerScreen(final MyGdxGame game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT, game.camera));
    }

    @Override
    public void show() {
        Gdx.app.log("STO_GAME_LOGGER","WinnerScreen loaded.");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        String backgroundName = "win.png";

        background = new Image(game.assets.get(backgroundName, Texture.class));
        background.setSize(game.HEIGHT * .75f, game.HEIGHT);
        background.setPosition( 0 - (game.HEIGHT * .75f - game.WIDTH) * .5f , 0);
        background.addAction(sequence(alpha(0f), alpha(1f, .3f)));
        background.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.mainMenuScreen);
            }
        });
        stage.addActor(background);
    }

    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        game.batch.setProjectionMatrix(game.camera.combined);
        Gdx.gl.glClearColor(.075f, .184f, .33f, .16f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();

        game.batch.begin();
        game.font60.setColor(1f, 1f, 1f, 1f);
        game.font60.draw(game.batch,
                "Dear player, i'd like to thank you for finishing this mini-game. I wish you a great day.\n\n\nTap to go back.",
                game.WIDTH * 0.1f, game.HEIGHT * .25f, game.WIDTH * .8f, 1, true);
        game.batch.end();
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
        game.dispose();
        stage.dispose();
    }
}
