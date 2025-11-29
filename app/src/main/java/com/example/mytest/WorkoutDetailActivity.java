package com.example.mytest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WorkoutDetailActivity extends AppCompatActivity {

    private MapView routeMapView;
    private TextView workoutTypeText, durationValue, distanceValue, avgSpeedValue, caloriesValue;
    private ImageView workoutTypeIcon, deleteButton;
    private WorkoutManager workoutManager;
    private Workout workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_workout_detail);

        workoutManager = WorkoutManager.getInstance(this);

        String workoutId = getIntent().getStringExtra("workout_id");
        if (workoutId != null) {
            workout = workoutManager.getWorkoutById(workoutId);
        }

        initViews();
        displayWorkoutDetails();
        displayWorkoutRoute();
        setupDeleteButton();
    }

    private void initViews() {
        routeMapView = findViewById(R.id.routeMapView);
        workoutTypeText = findViewById(R.id.workoutTypeText);
        workoutTypeIcon = findViewById(R.id.workoutTypeIcon);
        deleteButton = findViewById(R.id.deleteButton);
        durationValue = findViewById(R.id.durationValue);
        distanceValue = findViewById(R.id.distanceValue);
        avgSpeedValue = findViewById(R.id.avgSpeedValue);
        caloriesValue = findViewById(R.id.caloriesValue);

        // Настройка карты
        routeMapView.setTileSource(TileSourceFactory.MAPNIK);
        routeMapView.setMultiTouchControls(true);
    }

    private void displayWorkoutDetails() {
        if (workout == null) return;

        // Установка типа тренировки и иконки
        String workoutType = workout.getType();
        String workoutTitle = workout.getTitle();

        workoutTypeText.setText(workoutTitle != null ? workoutTitle : "Тренировка");

        // Установка иконки в зависимости от типа тренировки
        int workoutIconRes = 0;
        if (workoutType != null) {
            switch (workoutType) {
                case "run":
                    workoutIconRes = R.drawable.run;
                    break;
                case "walk":
                    workoutIconRes = R.drawable.walk;
                    break;
                case "bicycle":
                    workoutIconRes = R.drawable.bicycle;
                    break;
            }
        }
        if (workoutIconRes != 0) {
            workoutTypeIcon.setImageResource(workoutIconRes);
        }

        // Форматирование и отображение метрик

        // Длительность
        long durationMs = workout.getDuration();
        long minutes = (durationMs / 1000) / 60;
        long seconds = (durationMs / 1000) % 60;
        durationValue.setText(String.format(Locale.getDefault(), "%d:%02d", minutes, seconds));

        // Дистанция
        distanceValue.setText(String.format(Locale.getDefault(), "%.2f км", workout.getTotalDistance()));

        // Средняя скорость
        avgSpeedValue.setText(String.format(Locale.getDefault(), "%.1f км/ч", workout.getAverageSpeed()));

        // Калории
        caloriesValue.setText(String.format(Locale.getDefault(), "%.0f кКал", workout.getCalories()));
    }

    private void displayWorkoutRoute() {
        if (workout == null || workout.getPathPoints().isEmpty()) return;

        List<LocationPoint> points = workout.getPathPoints();
        List<GeoPoint> geoPoints = new ArrayList<>();

        // Конвертируем точки в GeoPoint
        for (LocationPoint point : points) {
            geoPoints.add(new GeoPoint(point.getLatitude(), point.getLongitude()));
        }

        // Отображаем маршрут
        Polyline polyline = new Polyline();
        polyline.setPoints(geoPoints);
        polyline.setColor(0xFFFF0000);
        polyline.setWidth(12.0f);
        routeMapView.getOverlays().add(polyline);

        // Добавляем маркер старта
        if (!geoPoints.isEmpty()) {
            Marker startMarker = new Marker(routeMapView);
            startMarker.setPosition(geoPoints.get(0));
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            startMarker.setTitle("Старт");
            routeMapView.getOverlays().add(startMarker);

            // Центрируем карту на маршруте
            routeMapView.getController().setCenter(geoPoints.get(0));
            routeMapView.getController().setZoom(15.0);
        }

        routeMapView.invalidate();
    }

    private void setupDeleteButton() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Удаление тренировки")
                .setMessage("Вы уверены, что хотите удалить эту тренировку?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteWorkout();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void deleteWorkout() {
        if (workout != null) {
            boolean deleted = workoutManager.deleteWorkout(workout.getId());
            if (deleted) {
                Toast.makeText(this, "Тренировка удалена", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ошибка при удалении", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (routeMapView != null) {
            routeMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (routeMapView != null) {
            routeMapView.onPause();
        }
    }
}