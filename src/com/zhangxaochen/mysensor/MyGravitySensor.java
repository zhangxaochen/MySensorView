package com.zhangxaochen.mysensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MyGravitySensor extends Activity {

	private SensorManager _sm;
	int _sensorType = -1;
	int _sampleRate = -1;

	MyView _myView;
	RadioGroup _radioGroupSensorRate;
	RadioButton _radioButtonSensorFastest;
	RadioButton _radioButtonSensorGame;
	RadioButton _radioButtonSensorUi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_sensor);
		_radioGroupSensorRate = (RadioGroup) findViewById(R.id.radioGroupSensorRate);
		_radioButtonSensorFastest = (RadioButton) findViewById(R.id.radioButtonSensorFastest);
		_radioButtonSensorGame = (RadioButton) findViewById(R.id.radioButtonSensorGame);
		_radioButtonSensorUi = (RadioButton) findViewById(R.id.radioButtonSensorUi);

		_myView = (MyView) findViewById(R.id.myView);

		_sensorType = Sensor.TYPE_GYROSCOPE; //issue
//		_sensorType = Sensor.TYPE_ACCELEROMETER;
		_myView.setGap(_sensorType);
		_sampleRate = SensorManager.SENSOR_DELAY_FASTEST;
		_sampleRate=1;

		_sm = (SensorManager) this.getSystemService(SENSOR_SERVICE);

		_radioGroupSensorRate
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						System.out.println("checkedId:= " + checkedId);
						
						if (checkedId == _radioButtonSensorFastest.getId())
//							_sampleRate = SensorManager.SENSOR_DELAY_FASTEST;
							_sampleRate=50;
						else if (checkedId == _radioButtonSensorGame.getId())
//							_sampleRate = SensorManager.SENSOR_DELAY_GAME;
							_sampleRate=20;
						else if (checkedId == _radioButtonSensorUi.getId())
//							_sampleRate = SensorManager.SENSOR_DELAY_UI;
							_sampleRate=5;

						_sm.unregisterListener(_myView);
						_sm.registerListener(_myView,
								_sm.getDefaultSensor(_sensorType), 1000*1000/_sampleRate);
					}
				});

	}//onCreate

	@Override
	protected void onResume() {
		super.onResume();
		_sm.registerListener(_myView, _sm.getDefaultSensor(_sensorType),
				1000*1000/_sampleRate);
	}

	@Override
	protected void onPause() {
		super.onPause();
		_sm.unregisterListener(_myView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_my_sensor, menu);

		return true;
	}
}

