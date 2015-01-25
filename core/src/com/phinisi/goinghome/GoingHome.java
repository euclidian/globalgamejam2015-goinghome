package com.phinisi.goinghome;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.phinisi.goinghome.screen.GameScreen;
import com.phinisi.goinghome.screen.SplashScreen;

public class GoingHome extends Game {
	SpriteBatch batch;	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
//		GameScreen gameScreen = new GameScreen(this);
//		setScreen(gameScreen);
		SplashScreen splashScreen = new SplashScreen(this);
		setScreen(splashScreen);
		Gdx.app.setLogLevel(Gdx.app.LOG_NONE);
	}
	
	public SpriteBatch getSpriteBatch(){
		return batch;
	}
}
