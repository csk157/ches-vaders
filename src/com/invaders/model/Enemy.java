package com.invaders.model;

import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;

import com.invaders.BaseActivity;

public class Enemy {
	public Sprite sprite;
	public int hp;
	// the max health for each enemy
	protected final int MAX_HEALTH = 2;

	public Enemy() {
		sprite = new Sprite(0, 0, BaseActivity.getSharedInstance().mEnemy, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		init();
	}

	// method for initializing the Enemy object , used by the constructor and
	// the EnemyPool class
	public void init() {
		hp = MAX_HEALTH;
		sprite.registerEntityModifier(new LoopEntityModifier(
				new SequenceEntityModifier(new RotationModifier(3, -30, 30), new RotationModifier(3, 30, -30))));
		
	}

	public void clean() {
		sprite.clearEntityModifiers();
		sprite.clearUpdateHandlers();
	}

	// method for applying hit and checking if enemy died or not
	// returns false if enemy died
	public boolean gotHit() {
		synchronized (this) {
			hp--;
			if (hp <= 0)
				return false;
			else
				return true;
		}
	}

}
