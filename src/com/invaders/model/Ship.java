package com.invaders.model;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.invaders.BaseActivity;
import com.invaders.GameScene;

public class Ship {
	public Sprite sprite;
	public static Ship instance;
	Camera mCamera;
	boolean moveable;

	public static Ship getSharedInstance() {
		if (instance == null)
			instance = new Ship();
		return instance;
	}

	private Ship() {
		sprite = new Sprite(0, 0, BaseActivity.getSharedInstance().mShip, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
        mCamera = BaseActivity.getSharedInstance().mCamera;
		sprite.setPosition(mCamera.getWidth() / 2 - sprite.getWidth() / 2,
				mCamera.getHeight() - sprite.getHeight() - 10);
		moveable = true;
	}

	public void moveShip(float accelerometerSpeedX) {
		if (!moveable)
			return;

		if (accelerometerSpeedX != 0) {
			int lL = 0;
			int rL = (int) (mCamera.getWidth() - (int) sprite.getWidth());
			float newX;

			// Calculate New X,Y Coordinates within Limits
			if (sprite.getX() >= lL)
				newX = sprite.getX() + accelerometerSpeedX;
			else
				newX = lL;
			
			if (newX <= rL)
				newX = sprite.getX() + accelerometerSpeedX;
			else
				newX = rL;

			// Double Check That New X,Y Coordinates are within Limits
			if (newX < lL)
				newX = lL;
			else if (newX > rL)
				newX = rL;
			sprite.setPosition(newX, sprite.getY());
		}
	}
	
	// shoots bullets
	public void shoot() {
	    if (!moveable)
	        return;
	    GameScene scene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;

	    Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();
	    b.sprite.setPosition(sprite.getX() + sprite.getWidth() / 2,
	        sprite.getY());
	    MoveYModifier mod = new MoveYModifier(1.5f, b.sprite.getY(),
	        -b.sprite.getHeight());

	    b.sprite.setVisible(true);
	    b.sprite.detachSelf();
	    scene.attachChild(b.sprite);
	    scene.bulletList.add(b);
	    b.sprite.registerEntityModifier(mod);
	    scene.bulletCount++;
	}


	// resets the ship to the middle of the screen
	public void restart() {
	    moveable = false;
	    Camera mCamera = BaseActivity.getSharedInstance().mCamera;
	    MoveXModifier mod = new MoveXModifier(0.2f, sprite.getX(),
	        mCamera.getWidth() / 2 - sprite.getWidth() / 2) {
	        @Override
	        protected void onModifierFinished(IEntity pItem) {
	            super.onModifierFinished(pItem);
	            moveable = true;
	        }
	    };
	    sprite.registerEntityModifier(mod);
	}

}
