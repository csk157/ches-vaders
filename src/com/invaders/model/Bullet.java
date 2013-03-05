package com.invaders.model;

import org.andengine.entity.primitive.Rectangle;

import com.invaders.BaseActivity;

public class Bullet {
    public Rectangle sprite;
    public Bullet() {
        sprite = new Rectangle(0, 0, 3, 5, BaseActivity.getSharedInstance()
            .getVertexBufferObjectManager());

        sprite.setColor(0f, 1f, 0f);
    }
}
