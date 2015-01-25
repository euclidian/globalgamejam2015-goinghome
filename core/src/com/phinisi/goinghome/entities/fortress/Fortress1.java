package com.phinisi.goinghome.entities.fortress;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

public class Fortress1 extends BaseFortress{

	public Fortress1(World world) {
		super(world);		
	}

	@Override
	public void create() {		
		Texture texture = new Texture("fortress/Kotak-kayu.png");
		this.charSprite = new Sprite(texture);
		this.hp = 4;
	}

	
}
