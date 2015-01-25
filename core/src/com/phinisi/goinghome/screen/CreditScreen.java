package com.phinisi.goinghome.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.phinisi.goinghome.GoingHome;
import com.phinisi.goinghome.utilities.Constants;

public class CreditScreen extends AbstractScreen {

	enum CreditState {
		Start, End
	}

	Texture bg;
	Texture hitam;
	float stateTime = 0f, maxTime = 3f;
	float posX, posY;
	SpriteBatch spriteBatch;
	CreditState state = CreditState.Start;

	public CreditScreen(GoingHome game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void create() {
		bg = new Texture("credit.png");
		posY = -bg.getHeight();
		posX = (Constants.GRAPHIC_WIDTH - bg.getWidth()) / 2;

		spriteBatch = new SpriteBatch();		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		update(delta);

		if (state == CreditState.End) {
			spriteBatch.setColor(1, 1, 1, 1 - stateTime / maxTime);
		}

		spriteBatch.setProjectionMatrix(camera.projection);
		spriteBatch.setTransformMatrix(camera.view);
		spriteBatch.begin();
		spriteBatch.draw(bg, posX, posY);

		spriteBatch.end();

	}

	public void update(float delta) {
		switch (state) {
		case Start:
			if (posY < 0) {
				posY += 3;
			} else {
				state = CreditState.End;
			}
			break;

		case End:
			stateTime += delta;
			Gdx.app.log("A", "State: " + stateTime);
			if (stateTime > maxTime) {
				myGame.setScreen(new GameScreen(myGame));
			}
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

}
