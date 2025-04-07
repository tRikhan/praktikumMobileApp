package com.example.perlayoutan;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.perlayoutan.model.Wife;
import com.google.android.material.imageview.ShapeableImageView;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_WIFE = "extra_wife";
    public static final String EXTRA_POSITION = "extra_position";
    private Wife wife;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        
        setTitle("Wife Details");

        wife = getIntent().getParcelableExtra(EXTRA_WIFE);
        position = getIntent().getIntExtra(EXTRA_POSITION, -1);
        
        if (wife != null) {
            updateDisplay();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            showEditDialog();
            return true;
        } else if (id == R.id.action_delete) {
            showDeleteConfirmation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDisplay() {
        ShapeableImageView imageProfile = findViewById(R.id.imageProfile);
        TextView textName = findViewById(R.id.textName);
        TextView textAge = findViewById(R.id.textAge);
        TextView textOrigin = findViewById(R.id.textOrigin);

        imageProfile.setImageResource(wife.getProfileImageResId());
        textName.setText(wife.getName());
        textAge.setText(String.format("Age: %d", wife.getAge()));
        textOrigin.setText(String.format("From: %s", wife.getOrigin()));
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_wife, null);
        
        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editAge = dialogView.findViewById(R.id.editAge);
        EditText editOrigin = dialogView.findViewById(R.id.editOrigin);
        EditText editDescription = dialogView.findViewById(R.id.editDescription);

        // Pre-fill with current data
        editName.setText(wife.getName());
        editAge.setText(String.valueOf(wife.getAge()));
        editOrigin.setText(wife.getOrigin());
        editDescription.setText(wife.getDescription());

        builder.setView(dialogView)
               .setTitle("Edit Wife")
               .setPositiveButton("Save", (dialog, which) -> {
                   if (TextUtils.isEmpty(editName.getText()) || 
                       TextUtils.isEmpty(editAge.getText()) ||
                       TextUtils.isEmpty(editOrigin.getText()) ||
                       TextUtils.isEmpty(editDescription.getText())) {
                       Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                       return;
                   }

                   wife.setName(editName.getText().toString());
                   wife.setAge(Integer.parseInt(editAge.getText().toString()));
                   wife.setOrigin(editOrigin.getText().toString());
                   wife.setDescription(editDescription.getText().toString());
                   
                   updateDisplay();
                   setResult(RESULT_OK);
               })
               .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
            .setTitle("Delete Wife")
            .setMessage("Are you sure you want to delete this wife?")
            .setPositiveButton("Delete", (dialog, which) -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_POSITION, position);
                setResult(RESULT_OK, resultIntent);
                finish();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
}