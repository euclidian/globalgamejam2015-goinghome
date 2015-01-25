package com.phinisi.goinghome.entities.fortress;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

public class Fortress2 extends BaseFortress{

	public Fortress2(World world) {
		super(world);		
	}

	@Override
	public void create() {
		Texture texture = new Texture("fortress/Peti-biru.png");
		this.charSprite = new Sprite(texture);
		this.hp = 6;
		
	}

}
