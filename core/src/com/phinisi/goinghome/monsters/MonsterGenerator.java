package com.phinisi.goinghome.monsters;

import java.util.Random;

import com.badlogic.gdx.physics.box2d.World;

public class MonsterGenerator {

	static Random r = new Random(System.currentTimeMillis());
	
	public static BaseMonster getMonster(World world){
		int a = Math.abs(r.nextInt()) % 100;
		if(a > 50){
			return new Monster1(world);			
		}
		return new Monster2(world);
	}
}
