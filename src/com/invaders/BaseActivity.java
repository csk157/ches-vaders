package com.invaders;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;

public class BaseActivity extends SimpleBaseGameActivity {

	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;

	public Font mFont;
	public Camera mCamera;
	private ITexture mTexture;
	public ITextureRegion mShip;
	public ITextureRegion mEnemy;
	public ITextureRegion mBg;
	public ITextureRegion mRegScoreButton;

	// A reference to the current scene
	public Scene mCurrentScene;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	public TiledTextureRegion inputBg;
	public DbConnection db;
	public TextureRegion mRetryButton;
	public static BaseActivity instance;

	@Override
	public EngineOptions onCreateEngineOptions() {
		instance = this;
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		db = new DbConnection(getBaseContext());
		
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
	}

	@Override
	protected void onCreateResources() {
		mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		mFont.load();
		try {
			this.mTexture = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("ship.png");
						}
					});

			this.mTexture.load();
			this.mShip = TextureRegionFactory
					.extractFromTexture(this.mTexture);
		
			this.mTexture = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("retry_button.png");
				}
			});
			
			this.mTexture.load();
			this.mRetryButton = TextureRegionFactory
					.extractFromTexture(this.mTexture);
			
			this.mTexture = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("enemy.png");
						}
					});

			this.mTexture.load();
			this.mEnemy = TextureRegionFactory
					.extractFromTexture(this.mTexture);
			
			this.mTexture = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("bg.png");
				}
			});
			
			this.mTexture.load();
			this.mBg = TextureRegionFactory
					.extractFromTexture(this.mTexture);
			this.mTexture = new BitmapTexture(this.getTextureManager(),
					new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("ratings_button.png");
				}
			});
			
			this.mTexture.load();
			this.mRegScoreButton = TextureRegionFactory
					.extractFromTexture(this.mTexture);
			
			
			this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 200, 50);
	        this.inputBg = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "inp.png", 0, 0, 1, 1);
	        this.mBitmapTextureAtlas.load();
		} catch (IOException e) {
			Debug.e(e);
		}
	}

	@Override
	protected Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		mCurrentScene = new SplashScene();
		return mCurrentScene;
	}

	public static BaseActivity getSharedInstance() {
		return instance;
	}

	// to change the current main scene
	public void setCurrentScene(Scene scene) {
		mCurrentScene = scene;
		getEngine().setScene(mCurrentScene);
	}

	@Override
	public void onBackPressed() {
		if (mCurrentScene instanceof GameScene)
			((GameScene) mCurrentScene).detach();

		mCurrentScene = null;
		SensorListener.instance = null;
		super.onBackPressed();
	}

}