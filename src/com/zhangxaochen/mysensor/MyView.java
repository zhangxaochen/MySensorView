package com.zhangxaochen.mysensor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View implements SensorEventListener {
	public Bitmap _bitmap;
	private Canvas _canvas = new Canvas(); // 在这里设 bitmap 没用

	int _width;
	float _height;
	float _maxX, _lastX;
	int _speed = 5;
	/**
	 * height/4 所对应的值，默认 SensorManager.STANDARD_GRAVITY
	 */
	float _gap=SensorManager.STANDARD_GRAVITY;
	int _colors[] = new int[6];
	Paint _paint = new Paint();
	float _values[] = { 0, 0, 0 }, _lastValues[] = { 0, 0, 0 };
	// float _xCoord;

	{
		_colors[0] = Color.argb(255, 255, 0, 0);
		_colors[1] = Color.argb(255, 0, 255, 0);
		_colors[2] = Color.argb(255, 0, 0, 255);
		// _colors[3]=Color.argb(255, 255, 0, 0);
	}

	public MyView(Context context) {
		super(context);
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setGap(int sensorType) {
		switch (sensorType) {
		case Sensor.TYPE_GYROSCOPE:
		case Sensor.TYPE_ACCELEROMETER:
		case Sensor.TYPE_GRAVITY:
			_gap = SensorManager.STANDARD_GRAVITY;
			
			break;
		case Sensor.TYPE_ROTATION_VECTOR:
			_gap = 1.f;
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			_gap = 50.f;
			break;
		default:
			break;
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		_width = w;
		_height = h;

		_lastX = _maxX = _width;

		_bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
		_canvas.setBitmap(_bitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 基准线：
		// canvas.drawLine(0, _height / 4, _maxX, _height / 4, _paint);
		// canvas.drawLine(0, _height / 2, _maxX, _height / 2, _paint);
		// canvas.drawLine(0, _height * 3 / 4, _maxX, _height * 3 / 4, _paint);
		// //看来左上角是 00

		canvas.drawBitmap(_bitmap, 0, 0, _paint);
		// canvas.drawBitmap(_bitmap, 0, 0, null); //√

	}

	public void onSensorChanged(SensorEvent event) {
		// System.out.println("onSensorChanged()");
		_lastValues = _values.clone();
		_values = event.values.clone();

		System.out.println("_values: " + _values[0] + ", " + _values[1] + ", "
				+ _values[2]);

		int oldColor = _paint.getColor();
		float oldWidth = _paint.getStrokeWidth();
		_paint.setStrokeWidth(5);

		float newX = _lastX + _speed;
		float base = _height / 2;
		float factor = (float)_height / 4 / _gap;

//		System.out.println("_lastValues[0] * factor:= "+_lastValues[0] * factor);
		
		
		for (int i = 0; i < 3; i++) {
			_paint.setColor(_colors[i]);
			_canvas.drawLine(_lastX, base + _lastValues[i] * factor, newX, base
					+ _values[i] * factor, _paint);
		}

		_lastX = newX;
		// System.out.println("_values[0]*factor:= " + _values[0] * factor);
		_paint.setStrokeWidth(oldWidth);
		_paint.setColor(oldColor);

		if (_lastX >= _maxX) {
			// System.out.println("_lastX>=_maxX");
			_lastX = 0;
			_canvas.drawColor(0xffcccccc); // 这里同时起到 清空屏幕的作用
			_canvas.drawLine(0, _height / 4, _maxX, _height / 4, _paint);
			_canvas.drawLine(0, _height / 2, _maxX, _height / 2, _paint);
			_canvas.drawLine(0, _height * 3 / 4, _maxX, _height * 3 / 4, _paint);
		}

		invalidate();
	}// onSensorChanged

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

}
