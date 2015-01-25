package com.phinisi.goinghome.entities.fortress;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

public class Fortress3 extends BaseFortress{

	public Fortress3(World world) {
		super(world);		
	}

	@Override
	public void create() {		
		Texture texture = new Texture("fortress/Peti-merah.png");
		this.charSprite = new Sprite(texture);
		this.hp = 8;
	}

}
