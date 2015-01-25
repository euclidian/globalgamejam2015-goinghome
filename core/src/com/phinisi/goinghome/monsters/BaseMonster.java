package com.phinisi.goinghome.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.phinisi.goinghome.animation.TextureAnimation;
import com.phinisi.goinghome.entities.PhysicsObject;
import com.phinisi.goinghome.utilities.BodyFactory;
import com.phinisi.goinghome.utilities.Constants;

public abstract class BaseMonster extends PhysicsObject{

	public enum MonsterType{
		Monster1,
		Monster2,
		Monster3
	}
	
	TextureAnimation fireAnimation;
	TextureAnimation animation;
	
	Texture fire1, fire2;
		
	protected MonsterType monsterType;
	
	protected float wakeTime;
	protected float liveTime = 0;
	protected boolean isDie = false;	
	
	protected abstract void create();
	
	float speed;
	
	boolean isReborn = false;
	boolean hasReborn = false;
	boolean isWalk = false;
	boolean isLeft = false;
	
	java.util.Random r = new java.util.Random(System.currentTimeMillis());
	
	World world;
	
	public BaseMonster(World world){
		create();
		this.body = BodyFactory.CreateBody(Constants.GRAPHIC_WIDTH/2, Constants.GRAPHIC_HEIGHT + 10, 
				charSprite.getWidth(), 
				charSprite.getHeight(), 
				BodyType.DynamicBody, 
				world,
				Constants.MonsterCategory,
				(short)(~Constants.MonsterCategory & ~Constants.CharCategory));
		this.body.setActive(false);
		this.body.setUserData(this);	
		this.speed = 2;
		this.world = world;
		
		
		fire1 = new Texture("monster/Api-1.png");
		fire2 = new Texture("monster/Api-1.png");
		
		fireAnimation = new TextureAnimation(0.5f, true, fire1,fire2);		
		
	}	
	
	public MonsterType getMonsterType(){
		return monsterType;
	}
	
	public void setMonsterType(MonsterType monsterType){
		this.monsterType = monsterType;
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {			
		if(hasReborn){
			fireAnimation.draw(spriteBatch);		
		}
		if(animation != null){
			animation.draw(spriteBatch);
		}
	}
	
	
	
	
	public void update(float deltaTime){
		liveTime+=deltaTime;
		if(liveTime > wakeTime){
			this.body.setActive(true);
		}
		super.update(deltaTime);
				
		animation.setPosition(charSprite.getX(), charSprite.getY());
		fireAnimation.setPosition(charSprite.getX(), charSprite.getY());
		
		if(body.getLinearVelocity().x > 0){
			fireAnimation.setFlip(true);
			animation.setFlip(true);
		}else{
			fireAnimation.setFlip(false);
			animation.setFlip(false);
		}
		animation.update(deltaTime);
		
		if(hasReborn){
			fireAnimation.setActive(true);
			fireAnimation.update(deltaTime);			
		}
		
		if(isReborn){			
			world.destroyBody(body);
//			this.body.setTransform(Constants.GRAPHIC_WIDTH/2/Constants.PIXELS_TO_METERS, (Constants.GRAPHIC_HEIGHT + 20) /Constants.PIXELS_TO_METERS, 0);
			this.body = BodyFactory.CreateBody(Constants.GRAPHIC_WIDTH/2, Constants.GRAPHIC_HEIGHT + 10, 
					charSprite.getWidth(), 
					charSprite.getHeight(), 
					BodyType.DynamicBody, 
					world,
					Constants.MonsterCategory,
					(short)(~Constants.MonsterCategory & ~Constants.CharCategory));		
			speed = Math.min(speed++,8) ;	
			isReborn = false;
			this.body.setActive(false);
			this.body.setUserData(this);			
			isWalk = false;
			isLeft = false;
			liveTime = 0;
			
		}
		
		if(isWalk){
			if(isLeft){
				if(this.body.getLinearVelocity().x > -speed/2){
					this.body.setLinearVelocity(-speed, this.body.getLinearVelocity().y);
				}
			}else{
				if(this.body.getLinearVelocity().x > speed/2){
					this.body.setLinearVelocity(speed, this.body.getLinearVelocity().y);
				}
			}
		}
	}

	public float getWakeTime() {
		return wakeTime;
	}

	public void setWakeTime(float wakeTime) {
		this.wakeTime = wakeTime;
	}

	public void reverse() {
		Vector2 v = this.body.getLinearVelocity();
		v.x = -v.x;
		this.body.setLinearVelocity(v);
		isLeft = !isLeft;
		
	}

	public void startWalk() {
		Vector2 v = new Vector2(0, 0);		
		int b = r.nextInt(100);
		if(b < 50){
			v.x = speed;
			isLeft = false;
		}else{
			v.x = -speed;
			isLeft = true;
		}		
		this.body.setLinearVelocity(v);
		isWalk = true;
		
	}

	public void stop() {
		Vector2 v = body.getLinearVelocity();
		v.x = 0;
		body.setLinearVelocity(v);
		isWalk = false;
		
	}

	public void die() {
		isDie = true;
	}
	
	public boolean isDie(){
		return isDie;
	}

	public void reborn() {	//		
		isReborn = true;	
		hasReborn = true;
	}
	
	
	
}
