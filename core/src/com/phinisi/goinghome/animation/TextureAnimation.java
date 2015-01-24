package com.phinisi.goinghome.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextureAnimation extends BaseAnimation{

	Texture[] textures;
	boolean isFlip = false;
	public TextureAnimation(float animationTime, boolean isLooping, Texture...textures ) {
		super(animationTime, isLooping);		
		this.textures = textures;
		this.isLooping = isLooping;
	}

	@Override
	public void draw(SpriteBatch batch) {
		float teta = 0f;
		if(isLooping){
			teta = stateTime - (float)Math.floor(stateTime);
			if(teta > animationTime) teta -= animationTime;
		}else{
			teta = stateTime;
		}
		int index = (int)Math.floor(teta / (animationTime / textures.length));
		Gdx.app.log("GoingHome", String.format("animasi: %f teta-asli: %f, teta: %f",(animationTime / textures.length),teta,(teta / (animationTime / textures.length))));
		Gdx.app.log("GoingHome", "index2: "+index);
		if(index >= textures.length){
			index = textures.length - 1;
		}		
		Texture t = textures[index];
		Gdx.app.log("GoingHome", "index: "+index);
		batch.draw(t, getPosX(), getPosY(), t.getWidth(), 
				t.getHeight(), 0, 0, 
				(int)t.getWidth(), (int)t.getHeight(),isFlip,false);
//		batch.dra
//		batch.dra
//		batch.draw(t, getPosX(), getPosY(), 			
//				t.getWidth(), 
//				t.getHeight(), 				
//				getPosX(), 
//				0, 
//				0, 
//				0, 
//				isFlip, false);
	}

	public void setFlip(boolean b) {
		isFlip = b;
	}

}
