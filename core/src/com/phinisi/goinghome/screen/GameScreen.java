package com.phinisi.goinghome.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
import com.phinisi.goinghome.entities.Egg;
import com.phinisi.goinghome.entities.fortress.BaseFortress;
import com.phinisi.goinghome.entities.fortress.Fortress1;
import com.phinisi.goinghome.entities.fortress.FortressGenerator;
import com.phinisi.goinghome.monsters.BaseMonster;
import com.phinisi.goinghome.monsters.Monster1;
import com.phinisi.goinghome.monsters.MonsterGenerator;
import com.phinisi.goinghome.utilities.Constants;
import com.phinisi.goinghome.utilities.Point;

public class GameScreen extends AbstractScreen implements InputProcessor,
		ContactListener {

	
	Texture bg;
	
	public String tmxFilename;
	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;

	// create box2d world
	World box2dWorld;
	// create box2d debugrenderer

	// character
	Character gameCharacter;
	Egg egg;
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	Texture leftButton, rightButton, jumpButton, pickButton;

	float leftButtonPosX, leftButtonPosY;
	float rightButtonPosX, rightButtonPosY;
	float jumpButtonPosX, jumpButtonPosY;
	float pickButtonX, pickButtonY;
	private boolean isLeftTouched;
	private boolean isRightTouched;
	private boolean isUpTouched;

	private boolean isLeftUp = true;
	private boolean isRightUp = true;

	private boolean isEggInRadar = false;
	private boolean isCollideWithFortress = false;

	List<BaseMonster> monsters;

	List<Point> leftPoints;
	List<Point> rightPoints;

	BaseFortress fortress;
	FortressGenerator generator;

	int MaxMonsters = 10;

	Random r = new Random(System.currentTimeMillis());

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
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		box2dWorld = new World(new Vector2(0, -10f), false);
		box2dWorld.setContactListener(this);

		generator = new FortressGenerator(box2dWorld);
		
		bg = new Texture("BG-awan.png");

		loadObjectLayer();

		loadSpawn();

		initControlButton();

		initMonsters();

		initSpawnPoints();

	}

	private void initMonsters() {
		monsters = new ArrayList<BaseMonster>();
		for (int i = 0; i < 10; i++) {
			BaseMonster m = MonsterGenerator.getMonster(box2dWorld);
			m.setWakeTime(i + 1);
			monsters.add(m);
		}
	}

	private void initControlButton() {
		leftButton = new Texture("ui/left.png");
		rightButton = new Texture("ui/right.png");
		jumpButton = new Texture("ui/jump.png");
		pickButton = new Texture("ui/egg.png");

		leftButtonPosX = 20;
		leftButtonPosY = 0;

		rightButtonPosX = 120;
		rightButtonPosY = 0;

		jumpButtonPosX = Constants.GRAPHIC_WIDTH - 2 * jumpButton.getWidth();
		jumpButtonPosY = 0;

		pickButtonX = Constants.GRAPHIC_WIDTH - 5 * pickButton.getWidth();
		pickButtonY = 0;

	}

	private void initSpawnPoints() {
		leftPoints = new ArrayList<Point>();
		rightPoints = new ArrayList<Point>();

		// load layer with name = leftSpawn
		MapLayer leftLayer = tiledMap.getLayers().get("leftSpawn");
		MapObjects leftObjects = leftLayer.getObjects();
		// iterate each object
		for (int i = 0; i < leftObjects.getCount(); i++) {
			Rectangle leftR = ((RectangleMapObject) leftObjects.get(i))
					.getRectangle();
			leftPoints.add(new Point((int) leftR.x, (int) leftR.y));
		}
		// load layer with name = rightSpawn
		MapLayer rightLayer = tiledMap.getLayers().get("rightSpawn");
		MapObjects rightObjects = rightLayer.getObjects();
		// iterate each object
		for (int i = 0; i < leftObjects.getCount(); i++) {
			Rectangle rightR = ((RectangleMapObject) rightObjects.get(i))
					.getRectangle();
			rightPoints.add(new Point((int) rightR.x, (int) rightR.y));
		}
	}

	private void loadSpawn() {

		// create character
		gameCharacter = new Character(box2dWorld);
		egg = new Egg(box2dWorld);

		// load layer with name = spawn
		MapLayer spawnLayer = tiledMap.getLayers().get("spawn");
		MapObjects objects = spawnLayer.getObjects();
		// iterate each object
		for (int i = 0; i < objects.getCount(); i++) {
			RectangleMapObject polyObject = (RectangleMapObject) objects.get(i);
			String spawnType = (String) polyObject.getProperties().get("type");
			if (spawnType == null)
				continue;
			Rectangle r = polyObject.getRectangle();
			if (spawnType.equalsIgnoreCase("1")) {
				gameCharacter.body.setTransform(r.getX()
						/ Constants.PIXELS_TO_METERS, r.getY()
						/ Constants.PIXELS_TO_METERS, 0);
			} else if (spawnType.equalsIgnoreCase("2")) {
				egg.body.setTransform(r.getX() / Constants.PIXELS_TO_METERS,
						r.getY() / Constants.PIXELS_TO_METERS, 0);
			}
		}
	}

	private void loadObjectLayer() {
		// load layer with name = bounds
		MapLayer boundingLayer = tiledMap.getLayers().get("bounds");
		boundingLayer.setVisible(false);
		MapObjects objects = boundingLayer.getObjects();
		// iterate each object
		for (int i = 0; i < objects.getCount(); i++) {
			objects.get(i).setVisible(false);
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
					/ Constants.PIXELS_TO_METERS, (r.y + r.height / 2)
					/ Constants.PIXELS_TO_METERS));
			// create a body from
			Body platformBody = box2dWorld.createBody(platformBodyDef);
			// create a polygon shape
			PolygonShape platformBox = new PolygonShape();
			platformBox.setAsBox(r.width / 2 / Constants.PIXELS_TO_METERS,
					r.height / 2 / Constants.PIXELS_TO_METERS);
			// Create fixture
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.shape = platformBox;
			fixtureDef.friction = 0.5f;
			// check wall type
			if (wallType.equalsIgnoreCase("1")) {
				// it is a turn around wall
				fixtureDef.filter.categoryBits = Constants.WallCategory;
			} else if (wallType.equalsIgnoreCase("2")) {
				// it is a platform
				fixtureDef.filter.categoryBits = Constants.PlatformCategory;
			} else {
				fixtureDef.filter.categoryBits = Constants.FireCategory;
				fixtureDef.filter.maskBits = Constants.MonsterCategory;
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
		Gdx.gl.glClearColor(0, 0, 0.25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		

		update(delta);

		myGame.getSpriteBatch().setProjectionMatrix(camera.combined);
		// myGame.getSpriteBatch().setTransformMatrix(camera.view);
		

		myGame.getSpriteBatch().begin();
		myGame.getSpriteBatch().draw(bg, 0, 0);		
		myGame.getSpriteBatch().end();
		
		tiledMapRenderer.setView(camera);		
		tiledMapRenderer.render();		
		
		myGame.getSpriteBatch().begin();
		gameCharacter.draw(myGame.getSpriteBatch());
		egg.draw(myGame.getSpriteBatch());
		if (fortress != null) {
			fortress.draw(myGame.getSpriteBatch());
		}
		for (BaseMonster m : monsters) {
			m.draw(myGame.getSpriteBatch());
		}
		drawControlUI();
		myGame.getSpriteBatch().end();			

		Matrix4 debugMatrix = myGame
				.getSpriteBatch()
				.getProjectionMatrix()
				.cpy()
				.scale(Constants.PIXELS_TO_METERS, Constants.PIXELS_TO_METERS,
						0);
//		debugRenderer.render(box2dWorld, debugMatrix);
		box2dWorld.step(1 / 45f, 6, 2);
	}

	private void update(float deltaTime) {
		if (isLeftTouched)
			gameCharacter.moveLeft();
		if (isRightTouched)
			gameCharacter.moveRight();
		
		camera.update();
		gameCharacter.update(deltaTime);
		egg.update(deltaTime);

		// update fortress
		generator.update(deltaTime);
		if (fortress != null) {
			fortress.update(deltaTime);
			if(fortress.getHp() <=0){
				fortress.releaseEgg();
				egg.put(fortress.charSprite.getX(),
						fortress.charSprite.getY());
				Rectangle r = new Rectangle(fortress.charSprite.getX() - fortress.charSprite.getWidth(),
						fortress.charSprite.getY(), 
						fortress.charSprite.getWidth() * 2,
						fortress.charSprite.getHeight());
				Rectangle r2 = new Rectangle(gameCharacter.charSprite.getX() - gameCharacter.charSprite.getWidth(),
						gameCharacter.charSprite.getY(), 
						gameCharacter.charSprite.getWidth() * 2,
						gameCharacter.charSprite.getHeight());
				if(r.contains(r2)){
					isEggInRadar = true;
				}
				box2dWorld.destroyBody(fortress.body);
				fortress = null;
			}
		}
		if (fortress == null) {
			if (egg.charSprite.getX() > Constants.GRAPHIC_WIDTH / 2) {
				int index = Math.abs(r.nextInt()) % leftPoints.size();
				fortress = generator.tryGetFortress(leftPoints.get(index)
						.getX(), leftPoints.get(index).getY());
			} else {
				int index = Math.abs(r.nextInt()) % rightPoints.size();
				fortress = generator.tryGetFortress(rightPoints.get(index)
						.getX(), rightPoints.get(index).getY());
			}
		}

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
		
		if(monsters.size() < 5){
			for(int i=0;i<5;i++){
				BaseMonster m = MonsterGenerator.getMonster(box2dWorld);
				m.setWakeTime(i + 1);
				monsters.add(m);
			}
		}
	}

	private void drawControlUI() {
		myGame.getSpriteBatch()
				.draw(leftButton, leftButtonPosX, leftButtonPosY);
		myGame.getSpriteBatch().draw(rightButton, rightButtonPosX,
				rightButtonPosY);
		myGame.getSpriteBatch()
				.draw(jumpButton, jumpButtonPosX, jumpButtonPosY);
		if (isEggInRadar || gameCharacter.isCarryingEgg()) {
			myGame.getSpriteBatch().draw(pickButton, pickButtonX, pickButtonY);
		}
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));
		Gdx.app.log("GoingHome", String.format("Pos: %f,%f", pos.x, pos.y));
		Gdx.app.log("GoingHome", String.format("Pos: %d,%d", screenX, screenY));
		if (isButtonPressed(pos.x, pos.y, leftButtonPosX, leftButtonPosY,
				leftButton.getWidth(), leftButton.getHeight())) {
			isLeftTouched = true;
			isLeftUp = false;
		} else if (isButtonPressed(pos.x, pos.y, rightButtonPosX,
				rightButtonPosY, rightButton.getWidth(),
				rightButton.getHeight())) {
			isRightTouched = true;
			isRightUp = false;
		} else if (isButtonPressed(pos.x, pos.y, jumpButtonPosX,
				jumpButtonPosY, jumpButton.getWidth(), jumpButton.getHeight())
				&& gameCharacter.body.getLinearVelocity().y == 0) {
			gameCharacter.jump();
		} else if (isButtonPressed(pos.x, pos.y, pickButtonX, pickButtonY,
				pickButton.getWidth(), pickButton.getHeight())) {
			boolean test = true;
			if (fortress != null && fortress.isCarryingEgg())
				test = false;
			if (!gameCharacter.isCarryingEgg() && isEggInRadar && test) {
				egg.carried();
				gameCharacter.carryEgg();
				isEggInRadar = false;
			} else if (gameCharacter.isCarryingEgg()) {
				if (isCollideWithFortress) {
					fortress.carryEgg();
					gameCharacter.releaseEgg();
				} else {
					egg.put(gameCharacter.charSprite.getX(),
							gameCharacter.charSprite.getY());
					gameCharacter.releaseEgg();
					isEggInRadar = true;
				}
			}
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
			isLeftUp = true;
			gameCharacter.stopMoving();
		} else if (isButtonPressed(pos.x, pos.y, rightButtonPosX,
				rightButtonPosY, rightButton.getWidth(),
				rightButton.getHeight())) {
			isRightTouched = false;
			isRightUp = true;
			gameCharacter.stopMoving();
		} else if (isButtonPressed(pos.x, pos.y, jumpButtonPosX,
				jumpButtonPosY, jumpButton.getWidth(), jumpButton.getHeight())) {
			isUpTouched = false;
		}
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
		checkCharWallCollision(contact, fixtureA, fixtureB);
		checkCharEggCollision(fixtureA, fixtureB);
		checkCharFortressCollision(fixtureA, fixtureB);
		checkMonsterFortressCollision(contact, fixtureA, fixtureB);

	}

	private void checkMonsterFortressCollision(Contact contact, Object fixtureA, Object fixtureB) {
		if ((fixtureA instanceof BaseMonster || fixtureB instanceof BaseMonster)
				&& (fixtureA instanceof BaseFortress || fixtureB instanceof BaseFortress)) {

			BaseFortress c;
			BaseMonster g;
			if (fixtureA instanceof BaseFortress) {
				c = (BaseFortress) fixtureA;
				g = (BaseMonster) fixtureB;
			} else {
				c = (BaseFortress) fixtureB;
				g = (BaseMonster) fixtureA;
			}
			if(c.damaged()) g.die();
		}
	}

	private void checkCharFortressCollision(Object fixtureA, Object fixtureB) {
		if ((fixtureA instanceof Character || fixtureB instanceof Character)
				&& (fixtureA instanceof BaseFortress || fixtureB instanceof BaseFortress)) {
			isCollideWithFortress = true;
			// Character c;
			// Egg g;
			// if(fixtureA instanceof Character){
			// c = (Character)fixtureA;
			// g = (Egg) fixtureB;
			// }else{
			// c = (Character)fixtureB;
			// g = (Egg) fixtureA;
			// }
			//
			//
		}
	}

	private void checkCharEggCollision(Object fixtureA, Object fixtureB) {
		if ((fixtureA instanceof Character || fixtureB instanceof Character)
				&& (fixtureA instanceof Egg || fixtureB instanceof Egg)) {
			isEggInRadar = true;
			// Character c;
			// Egg g;
			// if(fixtureA instanceof Character){
			// c = (Character)fixtureA;
			// g = (Egg) fixtureB;
			// }else{
			// c = (Character)fixtureB;
			// g = (Egg) fixtureA;
			// }
			//
			//

		}
	}

	private void checkCharWallCollision(Contact contact, Object fixtureA,
			Object fixtureB) {
		// check if fixtureA or fixtureB is Character and collision with wall
		if ((fixtureA instanceof Character || fixtureB instanceof Character)
				&& (fixtureA instanceof String || fixtureB instanceof String)) {
			WorldManifold wm = contact.getWorldManifold();
			if (wm.getNormal().x == -1 || wm.getNormal().x == 1) {
				isLeftTouched = false;
				isRightTouched = false;
			}

			Character m = null;
			String wallType = null;
			if (fixtureA instanceof Character) {
				m = (Character) fixtureA;
				wallType = (String) fixtureB;
			} else {
				m = (Character) fixtureB;
				wallType = (String) fixtureA;
			}
			// check for platform
			if (wallType.equalsIgnoreCase("2") && wm.getNormal().y == 1) {
				if (!isLeftUp)
					isLeftTouched = true;
				if (!isRightUp)
					isRightTouched = true;
			}

			Gdx.app.log(
					"GoingHome",
					String.format("Normal %f %f", wm.getNormal().x,
							wm.getNormal().y));
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
			} else if (wallType.equalsIgnoreCase("2")) {
				// tabrakan dengan platform, set linear velocity
				m.startWalk();
			} else {
				m.reborn();
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
		checkWallCharUnCollision(fixtureA, fixtureB);
		checkCharEggUnCollision(fixtureA, fixtureB);
		checkCharFortressUnCollision(fixtureA, fixtureB);

	}

	private void checkCharFortressUnCollision(Object fixtureA, Object fixtureB) {
		if ((fixtureA instanceof Character || fixtureB instanceof Character)
				&& (fixtureA instanceof BaseFortress || fixtureB instanceof BaseFortress)) {
			isCollideWithFortress = false;
			// Character c;
			// Egg g;
			// if(fixtureA instanceof Character){
			// c = (Character)fixtureA;
			// g = (Egg) fixtureB;
			// }else{
			// c = (Character)fixtureB;
			// g = (Egg) fixtureA;
			// }
			//
			//
		}
	}

	private void checkCharEggUnCollision(Object fixtureA, Object fixtureB) {
		if ((fixtureA instanceof Character || fixtureB instanceof Character)
				&& (fixtureA instanceof Egg || fixtureB instanceof Egg)) {
			isEggInRadar = false;
			// Character c;
			// Egg g;
			// if(fixtureA instanceof Character){
			// c = (Character)fixtureA;
			// g = (Egg) fixtureB;
			// }else{
			// c = (Character)fixtureB;
			// g = (Egg) fixtureA;
			// }
			//
			//

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

	private void checkWallCharUnCollision(Object fixtureA, Object fixtureB) {
		// check if fixtureA or fixtureB is Monster and collision with wall
		if ((fixtureA instanceof Character || fixtureB instanceof Character)
				&& (fixtureA instanceof String || fixtureB instanceof String)) {
			Character m = null;
			String wallType = null;
			if (fixtureA instanceof Character) {
				m = (Character) fixtureA;
				wallType = (String) fixtureB;
			} else {
				m = (Character) fixtureB;
				wallType = (String) fixtureA;
			}
			if (wallType.equalsIgnoreCase("2")) {
				// keluar dari platform, check body
				if (m.body.getLinearVelocity().y > 0) {
					if (!isLeftUp)
						isLeftTouched = true;
					if (!isRightUp)
						isRightTouched = true;
				}
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
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
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

}
