package com.phinisi.goinghome.entities.fortress;

import java.util.Random;

import com.badlogic.gdx.physics.box2d.World;

public class FortressGenerator {

	float fortressCoolDown = 3f;
	float stateTime = 0;
	
	Random r = new Random(System.currentTimeMillis());
	World world;
	public FortressGenerator(World world){
		this.world = world;
	}
	
	public void update(float deltaTime){	
		stateTime += deltaTime;
		if(stateTime > 2 * fortressCoolDown){
			stateTime = 0;
		}
	}
	
	public BaseFortress tryGetFortress(float posX, float posY) {
		if(stateTime > fortressCoolDown){
			int a = Math.abs(r.nextInt()) % 100;
			BaseFortress f;
			if(a < 50){
				f = new Fortress1(world);
			}else if( a >= 50 && a < 80){
				f = new Fortress2(world);
			}else{
				f = new Fortress3(world);
			}
			f.put(posX,posY);
			return f;
		}
		return null;
	}
}
