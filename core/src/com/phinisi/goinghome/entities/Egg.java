package com.phinisi.goinghome.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.phinisi.goinghome.utilities.BodyFactory;
import com.phinisi.goinghome.utilities.Constants;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class Egg extends PhysicsObject {

	
	World world;
	boolean isCarried;
	Texture texture;
	
	public Egg(World world){
		texture = new Texture("Egg.png");
		this.charSprite = new Sprite(texture);
		this.body = BodyFactory.CreateBody(0, 0, 
				charSprite.getWidth(), 
				charSprite.getHeight(), 
				BodyType.DynamicBody, 
				world, 
				Constants.EggCategory, 
				(short)~(Constants.CharCategory | Constants.MonsterCategory));
		createSensor(world);
		this.body.setUserData(this);
		this.world = world;
	}

	private void createSensor(World world) {
		CircleShape shape = new CircleShape();
		shape.setRadius(2 * charSprite.getWidth() / Constants.PIXELS_TO_METERS);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constants.EggSensorCategory;
		fixtureDef.filter.maskBits = Constants.CharCategory;
		
		this.body.createFixture(fixtureDef);
		shape.dispose();
		
	}

	public void carried() {
		this.world.destroyBody(body);	
		isCarried = true;
	}
	
	public void put(float posX, float posY){
		if(isCarried){
			isCarried = false;
			texture = new Texture("Egg.png");
			this.charSprite = new Sprite(texture);
			this.body = BodyFactory.CreateBody(posX, posY, 
					charSprite.getWidth(), 
					charSprite.getHeight(), 
					BodyType.DynamicBody, 
					world, 
					Constants.EggCategory, 
					(short)~(Constants.CharCategory | Constants.MonsterCategory));
			update(0);
			createSensor(world);
			this.body.setUserData(this);
		}
		
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		if(!isCarried){
			super.draw(spriteBatch);
		}		
	}
}
