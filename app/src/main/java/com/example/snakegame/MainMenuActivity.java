package com.example.snakegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) { //metoda wywoływana przy starcie aktywności
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button authorButton = findViewById(R.id.authorButton);
        Button playButton = findViewById(R.id.playButton);

        authorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAuthorInfoDialog();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showAuthorInfoDialog() { //metoda wyświetlająca informacje o autorze
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informacje o autorze")
                .setMessage("Autor: Mateusz Wojtas\nKierunek: Informatyka")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //przycisk OK zamykający okno dialogowe
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
