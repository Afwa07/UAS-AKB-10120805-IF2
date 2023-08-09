package com.example.myapplication.view.note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityDetailNoteBinding;
import com.example.myapplication.entity.Notes;
import com.example.myapplication.view.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailNoteActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private ActivityDetailNoteBinding binding;
    private String noteKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("notes")) {
            Notes notes = intent.getParcelableExtra("notes");
            noteKey = intent.getStringExtra("note_key");

            binding.edtTitle.setText(notes.getTitle());
            binding.edtCategory.setText(notes.getCategory());
            binding.edtContent.setText(notes.getContent());
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Note");

        setupUser();

        binding.btnUpdate.setOnClickListener(v -> updateData());

        binding.btnDelete.setOnClickListener(v -> deleteData());
    }

    private void updateData() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null && noteKey != null) {
            String userId = currentUser.getUid();
            DatabaseReference userNotesRef = databaseReference.child(userId);

            String newTitle = binding.edtTitle.getText().toString();
            String newCategory = binding.edtCategory.getText().toString();
            String newContent = binding.edtContent.getText().toString();

            Notes notes = new Notes(getCurrentDate(),newTitle, newCategory, newContent);

            userNotesRef.child(noteKey).setValue(notes, (error, ref) -> {
                if (error != null) {
                    Toast.makeText(this, "Gagal update" + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "berhasil update", Toast.LENGTH_SHORT).show();

                    finish();
                }
            });
        }
    }

    private void deleteData() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null && noteKey != null) {
            String userId = currentUser.getUid();
            DatabaseReference userNotesRef = databaseReference.child(userId);

            userNotesRef.child(noteKey).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(DetailNoteActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(DetailNoteActivity.this, "Failed to delete data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void setupUser() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        }
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        return formattedDate;
    }
}