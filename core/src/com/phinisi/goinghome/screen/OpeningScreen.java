package com.phinisi.goinghome.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.phinisi.goinghome.GoingHome;
import com.phinisi.goinghome.animation.ScaleRotateAnimation;
import com.phinisi.goinghome.animation.TextureAnimation;
import com.phinisi.goinghome.entities.Character;
import com.phinisi.goinghome.utilities.Constants;

public class OpeningScreen extends AbstractScreen {

	enum OpeningState {
		Start, JonesMasuk, Tulisan1Masuk, Tulisan2Masuk, Selesai
	}

	OpeningState state;
	SpriteBatch spriteBatch;
	ScaleRotateAnimation scaleRotationAnimation;
	
	TextureAnimation animation;
	
	//start state variable
	float currStart = 0, maxStart = 3;
	
	int currIdx = 0;
	String tulisan1 = "Thank You Jones !";
	String tulisan2 = "Your Quest Is Now Over";
	
	//jones masuk variable
	Texture jonesBg;
	
	Texture bg;
	
	BitmapFont font;

	public OpeningScreen(GoingHome game) {
		super(game);
		state = OpeningState.Start;
		spriteBatch = new SpriteBatch();
		bg = new Texture("opening.png");
		
		Texture t = new Texture("light.png");
		scaleRotationAnimation = new ScaleRotateAnimation(0.5f, true,t);
		scaleRotationAnimation.setPosition(Constants.GRAPHIC_WIDTH/2 - t.getWidth()/2 , 40);
		
		
		Texture texture1 = new Texture("karakter/kar-1.png");
		Texture texture2 = new Texture("karakter/kar-2.png");
		
		jonesBg = new Texture("karakter/kar-1.png");
		
		animation = new TextureAnimation(0.5f, true, texture1,texture2);
		animation.setPosition(50, 94);
		
		font = new BitmapFont(Gdx.files.internal("font/sert.fnt"));
		font.setColor(Color.WHITE);
//		font = new BitmapFont();
//		font.setScale(3);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        update(delta);
        
        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);	
        spriteBatch.begin();
                
		switch (state) {
		case Start:
			spriteBatch.setColor(1, 1, 1, currStart/maxStart);			        			
			spriteBatch.draw(bg, 0, 0);			
			break;
		case JonesMasuk:
			spriteBatch.draw(bg, 0, 0);
//			scaleRotationAnimation.draw(spriteBatch);
			//draw animasi
			animation.draw(spriteBatch);
			break;
		case Tulisan1Masuk:
			spriteBatch.draw(bg, 0, 0);
			spriteBatch.draw(jonesBg, Constants.GRAPHIC_WIDTH /2 - 100, 94);
//			scaleRotationAnimation.draw(spriteBatch);
			currIdx++;
			if(currIdx >= tulisan1.length()) currIdx = tulisan1.length();
			String currString = tulisan1.substring(0, currIdx);
			font.draw(spriteBatch, currString, 375, Constants.GRAPHIC_HEIGHT-165);
			if(currIdx >= tulisan1.length()){
				currIdx = 0;
				state = OpeningState.Tulisan2Masuk;
			}
			break;
		case Tulisan2Masuk:
			spriteBatch.draw(bg, 0, 0);
			spriteBatch.draw(jonesBg, Constants.GRAPHIC_WIDTH /2 - 100, 94);
//			scaleRotationAnimation.draw(spriteBatch);
			currIdx++;
			if(currIdx >= tulisan2.length()) currIdx = tulisan2.length();
			String currString2 = tulisan2.substring(0, currIdx);
			font.draw(spriteBatch,tulisan1, 375, Constants.GRAPHIC_HEIGHT - 165);
			font.draw(spriteBatch, currString2, 337, Constants.GRAPHIC_HEIGHT-195);
			if(currIdx == tulisan2.length()){				
				state = OpeningState.Selesai;	
				currStart = 0;
			}
			break;
		case Selesai:
			currStart += delta;
			spriteBatch.setColor(1, 1, 1, 1 - currStart/4);
			spriteBatch.draw(bg, 0, 0);
			spriteBatch.draw(jonesBg, Constants.GRAPHIC_WIDTH /2 - 100, 94);
			font.draw(spriteBatch,tulisan1, 375, Constants.GRAPHIC_HEIGHT - 165);
			font.draw(spriteBatch, tulisan2, 337, Constants.GRAPHIC_HEIGHT-195);
			if(currStart > maxStart){
				myGame.setScreen(new CreditScreen(myGame));
			}
			break;
		default:
			break;
		}		
		spriteBatch.end();

	}

	private void update(float delta) {
		scaleRotationAnimation.update(delta);		
		switch (state) {
		case Start:
			currStart += delta;
			if(currStart > maxStart){
				state = OpeningState.JonesMasuk;
			}
			break;
		case JonesMasuk:
			animation.update(delta);
			animation.setPosition(animation.getPosX()+1, animation.getPosY());
			if(animation.getPosX() > Constants.GRAPHIC_WIDTH /2 - 100){
				this.state = OpeningState.Tulisan1Masuk;
			}
			break;
		case Tulisan1Masuk:
			
			break;
		default:
			break;
		}
		
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

	@Override
	public void create() {
		// TODO Auto-generated method stub

	}

}
