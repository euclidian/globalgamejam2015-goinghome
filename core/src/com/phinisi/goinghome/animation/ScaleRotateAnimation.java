package com.phinisi.goinghome.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScaleRotateAnimation extends BaseAnimation{

	private float currentScale = 1;
	private float currentRotation  = 0;
	private int factorScale = 1;
	
	private float maxScale = 2;
	private float maxRotation = 360;
	private int factorRotation = 1;
	
	private int index = 0;
	
	Texture[] textures;
	
	public ScaleRotateAnimation(float animationTime, boolean isLooping, Texture...textures) {
		super(1f, isLooping);		
		this.textures = textures;
		this.isActive = true;
	}

	@Override
	public void draw(SpriteBatch batch) {
						
		batch.draw(textures[index],
				getPosX(), 
				getPosY(),
				(getPosX() + textures[index].getWidth()/2),
				(getPosY() + textures[index].getHeight()/2),
				textures[index].getWidth(), 
				textures[index].getHeight(),
				1, 
				1, 
				0,
				0,
				0,
				100, 
				100,
				false,
				false);		
		
	}
	
	public void update(float delta){
		super.update(delta);
		float teta = 0f;
		if(isLooping){
			if(stateTime > animationTime){
				teta = stateTime - (float)Math.floor(stateTime);
				stateTime = teta;
			}else{
				teta = stateTime;
			}
					
			if(teta > animationTime) teta -= animationTime;
		}else{
			teta = stateTime;
		}
		
		Gdx.app.log("A", String.format("Teta: %f Scale: %f,  Rotation: %f",teta, currentScale, currentRotation));
		
		float scaleAdded = (teta / animationTime) * maxScale;
		if(currentScale > maxScale)factorScale = -1;
		if(currentScale < 1) factorScale = 1;
//		currentScale += factorScale * scaleAdded;
		currentScale+= factorScale;		
		
		float rotationAdded = (teta / animationTime) * maxRotation;
		if(currentRotation > maxRotation) factorRotation = -1;
		if(currentRotation < 0) factorRotation = 1;
//		currentRotation += factorRotation * rotationAdded;
		currentRotation+=10;
		
		
		index = (int)Math.floor(teta / (animationTime / textures.length));		
		if(index >= textures.length){
			index = textures.length - 1;
		}		
	}

}
