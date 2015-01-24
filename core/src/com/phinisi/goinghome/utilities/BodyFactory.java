package com.phinisi.goinghome.utilities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public  class BodyFactory {
	public static Body CreateBody(float posX, float posY, float width, float height, BodyType bodyType, World world ){
		BodyDef bodyDef = new BodyDef();
		switch (bodyType) {
		case KinematicBody:
			throw new UnsupportedOperationException("A");			

		case DynamicBody:
			bodyDef.type = BodyType.DynamicBody;
			bodyDef.position.set(posX + width/2, posY + height/2);
			Body body = world.createBody(bodyDef);
			//create a box shape
//			CircleShape shape = new CircleShape();
			PolygonShape shape = new PolygonShape();
			//shape.setAsBox(width/2, height/2);
			shape.setAsBox(15, 15);
			//create a fixture
			FixtureDef fixturedef = new FixtureDef();
			fixturedef.shape = shape;			
			fixturedef.friction = 0.4f;
			fixturedef.density = 1f;
			
			//attach to body
			body.createFixture(fixturedef);
			shape.dispose();
			
			return body;			
		case StaticBody:
			throw new UnsupportedOperationException("B");			
		default:
			break;
		}
		return null;
	}
}
