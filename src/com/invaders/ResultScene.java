package com.invaders;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.database.Cursor;

public class ResultScene extends CameraScene{// implements IOnSceneTouchListener {
	boolean done;
	BaseActivity activity;
	private InputText regScore;
	private Text result;
	private Sprite registerButton;
	private Sprite retryButton;

	public ResultScene(Camera mCamera) {
		super(mCamera);

		activity = BaseActivity.getSharedInstance();
		setBackgroundEnabled(false);
		GameScene scene = (GameScene) activity.mCurrentScene;
		float accuracy = 1 - (float) scene.missCount / scene.bulletCount;
		if (Float.isNaN(accuracy))
			accuracy = 0;
		accuracy *= 100;
		result = new Text(0, 0, activity.mFont,
				activity.getString(R.string.accuracy) + ": "
						+ String.format("%.2f", accuracy) + "%", BaseActivity
						.getSharedInstance().getVertexBufferObjectManager());

		final int x = (int) (mCamera.getWidth() / 2 - result.getWidth() / 2);
		final int y = (int) (mCamera.getHeight() / 2 - result.getHeight() / 2);

		done = false;
		result.setPosition(x, mCamera.getHeight() + result.getHeight());
		MoveYModifier mod = new MoveYModifier(5, result.getY(), y) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				done = true;
			}
		};
		attachChild(result);
		result.registerEntityModifier(mod);
//		setOnSceneTouchListener(this);

		regScore = new InputText(50, 50, "Name", "Enter your name",
				activity.inputBg, activity.mFont, 5, 5,
				activity.getVertexBufferObjectManager(), activity);
		attachChild(regScore);
		registerTouchArea(regScore);

		registerButton = new Sprite(10, 10, activity.mRegScoreButton,
				activity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (regScore.getText() != null && pSceneTouchEvent.isActionUp()) {
					ResultScene.this.detachChild(this);
					ResultScene.this.detachChild(regScore);
					ResultScene.this.detachChild(result);
					GameScene scene = (GameScene) activity.mCurrentScene;

					float accuracy = 1 - (float) scene.missCount
							/ scene.bulletCount;
					if (Float.isNaN(accuracy))
						accuracy = 0;
					accuracy *= 100;
					activity.db.insertRating(regScore.getText(), accuracy);
					Cursor c = activity.db.getRatings(3);

					int height = 0;
					int index = 0;

					while (c.moveToNext()) {
						Text row = new Text(10, 10 + (height * index),
								activity.mFont, c.getString(c
										.getColumnIndex("name"))
										+ ": "
										+ String.format("%.2f", c.getFloat(c
												.getColumnIndex("accuracy")))
										+ "%", BaseActivity.getSharedInstance()
										.getVertexBufferObjectManager());
						height = (int) row.getHeight();
						index++;
						ResultScene.this.attachChild(row);
					}
				}
				return true;
			}
		};
		attachChild(registerButton);
		registerTouchArea(registerButton);
		setTouchAreaBindingOnActionDownEnabled(true);

		retryButton = new Sprite(10, 10, activity.mRetryButton,
				activity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					ResultScene.this.detachChild(registerButton);
					ResultScene.this.detachChild(this);
					((GameScene) activity.mCurrentScene).resetValues();
					return false;
				}
				return true;
			}
		};
		retryButton.setPosition(activity.CAMERA_WIDTH - retryButton.getWidth() - 10, 10);
		attachChild(retryButton);
		registerTouchArea(retryButton);
		setTouchAreaBindingOnActionDownEnabled(true);
	}

//	@Override
//	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
//		// TODO Auto-generated method stub
//		return true;
//	}

}
