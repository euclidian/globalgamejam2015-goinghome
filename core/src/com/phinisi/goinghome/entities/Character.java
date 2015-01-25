package com.phinisi.goinghome.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.phinisi.goinghome.animation.TextureAnimation;
import com.phinisi.goinghome.utilities.BodyFactory;
import com.phinisi.goinghome.utilities.Constants;

public class Character extends PhysicsObject{
	
	private Texture texture;
	private Texture texture1;
	private Texture texture2;
	
	private Texture eggTexture;
	
	TextureAnimation animation;
	
	private boolean isCarryingEgg = false;
	boolean lastFacing = false; //kanan
	
	public Character(World world){		
		texture = new Texture("karakter/kar-1.png");
		texture1 = new Texture("karakter/kar-1.png");
		texture2 = new Texture("karakter/kar-2.png");
		
		eggTexture = new Texture("marker.png");
		animation = new TextureAnimation(0.5f, true, texture1,texture2);
		this.charSprite = new Sprite(texture);
		this.charSprite.setPosition(100, 250);
		this.body = BodyFactory.CreateBodyTigaPerempat(100, 250, charSprite.getWidth(), charSprite.getHeight(), BodyType.DynamicBody, world, Constants.CharCategory,(short)0xffff);
		this.body.setUserData(this);
		
	}

	public void jump() {		
		Gdx.app.log("A", "Jump, body mass: "+ body.getMass());
//		this.body.applyLinearImpulse(new Vector2(0, 10000), new Vector2(this.charSprite.getX(), this.charSprite.getY()), false);
		this.body.applyForceToCenter(new Vector2(0, 4), true);
//		this.body.applyLinearImpulse(new Vector2(0, 100),body.getLocalCenter(),true);
//		this.body.setLinearVelocity(0, 1000);
//		float impulse = 1;
//	    body.applyLinearImpulse( new Vector2(0,impulse), new Vector2(body.getPosition().x, body.getPosition().y),true );		
//		body.setLinearVelocity(0, 100000);
	}

	public void moveRight() {		
//		this.body.setLinearVelocity(5, body.getLinearVelocity().y);
		if(this.body.getLinearVelocity().x < 3){
			this.body.applyForceToCenter(new Vector2(1, 0), true);
		}
	}

	public void moveLeft() {
		if(this.body.getLinearVelocity().x > -3 ){
			this.body.applyForceToCenter(new Vector2(-1, 0), true);
		}		
//		this.body.setLinearVelocity(-5, body.getLinearVelocity().y);
	}

	public void stopMoving() {
		if(this.body.getLinearVelocity().x < 0){
			lastFacing = true;
		}else{
			lastFacing = false;
		}
		this.body.setLinearVelocity(0, body.getLinearVelocity().y);		
	}	
	
	@Override
	public void update(float delta) {	
		super.update(delta);
		animation.setPosX(charSprite.getX());
		animation.setPosY(charSprite.getY());
		if(body.getLinearVelocity().x != 0){
			animation.setActive(true);
			animation.update(delta);
			if(body.getLinearVelocity().x < 0){
				animation.setFlip(true);
			}else{
				animation.setFlip(false);
			}
		}else{
			animation.reset();
			animation.setActive(false);
		}		
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {		
		if(animation.isActive()){			
			animation.draw(spriteBatch);
		}else{
			this.charSprite.setTexture(texture);
//			super.draw(spriteBatch);
			spriteBatch.draw(texture, charSprite.getX(), charSprite.getY(), texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight(), lastFacing, false);
		}
		if(isCarryingEgg){
			spriteBatch.draw(eggTexture, charSprite.getX() + charSprite.getWidth()/2 - eggTexture.getWidth()/2, charSprite.getY() + charSprite.getHeight() + 10);
		}
	}

	public boolean isCarryingEgg() {
		return isCarryingEgg;
	}

	public void setCarryingEgg(boolean isCarryingEgg) {
		this.isCarryingEgg = isCarryingEgg;
	}

	public void carryEgg() {		
		this.isCarryingEgg = true;
	}

	public void releaseEgg() {
		this.isCarryingEgg = false;
	}
	
	
}
