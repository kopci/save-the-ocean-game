package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;

public class LoadingScreen implements Screen {

    private MyGdxGame game;
    private ShapeRenderer shapeRenderer;
    private float progress;
    private Stage stage;

    public LoadingScreen(final MyGdxGame game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        this.stage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT, game.camera));
    }

    public void queueAssets() {
        // LOGO
        game.assets.load("logo.png", Texture.class);

        // MENU
        game.assets.load("menu/bag.png", Texture.class);
        game.assets.load("menu/cup.png", Texture.class);
        game.assets.load("menu/play.png", Texture.class);
        game.assets.load("menu/turtleAndBag.png", Texture.class);

        // submarine
        game.assets.load("SubmarineBase.png", Texture.class);
        game.assets.load("SubmarineHead.png", Texture.class);
        game.assets.load("WhirlpoolAbove.png", Texture.class);
        game.assets.load("WhirlpoolUnder.png", Texture.class);

        // SOUNDS
        // Music was removed from project

        // screens
        game.assets.load("gameOver.png", Texture.class);
        game.assets.load("tap.png", Texture.class);
        game.assets.load("win.png", Texture.class);

        // TRASH
        for (int i = 1; i <= 4; i++) {
            game.assets.load("trash/" + i + ".png", Texture.class);
        }
        // PARALLAX
        for(int i = 1; i <= 2; i++) {
            game.assets.load("parallax/" + i + ".png", Texture.class);
        }
        // UNDERWATER SURFACE
        for (int i = 1; i <= 5; i++) {
            game.assets.load("under/under" + i + ".png", Texture.class);
        }

        game.assets.load("subParallax.png", Texture.class);
        game.assets.load("catcherAnim.png", Texture.class);
        game.assets.load("close.png", Texture.class);
        game.assets.load("XOff.png", Texture.class);
        game.assets.load("XOn.png", Texture.class);
        game.assets.load("background.png", Texture.class);
        game.assets.load("gameplayShade.png", Texture.class);

    }

    @Override
    public void show() {
        Gdx.app.log("STO_GAME_LOGGER","LoadingScreen loaded.");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        Image intro = new Image(new Texture(Gdx.files.internal("loadingSTO.png")));
        intro.setSize(game.HEIGHT * .75f, game.HEIGHT);
        intro.setPosition( 0 - (game.HEIGHT * .75f - game.WIDTH) * .5f , 0);
        stage.addActor(intro);

        shapeRenderer.setProjectionMatrix(game.camera.combined);
        this.progress = 0f;
        queueAssets();
    }

    private void update(float delta) {
        progress = MathUtils.lerp(progress, game.assets.getProgress(), .1f);
        if(game.assets.update() && progress >= game.assets.getProgress() - .001f) {
            game.setScreen(game.mainMenuScreen);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.094f, .106f, .15f, .16f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);

        stage.draw();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(.16f, .19f, .24f, 1f);
        shapeRenderer.rect(0, 0, game.WIDTH, 10);

        shapeRenderer.setColor(1f,1f,1f,1f);
        shapeRenderer.rect(0,0, progress * game.WIDTH, 10);
        shapeRenderer.end();

        game.batch.begin();
        game.fontReallySmall.draw(game.batch, "Loading...", game.WIDTH * 0.1f, game.HEIGHT * .05f, game.WIDTH * 0.8f, 1, true);
        game.batch.end();

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
        shapeRenderer.dispose();
        stage.dispose();
        game.dispose();
    }
}
