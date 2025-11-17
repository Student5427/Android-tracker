package com.example.mytest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrackingActivity extends AppCompatActivity {

    private MapView mapView;
    private Button startButton, finishButton;
    private TextView distanceText, speedText, avgSpeedText;

    private boolean isTracking = false;
    private List<GeoPoint> pathPoints = new ArrayList<>();
    private Polyline pathPolyline;
    private Marker currentPositionMarker;
    private Marker startMarker;

    private float totalDistance = 0;
    private List<Float> speedSamples = new ArrayList<>();

    private static final int LOCATION_PERMISSION_REQUEST = 1;

    // Broadcast Receiver для получения обновлений из сервиса
    private BroadcastReceiver locationUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TrackingService.ACTION_LOCATION_UPDATE.equals(intent.getAction())) {
                Location location = intent.getParcelableExtra(TrackingService.EXTRA_LOCATION);
                float distance = intent.getFloatExtra(TrackingService.EXTRA_DISTANCE, 0);
                float speed = intent.getFloatExtra(TrackingService.EXTRA_SPEED, 0);

                // Обновляем данные из сервиса
                totalDistance = distance;

                if (location != null) {
                    processLocationFromService(location, speed);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_tracking);

        initViews();
        setupMap();

        // Регистрируем Broadcast Receiver
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(locationUpdateReceiver,
                        new IntentFilter(TrackingService.ACTION_LOCATION_UPDATE));

        if (!checkLocationPermission()) {
            requestLocationPermission();
        } else {
            getCurrentLocation();
        }
    }

    private void initViews() {
        mapView = findViewById(R.id.mapView);
        startButton = findViewById(R.id.startButton);
        finishButton = findViewById(R.id.finishButton);
        distanceText = findViewById(R.id.distanceText);
        speedText = findViewById(R.id.speedText);
        avgSpeedText = findViewById(R.id.avgSpeedText);

        startButton.setOnClickListener(v -> startTracking());
        finishButton.setOnClickListener(v -> finishTracking());

        finishButton.setEnabled(false);
        updateUI();
    }

    private void setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setMinZoomLevel(3.0);
        mapView.setMaxZoomLevel(20.0);

        GeoPoint defaultPoint = new GeoPoint(55.7558, 37.6173);
        mapView.getController().setCenter(defaultPoint);
        mapView.getController().setZoom(10.0);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (!checkLocationPermission()) return;

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        centerMapOnLocation(location, "Текущее местоположение");
                    }
                });
    }

    private void centerMapOnLocation(Location location, String title) {
        GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

        mapView.getController().setCenter(userLocation);
        mapView.getController().setZoom(16.0);

        if (currentPositionMarker != null) {
            mapView.getOverlays().remove(currentPositionMarker);
        }

        currentPositionMarker = new Marker(mapView);
        currentPositionMarker.setPosition(userLocation);
        currentPositionMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        currentPositionMarker.setTitle(title);
        mapView.getOverlays().add(currentPositionMarker);

        mapView.invalidate();
    }

    private void startTracking() {
        if (!checkLocationPermission()) {
            requestLocationPermission();
            return;
        }

        isTracking = true;
        startButton.setEnabled(false);
        finishButton.setEnabled(true);

        // Сбрасываем данные
        pathPoints.clear();
        totalDistance = 0;
        speedSamples.clear();

        // Очищаем карту
        mapView.getOverlays().clear();
        pathPolyline = null;
        currentPositionMarker = null;
        startMarker = null;

        // Запускаем сервис для фонового трекинга
        Intent serviceIntent = new Intent(this, TrackingService.class);
        serviceIntent.setAction("START_TRACKING");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        Toast.makeText(this, "Трекинг начат! Работает в фоне...", Toast.LENGTH_SHORT).show();
    }

    private void finishTracking() {
        isTracking = false;
        startButton.setEnabled(true);
        finishButton.setEnabled(false);

        // Останавливаем сервис
        Intent serviceIntent = new Intent(this, TrackingService.class);
        serviceIntent.setAction("STOP_TRACKING");
        stopService(serviceIntent);

        float averageSpeed = calculateAverageSpeed();

        Toast.makeText(this,
                String.format(Locale.getDefault(),
                        "Трекинг завершен!\nДистанция: %.2f км\nСредняя скорость: %.1f км/ч",
                        totalDistance, averageSpeed),
                Toast.LENGTH_LONG).show();

        updateUI();
    }

    // Обработка локаций из сервиса
    private void processLocationFromService(Location location, float speed) {
        GeoPoint newPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

        // Добавляем точку в путь
        pathPoints.add(newPoint);

        // Сохраняем скорость для расчета средней
        if (speed > 0.5f) {
            speedSamples.add(speed);
        }

        // Обновляем карту и UI
        updatePath();
        updateUI();
        updateCurrentPositionMarker(newPoint);

        // Центрируем карту если приложение активно
        if (!isFinishing()) {
            mapView.getController().animateTo(newPoint);
        }

        // Добавляем стартовый маркер при первой точке
        if (pathPoints.size() == 1) {
            startMarker = new Marker(mapView);
            startMarker.setPosition(newPoint);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            startMarker.setTitle("Старт");
            mapView.getOverlays().add(startMarker);
        }
    }

    private void updatePath() {
        if (pathPoints.size() < 2) return;

        if (pathPolyline != null) {
            mapView.getOverlays().remove(pathPolyline);
        }

        pathPolyline = new Polyline();
        pathPolyline.setPoints(pathPoints);
        pathPolyline.setColor(0xFFFF0000);
        pathPolyline.setWidth(12.0f);

        mapView.getOverlays().add(pathPolyline);
        mapView.invalidate();
    }

    private void updateCurrentPositionMarker(GeoPoint position) {
        if (currentPositionMarker != null) {
            mapView.getOverlays().remove(currentPositionMarker);
        }

        currentPositionMarker = new Marker(mapView);
        currentPositionMarker.setPosition(position);
        currentPositionMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        currentPositionMarker.setTitle("Вы здесь");

        mapView.getOverlays().add(currentPositionMarker);
        mapView.invalidate();
    }

    private void updateUI() {
        float currentSpeed = 0;
        if (!speedSamples.isEmpty()) {
            currentSpeed = speedSamples.get(speedSamples.size() - 1);
        }
        float averageSpeed = calculateAverageSpeed();

        distanceText.setText(String.format(Locale.getDefault(), "Дистанция: %.2f км", totalDistance));
        speedText.setText(String.format(Locale.getDefault(), "Текущая: %.1f км/ч", currentSpeed));
        avgSpeedText.setText(String.format(Locale.getDefault(), "Средняя: %.1f км/ч", averageSpeed));
    }

    private float calculateAverageSpeed() {
        if (speedSamples.isEmpty()) return 0;
        float sum = 0;
        for (float speed : speedSamples) {
            sum += speed;
        }
        return sum / speedSamples.size();
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешение на локацию получено!", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Отписываемся от receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationUpdateReceiver);
    }
}