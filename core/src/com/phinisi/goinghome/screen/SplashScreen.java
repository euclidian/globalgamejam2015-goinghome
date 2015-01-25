package com.phinisi.goinghome.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.phinisi.goinghome.GoingHome;

public class SplashScreen extends AbstractScreen{

	float maxTime = 3;
	float currTime = 0;
	Texture bg;
	SpriteBatch spriteBatch;
	
	public SplashScreen(GoingHome game) {
		super(game);		
	}
	
	@Override
	public void create() {
		bg = new Texture("splash.png");		
//		camera.position.set(-Constants.GRAPHIC_WIDTH/2, -Constants.GRAPHIC_HEIGHT/2, 0);
		spriteBatch = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		currTime += delta;
		if(currTime > maxTime){
			myGame.setScreen(new OpeningScreen(myGame));
		}
		Gdx.gl.glClearColor(0, 0, 0, currTime / maxTime);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();               
        
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        spriteBatch.setColor(1, 1, 1, 1 - currTime/maxTime);
		spriteBatch.begin();
		spriteBatch.draw(bg, 0, 0);
		spriteBatch.end();
		
	}
	

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
