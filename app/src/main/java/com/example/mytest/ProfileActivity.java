package com.example.mytest;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private EditText firstNameEdit, lastNameEdit, birthDateEdit, emailEdit, heightEdit, weightEdit;
    private LinearLayout saveProfileButton;
    private ProfileManager profileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileManager = ProfileManager.getInstance(this);

        initViews();
        loadProfileData();
        setupButtons();
    }

    private void initViews() {
        firstNameEdit = findViewById(R.id.profileFirstName);
        lastNameEdit = findViewById(R.id.profileLastName);
        birthDateEdit = findViewById(R.id.profileBirthDate);
        emailEdit = findViewById(R.id.profileEmail);
        heightEdit = findViewById(R.id.profileHeight);
        weightEdit = findViewById(R.id.profileWeight);
        saveProfileButton = findViewById(R.id.saveProfileButton);
    }

    private void loadProfileData() {
        Profile profile = profileManager.loadProfile();

        firstNameEdit.setText(profile.getFirstName());
        lastNameEdit.setText(profile.getLastName());
        birthDateEdit.setText(profile.getBirthDate());
        emailEdit.setText(profile.getEmail());
        heightEdit.setText(String.valueOf(profile.getHeight()));
        weightEdit.setText(String.valueOf(profile.getWeight()));
    }

    private void setupButtons() {
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void saveProfile() {
        try {
            Profile profile = new Profile();
            profile.setFirstName(firstNameEdit.getText().toString().trim());
            profile.setLastName(lastNameEdit.getText().toString().trim());
            profile.setBirthDate(birthDateEdit.getText().toString().trim());
            profile.setEmail(emailEdit.getText().toString().trim());

            // Проверка и сохранение числовых значений
            if (!heightEdit.getText().toString().isEmpty()) {
                profile.setHeight(Integer.parseInt(heightEdit.getText().toString()));
            } else {
                profile.setHeight(180);
            }

            if (!weightEdit.getText().toString().isEmpty()) {
                profile.setWeight(Float.parseFloat(weightEdit.getText().toString()));
            } else {
                profile.setWeight(70.0f);
            }

            profileManager.saveProfile(profile);
            Toast.makeText(this, "Профиль сохранен", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Проверьте правильность ввода роста и веса", Toast.LENGTH_SHORT).show();
        }
    }
}