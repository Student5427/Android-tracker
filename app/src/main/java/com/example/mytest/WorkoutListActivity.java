package com.example.mytest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import android.content.Intent;

public class WorkoutListActivity extends AppCompatActivity {

    private ListView workoutListView;
    private Button deleteAllButton;
    private WorkoutManager workoutManager;
    private List<Workout> workouts;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        workoutManager = WorkoutManager.getInstance(this);
        workouts = workoutManager.getWorkouts();
        workoutListView = findViewById(R.id.workoutListView);
        deleteAllButton = findViewById(R.id.deleteAllButton);

        displayWorkouts();

        // Обработка клика по тренировке для просмотра деталей
        workoutListView.setOnItemClickListener((parent, view, position, id) -> {
            if (!workouts.isEmpty()) {
                Workout selectedWorkout = workouts.get(position);
                Intent intent = new Intent(WorkoutListActivity.this, WorkoutDetailActivity.class);
                intent.putExtra("workout_id", selectedWorkout.getId());
                startActivity(intent);
            }
        });

        // Долгое нажатие для удаления отдельной тренировки
        workoutListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!workouts.isEmpty()) {
                    showDeleteConfirmationDialog(position);
                }
                return true;
            }
        });

        // Кнопка удаления всех тренировок
        deleteAllButton.setOnClickListener(v -> {
            if (!workouts.isEmpty()) {
                showDeleteAllConfirmationDialog();
            } else {
                Toast.makeText(WorkoutListActivity.this,
                        "Нет тренировок для удаления", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayWorkouts() {
        if (workouts.isEmpty()) {
            String[] emptyList = {"Нет сохраненных тренировок"};
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, emptyList);
            workoutListView.setAdapter(adapter);
            workoutListView.setEnabled(false);
            deleteAllButton.setEnabled(false);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            String[] workoutItems = new String[workouts.size()];

            for (int i = 0; i < workouts.size(); i++) {
                Workout workout = workouts.get(i);
                String dateStr = dateFormat.format(workout.getStartTime());
                workoutItems[i] = String.format(Locale.getDefault(),
                        "%s - %.2f км", dateStr, workout.getTotalDistance());
            }

            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, workoutItems);
            workoutListView.setAdapter(adapter);
            workoutListView.setEnabled(true);
            deleteAllButton.setEnabled(true);
        }
    }

    // Диалог подтверждения удаления одной тренировки
    private void showDeleteConfirmationDialog(int position) {
        Workout workoutToDelete = workouts.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        String workoutDate = dateFormat.format(workoutToDelete.getStartTime());

        new AlertDialog.Builder(this)
                .setTitle("Удаление тренировки")
                .setMessage("Вы уверены, что хотите удалить тренировку от " + workoutDate + "?")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean deleted = workoutManager.deleteWorkout(workoutToDelete.getId());
                        if (deleted) {
                            Toast.makeText(WorkoutListActivity.this,
                                    "Тренировка удалена", Toast.LENGTH_SHORT).show();
                            // Обновляем список
                            workouts = workoutManager.getWorkouts();
                            displayWorkouts();
                        }
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    // Диалог подтверждения удаления всех тренировок
    private void showDeleteAllConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Удаление всех тренировок")
                .setMessage("Вы уверены, что хотите удалить все тренировки? Это действие нельзя отменить.")
                .setPositiveButton("Удалить все", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workoutManager.clearAllWorkouts();
                        Toast.makeText(WorkoutListActivity.this,
                                "Все тренировки удалены", Toast.LENGTH_SHORT).show();
                        // Обновляем список
                        workouts = workoutManager.getWorkouts();
                        displayWorkouts();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем список при возвращении на экран
        workouts = workoutManager.getWorkouts();
        displayWorkouts();
    }
}