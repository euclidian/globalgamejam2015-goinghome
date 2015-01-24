package com.phinisi.goinghome.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.phinisi.goinghome.GoingHome;
import com.phinisi.goinghome.utilities.Constants;

public abstract class AbstractScreen implements Screen{

	public AbstractScreen(GoingHome game){
		this.myGame = game;

        camera = new OrthographicCamera(Constants.GRAPHIC_WIDTH,
                Constants.GRAPHIC_HEIGHT);

        camera.update();
        Vector3 v = new Vector3(Constants.GRAPHIC_WIDTH/2,Constants.GRAPHIC_HEIGHT/2,0);
        camera.position.set(v.x,v.y, 0);
        viewport = new FitViewport(Constants.GRAPHIC_WIDTH,Constants.GRAPHIC_HEIGHT, camera);
		create();
	}
	
	public abstract void create();
	
	@Override
	public void resize(int width, int height) {	
		viewport.update(width, height);		
	}
	
	protected Screen nextScreen;
	protected Screen prevScreen;
	protected OrthographicCamera camera;
	protected GoingHome myGame;
    protected Viewport viewport;
    
    public Screen getNextScreen(){		
		return nextScreen;
	}
	public Screen getBackScreen(){
		return prevScreen;
	}
}
