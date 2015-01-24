package com.phinisi.goinghome;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.phinisi.goinghome.screen.GameScreen;

public class GoingHome extends Game {
	SpriteBatch batch;	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		GameScreen gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}
	
	public SpriteBatch getSpriteBatch(){
		return batch;
	}
}
