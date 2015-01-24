package com.phinisi.goinghome.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseAnimation {

	protected float stateTime;
	
	protected float animationTime;
	
	protected float posX, posY;
	
	protected boolean isLooping;
	
	 protected boolean isActive = true;
	
	public BaseAnimation(float animationTime, boolean isLooping){
		this.animationTime = animationTime;
	}
	
	public abstract void draw(SpriteBatch batch);
	
	public void update(float delta){
		if(isActive && !isAnimationFinished())
    		stateTime+=delta;
	}
	
	public boolean isAnimationFinished(){
		if(isLooping) return false;
		return stateTime > animationTime;
	}
	
	public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }
    
    public void reset(){
    	stateTime = 0;
    }
    
    public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
