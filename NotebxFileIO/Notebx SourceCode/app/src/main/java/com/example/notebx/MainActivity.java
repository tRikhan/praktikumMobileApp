package com.example.notebx;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    
    private RecyclerView notesRecyclerView;
    private TextView emptyStateText;
    private FloatingActionButton fabAddNote;
    private NotesAdapter notesAdapter;
    private List<Note> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        checkPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void initViews() {
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        fabAddNote = findViewById(R.id.fabAddNote);
        
        notesList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        notesAdapter = new NotesAdapter(this, notesList);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setAdapter(notesAdapter);
    }

    private void setupClickListeners() {
        fabAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);
            startActivity(intent);
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
            loadNotes();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadNotes();
            } else {
                Toast.makeText(this, "Storage permission is required to save notes", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadNotes() {
        notesList.clear();
        
        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File notebxDir = new File(documentsDir, "Notebx");
        
        if (notebxDir.exists() && notebxDir.isDirectory()) {
            File[] files = notebxDir.listFiles((dir, name) -> name.endsWith(".txt"));
            
            if (files != null) {
                for (File file : files) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        StringBuilder content = new StringBuilder();
                        String line;
                        String title = "";
                        
                        // Read first line as title
                        if ((line = reader.readLine()) != null && line.startsWith("Title: ")) {
                            title = line.substring(7); // Remove "Title: " prefix
                            reader.readLine(); // Skip empty line
                        }
                        
                        // Read content
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                        reader.close();
                        
                        // Use filename as title if no title found
                        if (title.isEmpty()) {
                            title = file.getName().replace(".txt", "");
                        }
                        
                        notesList.add(new Note(title, content.toString().trim(), file.getName()));
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        updateUI();
    }

    private void updateUI() {
        if (notesList.isEmpty()) {
            notesRecyclerView.setVisibility(View.GONE);
            emptyStateText.setVisibility(View.VISIBLE);
        } else {
            notesRecyclerView.setVisibility(View.VISIBLE);
            emptyStateText.setVisibility(View.GONE);
            notesAdapter.updateNotes(notesList);
        }
    }
}