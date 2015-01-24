package com.phinisi.goinghome.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.phinisi.goinghome.utilities.BodyFactory;

public class PhysicsObject {

	public Body body;
	public Sprite charSprite;
	
	public void update(){
		this.charSprite.setPosition((this.body.getPosition().x * BodyFactory.PIXELS_TO_METERS) - charSprite.getTexture().getWidth()/2, (this.body.getPosition().y * BodyFactory.PIXELS_TO_METERS) - charSprite.getTexture().getHeight()/2);
	}
}
