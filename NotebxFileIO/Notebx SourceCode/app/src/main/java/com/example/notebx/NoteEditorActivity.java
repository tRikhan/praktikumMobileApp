package com.example.notebx;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NoteEditorActivity extends AppCompatActivity {
    private TextInputEditText editTextTitle;
    private TextInputEditText editTextContent;
    private Button buttonSave;
    private Button buttonCancel;
    private Button buttonDelete;
    
    private boolean isEditMode = false;
    private String originalFileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        initViews();
        setupClickListeners();
        loadNoteData();
    }

    private void initViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonDelete = findViewById(R.id.buttonDelete);
    }

    private void setupClickListeners() {
        buttonSave.setOnClickListener(v -> saveNote());
        buttonCancel.setOnClickListener(v -> finish());
        buttonDelete.setOnClickListener(v -> showDeleteConfirmation());
    }    private void loadNoteData() {
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("is_edit_mode", false);
        
        if (isEditMode) {
            String title = intent.getStringExtra("note_title");
            String content = intent.getStringExtra("note_content");
            originalFileName = intent.getStringExtra("note_filename");
            
            editTextTitle.setText(title != null ? title : "");
            editTextContent.setText(content != null ? content : "");
            buttonDelete.setVisibility(Button.VISIBLE);
        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Please enter some content", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create safe filename
        String safeTitle = title.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "_");
        String fileName = safeTitle + ".txt";

        // Create NoteBX directory in Documents
        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File notebxDir = new File(documentsDir, "Notebx");
        
        if (!notebxDir.exists()) {
            notebxDir.mkdirs();
        }

        // If editing and filename changed, delete old file
        if (isEditMode && !originalFileName.equals(fileName)) {
            File oldFile = new File(notebxDir, originalFileName);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        File noteFile = new File(notebxDir, fileName);

        try {
            FileWriter writer = new FileWriter(noteFile);
            writer.write("Title: " + title + "\n\n" + content);
            writer.close();
            
            Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Delete", (dialog, which) -> deleteNote())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteNote() {
        if (isEditMode && !TextUtils.isEmpty(originalFileName)) {
            File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File notebxDir = new File(documentsDir, "Notebx");
            File noteFile = new File(notebxDir, originalFileName);
            
            if (noteFile.exists() && noteFile.delete()) {
                Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error deleting note", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
