package com.example.inputin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNama;
    private EditText editTextNPM;
    private EditText editTextProgramStudi;
    private EditText editTextFakultas;
    private Button buttonSimpan;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Initialize views
        editTextNama = findViewById(R.id.editTextNama);
        editTextNPM = findViewById(R.id.editTextNPM);
        editTextProgramStudi = findViewById(R.id.editTextProgramStudi);
        editTextFakultas = findViewById(R.id.editTextFakultas);
        buttonSimpan = findViewById(R.id.buttonSimpan);
        textViewResult = findViewById(R.id.textViewResult);
        
        // Set button click listener
        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void simpanData() {
        // Get input values
        String nama = editTextNama.getText().toString().trim();
        String npm = editTextNPM.getText().toString().trim();
        String programStudi = editTextProgramStudi.getText().toString().trim();
        String fakultas = editTextFakultas.getText().toString().trim();
        
        // Check if all fields are filled
        if (nama.isEmpty() || npm.isEmpty() || programStudi.isEmpty() || fakultas.isEmpty()) {
            textViewResult.setText("Mohon lengkapi semua data!");
            textViewResult.setVisibility(View.VISIBLE);
            return;
        }
        
        // Create result message
        String resultMessage = "Kamu adalah " + nama + ", dengan NPM " + npm + 
                              " dari Program Studi " + programStudi + ", Fakultas " + fakultas + 
                              ", Universitas Negeri Mertoyudan.";
        
        // Display result
        textViewResult.setText(resultMessage);
        textViewResult.setVisibility(View.VISIBLE);
    }
}