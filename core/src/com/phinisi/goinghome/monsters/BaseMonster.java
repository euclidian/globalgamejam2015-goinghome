package com.phinisi.goinghome.monsters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.phinisi.goinghome.entities.PhysicsObject;
import com.phinisi.goinghome.utilities.BodyFactory;
import com.phinisi.goinghome.utilities.Constants;

public abstract class BaseMonster extends PhysicsObject{

	public enum MonsterType{
		Monster1,
		Monster2,
		Monster3
	}
		
	protected MonsterType monsterType;
	
	protected float wakeTime;
	protected float liveTime = 0;
	protected boolean isDie = false;
	
	protected abstract void create();
	
	java.util.Random r = new java.util.Random(System.currentTimeMillis());
	
	public BaseMonster(World world){
		create();
		this.body = BodyFactory.CreateBody(Constants.GRAPHIC_WIDTH/2, Constants.GRAPHIC_HEIGHT + 10, charSprite.getWidth(), charSprite.getHeight(), BodyType.DynamicBody, world);
		this.body.setActive(false);
		this.body.setUserData(this);		
	}	
	
	public MonsterType getMonsterType(){
		return monsterType;
	}
	
	public void setMonsterType(MonsterType monsterType){
		this.monsterType = monsterType;
	}
	
	public void update(float deltaTime){
		liveTime+=deltaTime;
		if(liveTime > wakeTime){
			this.body.setActive(true);
		}
		super.update(deltaTime);
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
		
	}

	public void startWalk() {
		Vector2 v = new Vector2(0, 0);		
		int b = r.nextInt(100);
		v.x = b < 50 ? 5 : -5;
		this.body.setLinearVelocity(v);
		
	}

	public void stop() {
		Vector2 v = body.getLinearVelocity();
		v.x = 0;
		body.setLinearVelocity(v);
		
	}

	public void die() {
		isDie = true;
	}
	
	public boolean isDie(){
		return isDie;
	}
	
	
	
}
