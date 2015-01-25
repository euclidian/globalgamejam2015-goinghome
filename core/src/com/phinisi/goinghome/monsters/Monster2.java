package com.phinisi.goinghome.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.phinisi.goinghome.animation.TextureAnimation;
public class Monster2 extends BaseMonster {

	public Monster2(World world){
		super(world);
	}

	@Override
	protected void create() {
		this.charSprite = new Sprite(new Texture("monster/monster2.png"));
		this.monsterType = MonsterType.Monster2;
		animation = new TextureAnimation(0.5f, true, 
				new Texture("monster/Skull-head.png"),
				new Texture("monster/Skull-head-2.png"));
		animation.setActive(true);	
	}
	
	
	
}
