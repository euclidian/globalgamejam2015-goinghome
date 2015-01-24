package com.phinisi.goinghome.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.phinisi.goinghome.utilities.BodyFactory;

public class Character extends PhysicsObject{
	
	public Character(World world){
		Texture texture = new Texture("karakter.png");
		this.charSprite = new Sprite(texture);
		this.charSprite.setPosition(100, 250);
		this.body = BodyFactory.CreateBody(100, 250, charSprite.getWidth(), charSprite.getHeight(), BodyType.DynamicBody, world);
		this.body.setUserData(this);		
	}

	public void jump() {		
		Gdx.app.log("A", "Jump, body mass: "+ body.getMass());
//		this.body.applyLinearImpulse(new Vector2(0, 10000), new Vector2(this.charSprite.getX(), this.charSprite.getY()), false);
//		this.body.applyForceToCenter(new Vector2(0, 500), true);
//		this.body.applyLinearImpulse(new Vector2(0, 100),body.getLocalCenter(),true);
//		this.body.setLinearVelocity(0, 1000);
//		float impulse = body.getMass() * 5000000;
//	    body.applyLinearImpulse( new Vector2(0,impulse), new Vector2(body.getPosition().x, body.getPosition().y),true );		
		body.setLinearVelocity(0, 100000);
	}	
}
