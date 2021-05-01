package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.screens.GameOverScreen;
import com.mygdx.game.screens.LoadingScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.screens.SplashScreen;
import com.mygdx.game.screens.WinnerScreen;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public AssetManager assets;
	public BitmapFont fontReallySmall, fontSmall, font60, font36Bold, font46Bold;

	// Screens
	public SplashScreen splashScreen;
	public LoadingScreen loadingScreen;
	public MainMenuScreen mainMenuScreen;
	public PlayScreen playScreen;
	public GameOverScreen gameOverScreen;
	public WinnerScreen winnerScreen;

	// Resolution
	public int WIDTH;
	public int HEIGHT;

	// Game play types:
	// 		1 - CLASSIC,
	// 		2 - INFINITY RUN (not available in this demo project),
	// 		3 - STRANDED ANIMALS (not available in this demo project)
	private int gamePlayType;
	private String lastScore;

	public boolean MENU_LOADED_FIRSTTIME;

	public MyGdxGame() {}

	public void toggleTopBanner(final boolean visible) {
		// Note: This version is desktop only, thus no need for banner handler
		// handler.showAds(visible);
	}

	@Override
	public void create () {
		// initialization
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		batch = new SpriteBatch();
		assets = new AssetManager();

		// fonts
		initFonts();

		// screens initialization
		splashScreen = new SplashScreen(this);
		loadingScreen = new LoadingScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		playScreen = new PlayScreen(this);
		gameOverScreen = new GameOverScreen(this);
		winnerScreen = new WinnerScreen(this);

		// hide ads on start
		// toggleTopBanner(false);
		gamePlayType = 1;
		lastScore = "...";
		MENU_LOADED_FIRSTTIME = true;

		this.setScreen(splashScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		assets.dispose();
		fontSmall.dispose();
	}

	private void initFonts() {
		int fontSize = HEIGHT / 46; // responsive font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Comfortaa-Light.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = fontSize;
		params.color = Color.valueOf("#dcdcff");
		fontSmall = generator.generateFont(params);

		int fontSize3 = HEIGHT / 36; // responsive font
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Comfortaa-Bold.ttf"));
		params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = fontSize3;
		params.color = Color.valueOf("#dcdcff");
		font36Bold = generator.generateFont(params);

		int fontSize2 = HEIGHT / 66;
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Comfortaa-Light.ttf"));
		params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = fontSize2;
		params.color = Color.valueOf("#dcdcff");
		fontReallySmall = generator.generateFont(params);

		int fontSize56 = HEIGHT / 60;
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Comfortaa-Light.ttf"));
		params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = fontSize56;
		params.color = Color.valueOf("#dcdcff");
		font60 = generator.generateFont(params);
	}

	public void setGameplayType(final int newGameplayType) {
		this.gamePlayType = newGameplayType;
	}
	public int getGameplayType() {
		return this.gamePlayType;
	}
	public void setLastScore(final String newLastScore) {
		this.lastScore = newLastScore;
	}
	public String getLastScore() {
		return this.lastScore;
	}
}
