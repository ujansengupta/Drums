package com.example.drumapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Drums extends Activity implements SensorEventListener {

	private float mLastX, mLastY, mLastZ;
	private boolean mInitialized;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private final float NOISE = (float) 4;
	SoundPool sp;
	int snare = 0, clap = 0, hihat = 0, kick = 0, crash = 0, ctr = 0;
	TextView tvType;
	ImageView iv;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drums);
		mInitialized = false;
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		snare = sp.load(this, R.raw.snare, 1);
		hihat = sp.load(this, R.raw.hihat, 1);
		kick = sp.load(this, R.raw.kick, 1);
		clap = sp.load(this, R.raw.clap, 1);
		crash = sp.load(this, R.raw.crash, 1);
		tvType = (TextView) findViewById(R.id.drumType);
		iv = (ImageView) findViewById(R.id.image);
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// can be safely ignored for this demo
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		/*
		 * TextView tvX = (TextView) findViewById(R.id.x_axis); TextView tvY =
		 * (TextView) findViewById(R.id.y_axis); TextView tvZ = (TextView)
		 * findViewById(R.id.z_axis);
		 */

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				iv.setImageResource(R.drawable.bass);
				sp.play(kick, 1, 1, 0, 0, 1);
				tvType.setText("Bass");

			}
		});

		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			/*
			 * tvX.setText("0.0"); tvY.setText("0.0"); tvZ.setText("0.0");
			 */

			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE)
				deltaX = (float) 0.0;
			if (deltaY < NOISE)
				deltaY = (float) 0.0;
			if (deltaZ < NOISE)
				deltaZ = (float) 0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			/*
			 * tvX.setText(Float.toString(deltaX));
			 * tvY.setText(Float.toString(deltaY));
			 * tvZ.setText(Float.toString(deltaZ));
			 */
			iv.setVisibility(View.VISIBLE);
			if (deltaY > deltaX) {
				iv.setImageResource(R.drawable.snare);
				sp.play(snare, 1, 1, 0, 0, 1);
				tvType.setText("Snare");
			} else if (deltaZ > deltaY + deltaX) {
				iv.setImageResource(R.drawable.hihat);
				sp.play(hihat, 1, 1, 0, 0, 1);
				tvType.setText("Hihat");
			} else if (deltaX > deltaY) {
				iv.setImageResource(R.drawable.crash);
				sp.play(crash, 1, 1, 0, 0, 1);
				tvType.setText("Crash");
			} else if (deltaZ > deltaX) {
				iv.setImageResource(R.drawable.clap);
				sp.play(clap, 1, 1, 0, 0, 1);
				tvType.setText("Clap");
			} else {
				iv.setImageResource(R.drawable.kit);
				tvType.setText(" ");
			}
		}
	}

}
