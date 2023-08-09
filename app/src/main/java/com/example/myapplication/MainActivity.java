package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.view.info.InfoFragment;
import com.example.myapplication.view.login.LoginActivity;
import com.example.myapplication.view.note.NotesFragment;
import com.example.myapplication.view.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    private BottomNavigationView bottomNavigationView;
    private InfoFragment fragment_Info = new InfoFragment();
    private ProfileFragment fragment_Profile = new ProfileFragment();
    private NotesFragment fragment_Notes = new NotesFragment();

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomView);
        bottomNavigationView.setOnItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.infoApp);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infoApp:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment_Info).commit();
                return true;
            case R.id.noteApp:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment_Notes).commit();
                return true;
            case R.id.profileAPP:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment_Profile).commit();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}