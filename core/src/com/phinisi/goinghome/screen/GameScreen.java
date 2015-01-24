package com.phinisi.goinghome.screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.phinisi.goinghome.GoingHome;
import com.phinisi.goinghome.entities.Character;
import com.phinisi.goinghome.utilities.Constants;

public class GameScreen extends AbstractScreen implements InputProcessor {

	public String tmxFilename;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;
	
	//create box2d world	
	World box2dWorld;
	//create box2d debugrenderer
	
	//character
	Character gameCharacter;
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	public final float PIXELS_TO_METERS = 100f;
	
	private static final float WOLRD_TO_BOX = 0.01f;
	private static final float BOX_TO_WORLD = 100f;

	public GameScreen(GoingHome game) {
		super(game);
	}

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);
		camera = new OrthographicCamera(Constants.GRAPHIC_WIDTH,
				Constants.GRAPHIC_HEIGHT);
		camera.update();
		camera.position.set(Constants.GRAPHIC_WIDTH / 2,
				Constants.GRAPHIC_HEIGHT / 2, 0);
		viewport = new FitViewport(Constants.GRAPHIC_WIDTH,
				Constants.GRAPHIC_HEIGHT);

		tiledMap = new TmxMapLoader().load("level1.tmx");
		tiledMapRenderer = new OrthoCachedTiledMapRenderer(tiledMap);

		box2dWorld = new World(new Vector2(0, -98f), false); 
		
		loadObjectLayer();
		
		//create character
		gameCharacter = new Character(box2dWorld);		
	}

	private void loadObjectLayer() {
		//load layer with name = bounds
		MapLayer boundingLayer = tiledMap.getLayers().get("bounds");
		MapObjects objects = boundingLayer.getObjects();
		//iterate each object
		for(int i=0;i<objects.getCount();i++){
			RectangleMapObject polyObject = (RectangleMapObject) objects.get(i);
			Rectangle r = polyObject.getRectangle();			
			Gdx.app.log("GoingHome", String.format("X: %f,  Y: %f, W: %f, H : %f",r.x, r.y,r.width, r.height));
			//create ploygon shape body
			BodyDef platformBodyDef = new BodyDef();
			platformBodyDef.position.set(new Vector2(r.x + r.width/2,r.y + r.height/2));
			//create a body from 
			Body platformBody = box2dWorld.createBody(platformBodyDef);
			//create a polygon shape
			PolygonShape platformBox = new PolygonShape();
			platformBox.setAsBox(r.width/2, r.height/2);
			platformBody.createFixture(platformBox,0);
			platformBox.dispose();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.update();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
		camera.update();
		gameCharacter.update();
		box2dWorld.step(Gdx.graphics.getDeltaTime(), 6, 2);
		
		myGame.getSpriteBatch().begin();
		gameCharacter.charSprite.draw(myGame.getSpriteBatch());
		myGame.getSpriteBatch().end();
		
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();		
		debugRenderer.render(box2dWorld, camera.combined);			
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
	public boolean keyDown(int keycode) {
		if(keycode == Keys.SPACE){
			gameCharacter.jump();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
