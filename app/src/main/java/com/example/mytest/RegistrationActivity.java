package com.example.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Button continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, PasswordActivity.class);

            startActivity(intent);
        });


    }
}