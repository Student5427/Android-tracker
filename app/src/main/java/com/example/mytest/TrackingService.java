package com.example.mytest;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class TrackingService extends Service {
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean isTracking = false;

    private static final int LOCATION_UPDATE_INTERVAL = 2000;
    private static final int LOCATION_FASTEST_INTERVAL = 1000;
    private static final int NOTIFICATION_ID = 123;
    private static final String CHANNEL_ID = "tracking_service_channel";

    public static final String ACTION_LOCATION_UPDATE = "LOCATION_UPDATE";
    public static final String EXTRA_LOCATION = "location";
    public static final String EXTRA_DISTANCE = "distance";
    public static final String EXTRA_SPEED = "speed";

    private float totalDistance = 0;
    private Location lastLocation;

    private final IBinder binder = new TrackingBinder();

    public class TrackingBinder extends Binder {
        TrackingService getService() {
            return TrackingService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "START_TRACKING".equals(intent.getAction())) {
            startTracking();
        } else if (intent != null && "STOP_TRACKING".equals(intent.getAction())) {
            stopTracking();
        }
        return START_STICKY;
    }

    private void startTracking() {
        if (!hasLocationPermission()) {
            Toast.makeText(this, "Нет разрешения на доступ к локации", Toast.LENGTH_SHORT).show();
            return;
        }

        isTracking = true;
        totalDistance = 0;
        lastLocation = null;

        // Настройка запроса локации
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(2);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null || !isTracking) return;

                for (Location location : locationResult.getLocations()) {
                    if (location != null && location.hasAccuracy() && location.getAccuracy() < 50) {
                        processNewLocation(location);
                    }
                }
            }
        };

        // 🔥 ЯВНАЯ ПРОВЕРКА РАЗРЕШЕНИЯ перед вызовом
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Разрешение на локацию отозвано", Toast.LENGTH_SHORT).show();
            stopTracking();
            return;
        }

        // Запускаем получение локаций (теперь безопасно)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        // Запускаем Foreground Service
        startForeground(NOTIFICATION_ID, createNotification());

        Toast.makeText(this, "Фоновый трекинг запущен", Toast.LENGTH_SHORT).show();
    }

    private void processNewLocation(Location location) {
        // Рассчитываем дистанцию
        if (lastLocation != null) {
            float distance = lastLocation.distanceTo(location);
            totalDistance += distance / 1000; // в километры
        }
        lastLocation = location;

        // Отправляем broadcast с новыми данными
        Intent intent = new Intent(ACTION_LOCATION_UPDATE);
        intent.putExtra(EXTRA_LOCATION, location);
        intent.putExtra(EXTRA_DISTANCE, totalDistance);
        intent.putExtra(EXTRA_SPEED, location.getSpeed() * 3.6f);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void stopTracking() {
        isTracking = false;
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        stopForeground(true);
        stopSelf();

        // Отправляем финальное обновление
        Intent intent = new Intent(ACTION_LOCATION_UPDATE);
        intent.putExtra(EXTRA_DISTANCE, totalDistance);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, TrackingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Спортивный трекер")
                .setContentText("Идет отслеживание тренировки...")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Трекинг тренировки",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Уведомление о работе трекера в фоне");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTracking();
    }
}