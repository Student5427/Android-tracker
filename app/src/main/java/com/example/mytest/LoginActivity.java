package com.example.mytest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Теперь используем EditText вместо TextInputEditText
        EditText emailField = findViewById(R.id.loginEmail);
        EditText passwordField = findViewById(R.id.loginPassword);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            // Получаем текст из полей
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            // Простая проверка (можно удалить если не нужно)
            if (email.isEmpty() || password.isEmpty()) {
                android.widget.Toast.makeText(LoginActivity.this,
                        "Заполните все поля", android.widget.Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(LoginActivity.this, TrackingActivity.class);
                startActivity(intent);
            }
        });
    }
}