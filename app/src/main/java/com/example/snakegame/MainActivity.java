package com.example.snakegame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Button startButton;
    private Button resetButton;
    private TextView scoreTextView;
    private SurfaceView gameSurfaceView;

    private SnakeGame snakeGame;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) { //metoda wywoływana przy starcie aktywności
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        scoreTextView = findViewById(R.id.scoreTextView);
        gameSurfaceView = findViewById(R.id.gameSurfaceView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        snakeGame = new SnakeGame(gameSurfaceView, scoreTextView);

        startButton.setOnClickListener(view -> snakeGame.startGame());
        resetButton.setOnClickListener(view -> snakeGame.resetGame());
    }


    public void backToMainMenu(View view) { //metoda przenosząca do menu głównego
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() { //metoda wywoływana przy wznowieniu aktywności
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() { //metoda wywoływana przy zatrzymaniu aktywności
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) { //metoda wywoływana przy zmianie wartości sensora
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0]/1000;
            float y = event.values[1]/1000;

            snakeGame.updateDirection(x, y);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
