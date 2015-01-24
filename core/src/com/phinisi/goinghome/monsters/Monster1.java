package com.phinisi.goinghome.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

public class Monster1 extends BaseMonster{

	public Monster1(World world) {
		super(world);		
	}

	@Override
	protected void create() {		
		this.charSprite = new Sprite(new Texture("monster/monster1.png"));
		this.monsterType = MonsterType.Monster1;
	}
	
}
