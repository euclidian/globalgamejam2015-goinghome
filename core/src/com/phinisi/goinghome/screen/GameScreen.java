package com.phinisi.goinghome.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.phinisi.goinghome.GoingHome;
import com.phinisi.goinghome.entities.Character;
import com.phinisi.goinghome.monsters.BaseMonster;
import com.phinisi.goinghome.monsters.Monster1;
import com.phinisi.goinghome.utilities.BodyFactory;
import com.phinisi.goinghome.utilities.Constants;

public class GameScreen extends AbstractScreen implements InputProcessor,
		ContactListener {

	public String tmxFilename;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;

	// create box2d world
	World box2dWorld;
	// create box2d debugrenderer

	// character
	Character gameCharacter;
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	Texture leftButton, rightButton, jumpButton;

	float leftButtonPosX, leftButtonPosY;
	float rightButtonPosX, rightButtonPosY;
	float jumpButtonPosX, jumpButtonPosY;
	private boolean isLeftTouched;
	private boolean isRightTouched;
	private boolean isUpTouched;

	List<BaseMonster> monsters;
	int MaxMonsters = 10;

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

		box2dWorld = new World(new Vector2(0, -10f), false);
		box2dWorld.setContactListener(this);

		loadObjectLayer();

		// create character
		gameCharacter = new Character(box2dWorld);

		initControlButton();

		initMonsters();

	}

	private void initMonsters() {
		monsters = new ArrayList<BaseMonster>();
		for (int i = 0; i < 10; i++) {
			Monster1 m = new Monster1(box2dWorld);
			m.setWakeTime(i + 1);
//			monsters.add(m);
		}
	}

	private void initControlButton() {
		leftButton = new Texture("ui/left.png");
		rightButton = new Texture("ui/right.png");
		jumpButton = new Texture("ui/jump.png");

		leftButtonPosX = 20;
		leftButtonPosY = 0;

		rightButtonPosX = 120;
		rightButtonPosY = 0;

		jumpButtonPosX = Constants.GRAPHIC_WIDTH - 2 * jumpButton.getWidth();
		jumpButtonPosY = 0;

	}

	private void loadObjectLayer() {
		// load layer with name = bounds
		MapLayer boundingLayer = tiledMap.getLayers().get("bounds");
		MapObjects objects = boundingLayer.getObjects();
		// iterate each object
		for (int i = 0; i < objects.getCount(); i++) {
			RectangleMapObject polyObject = (RectangleMapObject) objects.get(i);
			String wallType = (String) objects.get(i).getProperties()
					.get("type");
			wallType = wallType == null ? "2" : wallType;

			Rectangle r = polyObject.getRectangle();
			Gdx.app.log("GoingHome", String.format(
					"X: %f,  Y: %f, W: %f, H : %f, WallType : %s", r.x, r.y,
					r.width, r.height, wallType));
			// create ploygon shape body
			BodyDef platformBodyDef = new BodyDef();
			platformBodyDef.position.set(new Vector2((r.x + r.width / 2)
					/ BodyFactory.PIXELS_TO_METERS, (r.y + r.height / 2)
					/ BodyFactory.PIXELS_TO_METERS));
			// create a body from
			Body platformBody = box2dWorld.createBody(platformBodyDef);
			// create a polygon shape
			PolygonShape platformBox = new PolygonShape();
			platformBox.setAsBox(r.width / 2 / BodyFactory.PIXELS_TO_METERS,
					r.height / 2 / BodyFactory.PIXELS_TO_METERS);
			// Create fixture
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = platformBox;
			fixtureDef.friction = 0.5f;
			// check wall type
			if (wallType.equalsIgnoreCase("1")) {
				// it is a turn around wall
				fixtureDef.filter.categoryBits = Constants.WallCategory;
			} else {
				// it is a platform
				fixtureDef.filter.categoryBits = Constants.PlatformCategory;
			}
			fixtureDef.filter.maskBits = (short) 0xFFFF;
			platformBody.createFixture(fixtureDef);
			platformBody.setUserData(wallType);
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
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update(delta);

		myGame.getSpriteBatch().setProjectionMatrix(camera.combined);
		// myGame.getSpriteBatch().setTransformMatrix(camera.view);

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		myGame.getSpriteBatch().begin();
		gameCharacter.draw(myGame.getSpriteBatch());
		for (BaseMonster m : monsters) {
			m.charSprite.draw(myGame.getSpriteBatch());
		}
		drawControlUI();
		myGame.getSpriteBatch().end();

		Matrix4 debugMatrix = myGame
				.getSpriteBatch()
				.getProjectionMatrix()
				.cpy()
				.scale(BodyFactory.PIXELS_TO_METERS,
						BodyFactory.PIXELS_TO_METERS, 0);
		debugRenderer.render(box2dWorld, debugMatrix);
	}

	private void update(float deltaTime) {
		if (isLeftTouched)
			gameCharacter.moveLeft();
		if (isRightTouched)
			gameCharacter.moveRight();

		box2dWorld.step(1 / 60f, 6, 2);
		camera.update();
		gameCharacter.update(deltaTime);

		// update monsters
		List<BaseMonster> deletedMonsters = new ArrayList<BaseMonster>();
		for (BaseMonster m : monsters) {
			m.update(deltaTime);
			if (m.isDie()) {
				deletedMonsters.add(m);
			}
		}
		for (BaseMonster del : deletedMonsters) {
			box2dWorld.destroyBody(del.body);
			monsters.remove(del);
		}
	}

	private void drawControlUI() {
		myGame.getSpriteBatch()
				.draw(leftButton, leftButtonPosX, leftButtonPosY);
		myGame.getSpriteBatch().draw(rightButton, rightButtonPosX,
				rightButtonPosY);
		myGame.getSpriteBatch()
				.draw(jumpButton, jumpButtonPosX, jumpButtonPosY);
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
		switch (keycode) {
		case Keys.SPACE:
			gameCharacter.jump();
			break;
		case Keys.RIGHT:
			gameCharacter.moveRight();
			break;
		case Keys.LEFT:
			gameCharacter.moveLeft();
			break;
		case Keys.ESCAPE:
			myGame.setScreen(new GameScreen(myGame));
			break;

		default:
			break;
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
		Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));
		Gdx.app.log("GoingHome", String.format("Pos: %f,%f", pos.x, pos.y));
		Gdx.app.log("GoingHome", String.format("Pos: %d,%d", screenX, screenY));
		if (isButtonPressed(pos.x, pos.y, leftButtonPosX, leftButtonPosY,
				leftButton.getWidth(), leftButton.getHeight())) {
			isLeftTouched = true;
		} else if (isButtonPressed(pos.x, pos.y, rightButtonPosX,
				rightButtonPosY, rightButton.getWidth(),
				rightButton.getHeight())) {
			isRightTouched = true;
		} else if (isButtonPressed(pos.x, pos.y, jumpButtonPosX,
				jumpButtonPosY, jumpButton.getWidth(), jumpButton.getHeight())) {
			gameCharacter.jump();
		}
		return false;
	}

	private boolean isButtonPressed(float x, float y, float buttonPosX,
			float buttonPosY, float width, float height) {
		Rectangle r = new Rectangle(buttonPosX, buttonPosY, width, height);
		return r.contains(x, y);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));
		if (isButtonPressed(pos.x, pos.y, leftButtonPosX, leftButtonPosY,
				leftButton.getWidth(), leftButton.getHeight())) {
			isLeftTouched = false;
			gameCharacter.stopMoving();
		} else if (isButtonPressed(pos.x, pos.y, rightButtonPosX,
				rightButtonPosY, rightButton.getWidth(),
				rightButton.getHeight())) {
			isRightTouched = false;
			gameCharacter.stopMoving();
		} else if (isButtonPressed(pos.x, pos.y, jumpButtonPosX,
				jumpButtonPosY, jumpButton.getWidth(), jumpButton.getHeight())) {
			isUpTouched = false;
		}
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

	@Override
	public void beginContact(Contact contact) {
		Object fixtureA = contact.getFixtureA().getBody().getUserData();
		Object fixtureB = contact.getFixtureB().getBody().getUserData();

		if (fixtureA == null && fixtureB == null)
			return;

		checkWallMonsterCollision(fixtureA, fixtureB);
		checkMonsterCharacterCollision(fixtureA, fixtureB);
		checkMonsterWallCollision(contact, fixtureA, fixtureB);

	}

	private void checkMonsterWallCollision(Contact contact, Object fixtureA, Object fixtureB) {
		// check if fixtureA or fixtureB is Monster and collision with wall
		if ((fixtureA instanceof Character || fixtureB instanceof Character)
				&& (fixtureA instanceof String || fixtureB instanceof String)) {	
			WorldManifold wm = contact.getWorldManifold();
			if(wm.getNormal().x == -1 || wm.getNormal().x == 1){
				isLeftTouched = false;
				isRightTouched = false;
			}
			Gdx.app.log("GoingHome", String.format("Normal %f %f", wm.getNormal().x, wm.getNormal().y));
		}
	}

	private void checkMonsterCharacterCollision(Object fixtureA, Object fixtureB) {
		if ((fixtureA instanceof BaseMonster || fixtureB instanceof BaseMonster)
				&& (fixtureA instanceof Character || fixtureB instanceof Character)) {
			BaseMonster m = null;
			Character c = null;
			if (fixtureA instanceof BaseMonster) {
				m = (BaseMonster) fixtureA;
				c = (Character) fixtureB;
			} else {
				m = (BaseMonster) fixtureB;
				c = (Character) fixtureA;
			}
			if (c.body.getPosition().y > m.body.getPosition().y
					&& c.body.getLinearVelocity().y < 0) {
				m.die();
			}
		}

	}

	private void checkWallMonsterCollision(Object fixtureA, Object fixtureB) {
		// check if fixtureA or fixtureB is Monster and collision with wall
		if ((fixtureA instanceof BaseMonster || fixtureB instanceof BaseMonster)
				&& (fixtureA instanceof String || fixtureB instanceof String)) {
			BaseMonster m = null;
			String wallType = null;
			if (fixtureA instanceof BaseMonster) {
				m = (BaseMonster) fixtureA;
				wallType = (String) fixtureB;
			} else {
				m = (BaseMonster) fixtureB;
				wallType = (String) fixtureA;
			}
			if (wallType.equalsIgnoreCase("1")) {
				// tabrakan dengan wall, reverse monstar
				m.reverse();
			} else {
				// tabrakan dengan platform, set linear velocity
				m.startWalk();
			}
		}
	}

	private void checkWallMonsterUnCollision(Object fixtureA, Object fixtureB) {
		// check if fixtureA or fixtureB is Monster and collision with wall
		if ((fixtureA instanceof BaseMonster || fixtureB instanceof BaseMonster)
				&& (fixtureA instanceof String || fixtureB instanceof String)) {
			BaseMonster m = null;
			String wallType = null;
			if (fixtureA instanceof BaseMonster) {
				m = (BaseMonster) fixtureA;
				wallType = (String) fixtureB;
			} else {
				m = (BaseMonster) fixtureB;
				wallType = (String) fixtureA;
			}
			if (!wallType.equalsIgnoreCase("1")) {
				// keluar dari platform, stop movement
				m.stop();
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		Object fixtureA = contact.getFixtureA().getBody().getUserData();
		Object fixtureB = contact.getFixtureB().getBody().getUserData();
		if (fixtureA == null || fixtureB == null)
			return;

		checkWallMonsterUnCollision(fixtureA, fixtureB);

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
}
