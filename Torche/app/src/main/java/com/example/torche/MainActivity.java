package com.example.torche;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

    private CameraManager cameraManager;
    private boolean isFlashlightOn = false;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton flashlightButton = findViewById(R.id.flashlightButton);
        final TextView flashlightStatusText = findViewById(R.id.flashlightStatusText);

        // Fonction onClick
        flashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlashlight(); // toggleFlashlight
                updateFlashlightStatusText(flashlightStatusText); // appeller fonction changement message
            }
        });
        setUpCamera(); // appeller fonction permission Camera
    }

    // Fonction permission Camera
    private void setUpCamera() {
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0]; // En supposant qu'il y ait une caméra
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void toggleFlashlight() {
        if (isFlashlightOn) { // Condition pour allumer ou éteindre la torche
            turnOffFlashlight(); // Appeler le fonction off torche
        } else {
            turnOnFlashlight(); // Appeler le fonction on torche
        }
    }

    // Fonction off torche
    private void turnOffFlashlight() {
        if (cameraManager != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cameraManager.setTorchMode(cameraId, false);
                }
                isFlashlightOn = false;
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.e("Flashlight", "Erreur lors de l'extinction de la lampe de poche: " + e.getMessage());
            }
        }
    }
    // Fonction on torche
    private void turnOnFlashlight() {
        if (cameraManager != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cameraManager.setTorchMode(cameraId, true);
                }
                isFlashlightOn = true;
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.e("Flashlight", "Erreur lors de l'allumage de la lampe de poche: " + e.getMessage());
            }
        }
    }
    // Fonction changement message (La lampe s'allume ou La lampe s'éteint)
    private void updateFlashlightStatusText(TextView flashlightStatusText) {
        String statusText = isFlashlightOn ? "La lampe s'allume" : "La lampe s'éteint";
        flashlightStatusText.setText(statusText);
    }
}
