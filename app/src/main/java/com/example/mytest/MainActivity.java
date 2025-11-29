package com.example.mytest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView dateTitle;
    private LinearLayout calendarDaysLayout;
    private HorizontalScrollView calendarScrollView;
    private LinearLayout completedButton, plannedButton;
    private LinearLayout planWorkoutButtonBlack;
    private TextView completedText, plannedText;
    private LinearLayout workoutListLayout;
    private LinearLayout startWorkoutButton;
    private LinearLayout todayButton;
    private ImageView todayButtonIcon;

    private Calendar currentCalendar;
    private int selectedDay = -1;
    private int selectedMonth = -1;
    private int selectedYear = -1;
    private boolean isCompletedMode = true;

    private WorkoutManager workoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workoutManager = WorkoutManager.getInstance(this);

        initViews();
        setupCalendar();
        setupButtons();
        updateUI();
    }

    private void initViews() {
        dateTitle = findViewById(R.id.dateTitle);
        calendarDaysLayout = findViewById(R.id.calendarDaysLayout);
        calendarScrollView = findViewById(R.id.calendarScrollView);
        completedButton = findViewById(R.id.completedButton);
        plannedButton = findViewById(R.id.plannedButton);
        completedText = findViewById(R.id.completedText);
        plannedText = findViewById(R.id.plannedText);
        workoutListLayout = findViewById(R.id.workoutListLayout);
        startWorkoutButton = findViewById(R.id.startWorkoutButton);
        planWorkoutButtonBlack = findViewById(R.id.planWorkoutButtonBlack);

        todayButton = findViewById(R.id.todayButton);
        todayButtonIcon = findViewById(R.id.todayButtonIcon);

        currentCalendar = Calendar.getInstance();
        selectedDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth = currentCalendar.get(Calendar.MONTH);
        selectedYear = currentCalendar.get(Calendar.YEAR);
    }

    private String getDateKey(int day, int month, int year) {
        return day + "-" + month + "-" + year;
    }

    private String getSelectedDateKey() {
        return getDateKey(selectedDay, selectedMonth, selectedYear);
    }

    private List<Workout> getCompletedWorkoutsForSelectedDate() {
        return workoutManager.getCompletedWorkoutsForDate(getSelectedDateKey());
    }

    private List<Workout> getPlannedWorkoutsForSelectedDate() {
        return workoutManager.getPlannedWorkoutsForDate(getSelectedDateKey());
    }

    private void setupCalendar() {
        updateCalendar();
        addCalendarNavigation();
    }

    private void updateCalendar() {
        calendarDaysLayout.removeAllViews();

        // Числа месяца - показываем текущую неделю (7 дней)
        Calendar tempCalendar = (Calendar) currentCalendar.clone();

        // Находим понедельник текущей недели
        tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 7; i++) {
            final int day = tempCalendar.get(Calendar.DAY_OF_MONTH);
            final int month = tempCalendar.get(Calendar.MONTH);
            final int year = tempCalendar.get(Calendar.YEAR);

            LinearLayout dayContainer = new LinearLayout(this);
            dayContainer.setOrientation(LinearLayout.VERTICAL);
            dayContainer.setGravity(Gravity.CENTER);


            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                    dpToPx(30), LinearLayout.LayoutParams.WRAP_CONTENT
            );

            // Тут кароче эти ебаные отступы в календаре, надо фиксить.
            if (i == 0) {

                containerParams.setMargins(dpToPx(2), 0, dpToPx(4), 0);
            } else if (i == 6) {

                containerParams.setMargins(dpToPx(4), 0, dpToPx(2), 0);
            } else {

                containerParams.setMargins(dpToPx(4), 0, dpToPx(4), 0);
            }

            dayContainer.setLayoutParams(containerParams);

            TextView dayNumber = new TextView(this);
            dayNumber.setText(String.valueOf(day));
            dayNumber.setTextSize(14);
            dayNumber.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams numberParams = new LinearLayout.LayoutParams(
                    dpToPx(28), dpToPx(28)
            );
            numberParams.gravity = Gravity.CENTER;
            dayNumber.setLayoutParams(numberParams);

            // Проверяем, выбран ли этот день
            if (day == selectedDay && month == selectedMonth && year == selectedYear) {
                // Оранжевый кружок для выбранной даты
                GradientDrawable circle = new GradientDrawable();
                circle.setShape(GradientDrawable.OVAL);
                circle.setColor(Color.parseColor("#ffb94f"));
                dayNumber.setBackground(circle);
                dayNumber.setTextColor(Color.WHITE);
            } else {
                dayNumber.setTextColor(Color.parseColor("#525760"));
                dayNumber.setBackground(null);
            }

            dayContainer.addView(dayNumber);
            dayContainer.setOnClickListener(v -> {
                selectedDay = day;
                selectedMonth = month;
                selectedYear = year;
                currentCalendar.set(year, month, day);
                updateCalendar();
                updateUI();
            });

            calendarDaysLayout.addView(dayContainer);
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Прокручиваем к началу
        calendarScrollView.post(() -> {
            calendarScrollView.scrollTo(0, 0);
        });
    }

    private void addCalendarNavigation() {
        TextView prevButton = findViewById(R.id.prevWeekBtn);
        TextView nextButton = findViewById(R.id.nextWeekBtn);

        prevButton.setOnClickListener(v -> {
            currentCalendar.add(Calendar.WEEK_OF_YEAR, -1);
            updateCalendar();
            updateUI();
        });

        nextButton.setOnClickListener(v -> {
            currentCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            updateCalendar();
            updateUI();
        });
    }

    private void setupButtons() {
        completedButton.setOnClickListener(v -> {
            isCompletedMode = true;
            updateButtonStates();
            updateWorkoutList();
            updateBottomButtons(); // Добавь этот вызов
        });

        plannedButton.setOnClickListener(v -> {
            isCompletedMode = false;
            updateButtonStates();
            updateWorkoutList();
            updateBottomButtons(); // Добавь этот вызов
        });

        startWorkoutButton.setOnClickListener(v -> {
            showWorkoutTypeSelectionDialog(false);
        });

        planWorkoutButtonBlack.setOnClickListener(v -> {
            showWorkoutTypeSelectionDialog(true);
        });

        // Обработчик для кнопки "Сегодня"
        todayButton.setOnClickListener(v -> {
            navigateToToday();
        });
    }
    private void updateBottomButtons() {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedYear, selectedMonth, selectedDay);
        Calendar today = Calendar.getInstance();

        // Сбрасываем время для корректного сравнения дат
        selectedDate.set(Calendar.HOUR_OF_DAY, 0);
        selectedDate.set(Calendar.MINUTE, 0);
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        boolean isToday = selectedDate.equals(today);
        boolean isPast = selectedDate.before(today);
        boolean isFuture = selectedDate.after(today);

        // Сначала скрываем все кнопки
        startWorkoutButton.setVisibility(View.GONE);
        planWorkoutButtonBlack.setVisibility(View.GONE);

        if (isCompletedMode) {
            if (isToday) {

                startWorkoutButton.setVisibility(View.VISIBLE);
            }

        } else {
            if (isToday || isFuture) {

                planWorkoutButtonBlack.setVisibility(View.VISIBLE);
            }

        }
    }

    private void updateTodayButton() {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedYear, selectedMonth, selectedDay);
        Calendar today = Calendar.getInstance();

        // Сбрасываем время для корректного сравнения дат
        selectedDate.set(Calendar.HOUR_OF_DAY, 0);
        selectedDate.set(Calendar.MINUTE, 0);
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // Убедимся что ImageView виден
        todayButtonIcon.setVisibility(View.VISIBLE);

        // Удаляем текстовый View если он был добавлен ранее
        if (todayButton.getChildCount() > 1) {
            for (int i = 1; i < todayButton.getChildCount(); i++) {
                if (todayButton.getChildAt(i) instanceof TextView) {
                    todayButton.removeViewAt(i);
                    break;
                }
            }
        }

        if (selectedDate.equals(today)) {
            // Сегодня - показываем обычную иконку
            todayButtonIcon.setImageResource(R.drawable.icon);
            todayButtonIcon.getLayoutParams().width = dpToPx(24);
            todayButtonIcon.getLayoutParams().height = dpToPx(24);
            todayButton.setClickable(true);
            todayButton.setFocusable(true);
            // Устанавливаем обработчик для открытия профиля
            todayButton.setOnClickListener(v -> {
                openProfile();
            });
        } else if (selectedDate.before(today)) {
            // Прошлая дата - показываем иконку перехода из прошлого (увеличиваем размер)
            todayButtonIcon.setImageResource(R.drawable.today_past);
            todayButtonIcon.getLayoutParams().width = dpToPx(64);
            todayButtonIcon.getLayoutParams().height = dpToPx(64);
            todayButton.setClickable(true);
            todayButton.setFocusable(true);
            // Устанавливаем обработчик для перехода на сегодня
            todayButton.setOnClickListener(v -> {
                navigateToToday();
            });
        } else {
            // Будущая дата - показываем иконку перехода из будущего (увеличиваем размер)
            todayButtonIcon.setImageResource(R.drawable.today_future);
            todayButtonIcon.getLayoutParams().width = dpToPx(64);
            todayButtonIcon.getLayoutParams().height = dpToPx(64);
            todayButton.setClickable(true);
            todayButton.setFocusable(true);
            // Устанавливаем обработчик для перехода на сегодня
            todayButton.setOnClickListener(v -> {
                navigateToToday();
            });
        }

        // Принудительно обновляем layout
        todayButtonIcon.requestLayout();
    }

    private void openProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
    private void navigateToToday() {
        Calendar today = Calendar.getInstance();
        selectedDay = today.get(Calendar.DAY_OF_MONTH);
        selectedMonth = today.get(Calendar.MONTH);
        selectedYear = today.get(Calendar.YEAR);

        // Обновляем currentCalendar чтобы календарь показал правильную неделю
        currentCalendar.setTime(today.getTime());

        updateCalendar();
        updateUI();
    }
    private void updateButtonStates() {
        if (isCompletedMode) {
            // Выполнено активно
            completedButton.setBackgroundResource(R.drawable.left_button_background_orange);
            completedText.setTextColor(Color.WHITE);
            plannedButton.setBackgroundResource(R.drawable.right_button_background);
            plannedText.setTextColor(Color.parseColor("#525760"));
        } else {
            // Запланированно активно
            completedButton.setBackgroundResource(R.drawable.left_button_background);
            completedText.setTextColor(Color.parseColor("#525760"));
            plannedButton.setBackgroundResource(R.drawable.right_button_background_orange);
            plannedText.setTextColor(Color.WHITE);
        }
    }

    private void updateUI() {
        updateDateTitle();
        updateButtonStates();
        updateWorkoutList();
        updateBottomButtons();
        updateTodayButton();
    }

    private void updateDateTitle() {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedYear, selectedMonth, selectedDay);

        Calendar today = Calendar.getInstance();

        if (selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                selectedDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                selectedDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            dateTitle.setText("Сегодня");
        } else {
            // Ручное форматирование месяцев в именительном падеже
            String[] months = {
                    "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                    "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
            };

            String monthName = months[selectedMonth];
            int year = selectedYear;

            dateTitle.setText(monthName + " " + year);
        }
    }

    private void updateWorkoutList() {
        workoutListLayout.removeAllViews();

        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedYear, selectedMonth, selectedDay);
        Calendar today = Calendar.getInstance();

        boolean isToday = selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                selectedDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                selectedDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);

        boolean isPast = selectedDate.before(today) && !isToday;

        if (isCompletedMode) {
            // Режим "Выполнено"
            List<Workout> completedWorkouts = getCompletedWorkoutsForSelectedDate();

            if (isToday) {
                if (completedWorkouts.isEmpty()) {
                    // Ничего не выполнено сегодня - пустое пространство
                    showEmptyState();
                } else {
                    showCompletedWorkouts();
                }
            } else if (isPast) {
                if (completedWorkouts.isEmpty()) {
                    showRestDayUI(R.drawable.past_chill, "В этот день вы отдыхали");
                } else {
                    showCompletedWorkouts();
                }
            } else {

                showEmptyState();
            }
        } else {
            // Режим "Запланировано"
            List<Workout> plannedWorkouts = getPlannedWorkoutsForSelectedDate();

            if (isToday) {
                if (plannedWorkouts.isEmpty()) {

                    showEmptyState();
                } else {
                    showPlannedWorkouts();
                }
            } else if (isPast) {
                if (plannedWorkouts.isEmpty()) {
                    showRestDayUI(R.drawable.plan_chill, "В этот день вы планировали отдыхать");
                } else {
                    showPlannedWorkouts();
                }
            } else {
                if (plannedWorkouts.isEmpty()) {
                    showEmptyState();
                } else {
                    showPlannedWorkouts();
                }
            }
        }
    }

    private void showCompletedWorkouts() {
        List<Workout> completedWorkouts = getCompletedWorkoutsForSelectedDate();
        for (Workout workout : completedWorkouts) {
            View workoutView = createWorkoutView(workout, R.drawable.see, false);
            workoutListLayout.addView(workoutView);
        }
    }

    private void showPlannedWorkouts() {
        List<Workout> plannedWorkouts = getPlannedWorkoutsForSelectedDate();
        for (Workout workout : plannedWorkouts) {
            View workoutView = createWorkoutView(workout, R.drawable.see, true);
            workoutListLayout.addView(workoutView);
        }
    }

    private View createWorkoutView(Workout workout, int actionIconRes, boolean isPlanned) {
        LinearLayout workoutLayout = new LinearLayout(this);
        workoutLayout.setOrientation(LinearLayout.HORIZONTAL);
        workoutLayout.setBackground(createRoundedRectDrawable(Color.WHITE, dpToPx(8)));
        workoutLayout.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(60)
        );
        layoutParams.setMargins(0, 0, 0, dpToPx(8));
        workoutLayout.setLayoutParams(layoutParams);

        // Иконка тренировки
        ImageView workoutIcon = new ImageView(this);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dpToPx(32), dpToPx(32));
        iconParams.setMargins(0, 0, dpToPx(16), 0);
        workoutIcon.setLayoutParams(iconParams);

        // Установка иконки в зависимости от типа тренировки
        int workoutIconRes = 0;
        if (workout.getType() != null) {
            switch (workout.getType()) {
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
            workoutIcon.setImageResource(workoutIconRes);
        }
        workoutLayout.addView(workoutIcon);

        // Текст тренировки
        TextView workoutText = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
        );
        workoutText.setLayoutParams(textParams);
        workoutText.setText(workout.getTitle() != null ? workout.getTitle() : "Тренировка");
        workoutText.setTextSize(14);
        workoutText.setTextColor(Color.parseColor("#525760"));
        workoutText.setGravity(Gravity.CENTER_VERTICAL);
        workoutLayout.addView(workoutText);

        // Кнопка действия
        ImageView actionButton = new ImageView(this);
        LinearLayout.LayoutParams actionParams = new LinearLayout.LayoutParams(dpToPx(24), dpToPx(24));
        actionButton.setLayoutParams(actionParams);

        if (isPlanned) {
            // Для запланированных тренировок проверяем дату
            if (isSelectedDateToday()) {
                // Дата сегодня - показываем кнопку "Начать"
                actionButton.setImageResource(R.drawable.start);
                actionButton.setOnClickListener(v -> {
                    startPlannedWorkout(workout);
                });
            } else {
                // Дата не сегодня (прошлая или будущая) - скрываем кнопку
                actionButton.setVisibility(View.GONE);
            }
        } else {
            // Для выполненных тренировок всегда показываем кнопку "Просмотр"
            actionButton.setImageResource(actionIconRes);
            actionButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, WorkoutDetailActivity.class);
                intent.putExtra("workout_id", workout.getId());
                startActivity(intent);
            });
        }

        workoutLayout.addView(actionButton);

        return workoutLayout;
    }

    private void startPlannedWorkout(Workout plannedWorkout) {
        // Удаляем из запланированных
        workoutManager.removePlannedWorkout(getSelectedDateKey(), plannedWorkout);

        // Создаем новую тренировку для выполнения
        Workout workout = new Workout();
        workout.setType(plannedWorkout.getType());
        workout.setTitle(plannedWorkout.getTitle().replace(" (План)", ""));
        workout.setPlanned(false);

        // Запускаем трекинг
        Intent intent = new Intent(MainActivity.this, TrackingActivity.class);
        intent.putExtra("workout_type", workout.getType());
        intent.putExtra("workout_id", workout.getId());
        intent.putExtra("is_planned", true);
        intent.putExtra("date_key", getSelectedDateKey()); // Передаем ключ даты
        startActivity(intent);

        // Обновляем UI
        updateWorkoutList();
    }

    private boolean isSelectedDateToday() {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(selectedYear, selectedMonth, selectedDay);
        Calendar today = Calendar.getInstance();

        // Сбрасываем время для корректного сравнения дат
        selectedDate.set(Calendar.HOUR_OF_DAY, 0);
        selectedDate.set(Calendar.MINUTE, 0);
        selectedDate.set(Calendar.SECOND, 0);
        selectedDate.set(Calendar.MILLISECOND, 0);

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return selectedDate.equals(today);
    }
    private void showRestDayUI(int imageResource, String text) {
        LinearLayout restLayout = new LinearLayout(this);
        restLayout.setOrientation(LinearLayout.VERTICAL);
        restLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, dpToPx(40), 0, 0);
        restLayout.setLayoutParams(layoutParams);

        // Изображение
        ImageView restImage = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(dpToPx(250), dpToPx(250));
        imageParams.setMargins(0, 0, 0, dpToPx(16));
        restImage.setLayoutParams(imageParams);
        restImage.setImageResource(imageResource);
        restLayout.addView(restImage);

        // Текст
        TextView restText = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        restText.setLayoutParams(textParams);
        restText.setText(text);
        restText.setTextSize(14);
        restText.setTextColor(Color.parseColor("#80858E"));
        restLayout.addView(restText);

        workoutListLayout.addView(restLayout);
    }

    private void showEmptyState() {
        // Просто пустое состояние - ничего не показываем
    }

    private void showWorkoutTypeSelectionDialog(boolean isPlanning) {
        String[] workoutTypes = {"Бег", "Ходьба", "Велотренировка"};
        String[] workoutTypeKeys = {"run", "walk", "bicycle"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isPlanning ? "Выберите тип тренировки" : "Начать тренировку");
        builder.setItems(workoutTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedType = workoutTypeKeys[which];
                String selectedTitle = workoutTypes[which];

                if (isPlanning) {
                    // Планирование тренировки
                    Workout newWorkout = new Workout();
                    newWorkout.setType(selectedType);
                    newWorkout.setTitle(selectedTitle + " (План)");
                    newWorkout.setPlanned(true);

                    workoutManager.addPlannedWorkout(getSelectedDateKey(), newWorkout);
                    updateWorkoutList();
                } else {
                    // Начало тренировки
                    Workout workout = new Workout();
                    workout.setType(selectedType);
                    workout.setPlanned(false);

                    Intent intent = new Intent(MainActivity.this, TrackingActivity.class);
                    intent.putExtra("workout_type", workout.getType());
                    intent.putExtra("workout_id", workout.getId());
                    intent.putExtra("is_planned", false);
                    intent.putExtra("date_key", getSelectedDateKey()); // Передаем ключ даты
                    startActivity(intent);
                }
            }
        });
        builder.show();
    }

    private GradientDrawable createRoundedRectDrawable(int color, float cornerRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(cornerRadius);
        drawable.setStroke(dpToPx(1), Color.BLACK);
        return drawable;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateWorkoutList();
        updateBottomButtons();
    }
}
