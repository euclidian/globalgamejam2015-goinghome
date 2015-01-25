package com.phinisi.goinghome.entities.fortress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.phinisi.goinghome.entities.PhysicsObject;
import com.phinisi.goinghome.utilities.BodyFactory;
import com.phinisi.goinghome.utilities.Constants;

public abstract class BaseFortress extends PhysicsObject{

	World world;
	
	private boolean isCarryingEgg = false;
	private Texture t ;
	protected int hp;
	
	public BaseFortress(World world){
		create();
		t = new Texture("marker.png");
		this.body = BodyFactory.CreateBody(0, 0,
				charSprite.getWidth(),
				charSprite.getHeight(),
				BodyType.DynamicBody,
				world,
				Constants.FortressCategory,
				(short)~(Constants.CharCategory | Constants.MonsterCategory));
		createSensor(world);
		this.body.setUserData(this);
		this.world = world;
		update(0);
	}
	
	public boolean isCarryingEgg() {
		return isCarryingEgg;
	}

	public void setCarryingEgg(boolean isCarryingEgg) {
		this.isCarryingEgg = isCarryingEgg;
	}

	public void carryEgg() {		
		this.isCarryingEgg = true;	
//		this.body.getFixtureList().get(1).getFilterData().maskBits = 
//				(short)(Constants.MonsterCategory | Constants.PlatformCategory); 
	}

	public void releaseEgg() {
		this.isCarryingEgg = false;
	}
	
	private void createSensor(World world2) {		
		CircleShape shape = new CircleShape();
		shape.setRadius(2 * charSprite.getWidth() / Constants.PIXELS_TO_METERS);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constants.FortressSensorCategory;
		fixtureDef.filter.maskBits = Constants.CharCategory;
		
		this.body.createFixture(fixtureDef);
		shape.dispose();
		
		CircleShape shapeMonster = new CircleShape();
		shapeMonster.setRadius(charSprite.getWidth() / Constants.PIXELS_TO_METERS);
		FixtureDef fixtureDefMonster = new FixtureDef();
		fixtureDefMonster.isSensor = true;
		fixtureDefMonster.shape = shapeMonster;
		fixtureDefMonster.filter.categoryBits = Constants.FortressMonsterSensorCategory;
		fixtureDefMonster.filter.maskBits = Constants.MonsterCategory;
		
		this.body.createFixture(fixtureDefMonster);
		shapeMonster.dispose();
	}
	
	public void put(float posX, float posY) {
		Gdx.app.log("A", String.format("X: %f Y: %f", posX, posY));
		this.body.setTransform(posX /Constants.PIXELS_TO_METERS, posY /Constants.PIXELS_TO_METERS, 0);
		update(0);
		
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {		
		super.draw(spriteBatch);
		if(isCarryingEgg){
			spriteBatch.draw(t, charSprite.getX() + charSprite.getWidth()/2 - t.getWidth()/2, charSprite.getY() + charSprite.getHeight() + 10);
		}
	}


	public abstract void create();

	public boolean damaged() {	
		if(isCarryingEgg){
			this.hp --;			
		}			
		return isCarryingEgg;
	}
	
	public int getHp(){
		return this.hp;
	}

	public void die() {
		// TODO Auto-generated method stub
		
	}
}
