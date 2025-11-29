package com.example.mytest;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutManager {
    private static final String PREFS_NAME = "workout_prefs";
    private static final String WORKOUTS_KEY = "workouts";
    private static final String COMPLETED_WORKOUTS_MAP_KEY = "completed_workouts_map";
    private static final String PLANNED_WORKOUTS_MAP_KEY = "planned_workouts_map";

    private static WorkoutManager instance;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private List<Workout> workouts;

    // Добавляем мапы для хранения по датам
    private Map<String, List<Workout>> completedWorkoutsMap;
    private Map<String, List<Workout>> plannedWorkoutsMap;

    private WorkoutManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadAllData();
    }

    public static synchronized WorkoutManager getInstance(Context context) {
        if (instance == null) {
            instance = new WorkoutManager(context);
        }
        return instance;
    }

    private void loadAllData() {
        // Загружаем общий список тренировок
        String workoutsJson = sharedPreferences.getString(WORKOUTS_KEY, "[]");
        Type workoutListType = new TypeToken<List<Workout>>(){}.getType();
        workouts = gson.fromJson(workoutsJson, workoutListType);
        if (workouts == null) {
            workouts = new ArrayList<>();
        }

        // Загружаем выполненные тренировки по датам
        String completedJson = sharedPreferences.getString(COMPLETED_WORKOUTS_MAP_KEY, "{}");
        Type completedMapType = new TypeToken<Map<String, List<Workout>>>(){}.getType();
        completedWorkoutsMap = gson.fromJson(completedJson, completedMapType);
        if (completedWorkoutsMap == null) {
            completedWorkoutsMap = new HashMap<>();
        }

        // Загружаем запланированные тренировки по датам
        String plannedJson = sharedPreferences.getString(PLANNED_WORKOUTS_MAP_KEY, "{}");
        Type plannedMapType = new TypeToken<Map<String, List<Workout>>>(){}.getType();
        plannedWorkoutsMap = gson.fromJson(plannedJson, plannedMapType);
        if (plannedWorkoutsMap == null) {
            plannedWorkoutsMap = new HashMap<>();
        }
    }

    private void saveAllData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Сохраняем общий список тренировок
        String workoutsJson = gson.toJson(workouts);
        editor.putString(WORKOUTS_KEY, workoutsJson);

        // Сохраняем выполненные тренировки по датам
        String completedJson = gson.toJson(completedWorkoutsMap);
        editor.putString(COMPLETED_WORKOUTS_MAP_KEY, completedJson);

        // Сохраняем запланированные тренировки по датам
        String plannedJson = gson.toJson(plannedWorkoutsMap);
        editor.putString(PLANNED_WORKOUTS_MAP_KEY, plannedJson);

        editor.apply();
    }

    public void addWorkout(Workout workout) {
        workouts.add(0, workout); // Добавляем в начало списка
        saveAllData();
    }

    public List<Workout> getWorkouts() {
        return new ArrayList<>(workouts);
    }

    public Workout getWorkoutById(String id) {
        for (Workout workout : workouts) {
            if (workout.getId().equals(id)) {
                return workout;
            }
        }
        return null;
    }

    // Методы для работы с тренировками по датам
    public void addCompletedWorkout(String dateKey, Workout workout) {
        if (!completedWorkoutsMap.containsKey(dateKey)) {
            completedWorkoutsMap.put(dateKey, new ArrayList<>());
        }
        completedWorkoutsMap.get(dateKey).add(workout);
        workouts.add(0, workout); // Также добавляем в общий список
        saveAllData();
    }

    public void addPlannedWorkout(String dateKey, Workout workout) {
        if (!plannedWorkoutsMap.containsKey(dateKey)) {
            plannedWorkoutsMap.put(dateKey, new ArrayList<>());
        }
        plannedWorkoutsMap.get(dateKey).add(workout);
        saveAllData();
    }

    public void removePlannedWorkout(String dateKey, Workout workout) {
        if (plannedWorkoutsMap.containsKey(dateKey)) {
            plannedWorkoutsMap.get(dateKey).remove(workout);
            saveAllData();
        }
    }

    public List<Workout> getCompletedWorkoutsForDate(String dateKey) {
        if (!completedWorkoutsMap.containsKey(dateKey)) {
            completedWorkoutsMap.put(dateKey, new ArrayList<>());
        }
        return new ArrayList<>(completedWorkoutsMap.get(dateKey));
    }

    public List<Workout> getPlannedWorkoutsForDate(String dateKey) {
        if (!plannedWorkoutsMap.containsKey(dateKey)) {
            plannedWorkoutsMap.put(dateKey, new ArrayList<>());
        }
        return new ArrayList<>(plannedWorkoutsMap.get(dateKey));
    }

    // Обновим метод deleteWorkout для полного удаления
    public boolean deleteWorkout(String id) {
        // Удаляем из общего списка
        boolean removedFromList = false;
        for (int i = 0; i < workouts.size(); i++) {
            if (workouts.get(i).getId().equals(id)) {
                workouts.remove(i);
                removedFromList = true;
                break;
            }
        }

        // Удаляем из выполненных тренировок по всем датам
        boolean removedFromCompleted = removeWorkoutFromMap(completedWorkoutsMap, id);

        // Удаляем из запланированных тренировок по всем датам
        boolean removedFromPlanned = removeWorkoutFromMap(plannedWorkoutsMap, id);

        if (removedFromList || removedFromCompleted || removedFromPlanned) {
            saveAllData();
            return true;
        }

        return false;
    }
    // Вспомогательный метод для удаления тренировки из мапы
    private boolean removeWorkoutFromMap(Map<String, List<Workout>> workoutMap, String workoutId) {
        boolean removed = false;
        for (Map.Entry<String, List<Workout>> entry : workoutMap.entrySet()) {
            List<Workout> workoutsForDate = entry.getValue();
            for (int i = workoutsForDate.size() - 1; i >= 0; i--) {
                if (workoutsForDate.get(i).getId().equals(workoutId)) {
                    workoutsForDate.remove(i);
                    removed = true;
                    // Не break, так как тренировка может быть только в одной дате
                }
            }
        }
        return removed;
    }

    public void clearAllWorkouts() {
        workouts.clear();
        completedWorkoutsMap.clear();
        plannedWorkoutsMap.clear();
        saveAllData();
    }
}