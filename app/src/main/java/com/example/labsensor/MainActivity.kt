package com.example.labsensor

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private var sensorLight: Sensor? = null
    private var maxValue: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as
                SensorManager
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        val lightSensorListener: SensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                // TODO Auto-generated method stub
            }

            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor?.type != Sensor.TYPE_LIGHT) return

                textView.text = checkLuminousLevel(event.values[0])
                val newValue: Int = ((255f * event.values[0] / maxValue).toInt())
                textView.setBackgroundColor(Color.rgb(newValue, newValue, newValue))
            }
        }

        if (sensorLight != null) {
            maxValue = sensorLight!!.maximumRange
            sensorManager.registerListener(
                lightSensorListener,
                sensorLight,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun checkLuminousLevel(value: Float): String {
        when {
            value == 0.0001f -> return "Moonless, overcast night sky (starlight)"
            value == 0.002f -> return "Moonless clear night sky with airglow"
            value in 0.05..0.3 -> return "Full moon on a clear night"
            value == 3.4f -> return "Dark limit of civil twilight under a clear sky"
            value >= 20 && value < 50 -> return "Public areas with dark surroundings"
            value == 50f -> return "Family living room lights (Australia, 1998)"
            value == 80f -> return "Office building hallway/toilet lighting"
            value == 100f -> return "Very dark overcast day"
            value == 150f -> return "Train station platforms"
            value in 320.0..500.0 -> return "Office lighting"
            value == 400f -> return "Sunrise or sunset on a clear day."
            value == 1000f -> return "Overcast day; typical TV studio lighting"
            value in 10000.0..25000.0 -> return "Full daylight (not direct sun)"
            value in 32000.0..100000.0 -> return "Direct sunlight"
            else ->
                return "Luminosity: $value lx"

        }
    }
}