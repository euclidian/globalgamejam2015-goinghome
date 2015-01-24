package com.phinisi.goinghome.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

public class PhysicsObject {

	public Body body;
	public Sprite charSprite;
	
	public void update(){
		this.charSprite.setPosition(this.body.getPosition().x - charSprite.getTexture().getWidth()/2, this.body.getPosition().y - charSprite.getTexture().getHeight()/2);
	}
}
