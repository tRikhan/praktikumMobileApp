package com.example.perlayoutan;

import android.os.Bundle;
import android.widget.ListView;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Toast;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

import com.example.perlayoutan.adapter.WifeAdapter;
import com.example.perlayoutan.model.Wife;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private WifeAdapter adapter;
    private ArrayList<Wife> wifeList;
    private FloatingActionButton fabAdd;
    private ActivityResultLauncher<Intent> detailLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        setTitle(R.string.app_name);
        setupDetailLauncher();
        
        // Initialize views
        listView = findViewById(R.id.listView);
        fabAdd = findViewById(R.id.fabAdd);
        
        // Initialize data
        wifeList = new ArrayList<>();
        wifeList.add(new Wife("Rem", "Best maid from Another World", 18, "Re:Zero", R.drawable.wife_1));
        wifeList.add(new Wife("Mai Sakurajima", "Bunny Girl Senpai", 17, "Rascal Does Not Dream of Bunny Girl Senpai", R.drawable.wife_2));
        wifeList.add(new Wife("Marin Kitagawa", "Cosplay enthusiast", 15, "My Dress-Up Darling", R.drawable.wife_3));
        
        // Initialize adapter
        adapter = new WifeAdapter(this, wifeList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Wife selectedWife = wifeList.get(position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_WIFE, selectedWife);
            intent.putExtra(DetailActivity.EXTRA_POSITION, position);
            detailLauncher.launch(intent);
        });

        fabAdd.setOnClickListener(v -> showAddWifeDialog());

        // Edge to edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupDetailLauncher() {
        detailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    int position = result.getData().getIntExtra(DetailActivity.EXTRA_POSITION, -1);
                    if (position != -1) {
                        // Handle delete
                        wifeList.remove(position);
                        adapter.notifyDataSetChanged();
                    } else {
                        // Handle edit
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        );
    }

    private void showAddWifeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_wife, null);
        
        EditText editName = dialogView.findViewById(R.id.editName);
        EditText editAge = dialogView.findViewById(R.id.editAge);
        EditText editOrigin = dialogView.findViewById(R.id.editOrigin);
        EditText editDescription = dialogView.findViewById(R.id.editDescription);

        builder.setView(dialogView)
               .setTitle("Add New Wife")
               .setPositiveButton("Add", (dialog, which) -> {
                   if (TextUtils.isEmpty(editName.getText()) || 
                       TextUtils.isEmpty(editAge.getText()) ||
                       TextUtils.isEmpty(editOrigin.getText()) ||
                       TextUtils.isEmpty(editDescription.getText())) {
                       Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                       return;
                   }

                   String name = editName.getText().toString();
                   int age = Integer.parseInt(editAge.getText().toString());
                   String origin = editOrigin.getText().toString();
                   String description = editDescription.getText().toString();
                   
                   // Add new wife with a default image (cycling through existing ones)
                   int imageResId = R.drawable.wife_1;
                   if (wifeList.size() % 3 == 1) imageResId = R.drawable.wife_2;
                   if (wifeList.size() % 3 == 2) imageResId = R.drawable.wife_3;
                   
                   Wife newWife = new Wife(name, description, age, origin, imageResId);
                   wifeList.add(newWife);
                   adapter.notifyDataSetChanged();
               })
               .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}