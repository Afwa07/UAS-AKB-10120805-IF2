package com.example.myapplication.view.note;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.NoteAdapter;
import com.example.myapplication.databinding.FragmentNotesBinding;
import com.example.myapplication.entity.Notes;
import com.example.myapplication.view.login.LoginActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class NotesFragment extends Fragment {
    private FragmentNotesBinding binding;
    private FirebaseAuth auth;

    private DatabaseReference databaseReference;
    private NoteAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Note");

        binding.loading.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.VISIBLE);

        setupUser();
        loadData();

        binding.btnAdd.setOnClickListener(v -> gotoAdd());

    }

    private void loadData() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userNotesRef = databaseReference.child(userId);

            LinearLayoutManager manager = new LinearLayoutManager(requireContext());
            binding.rvNote.setLayoutManager(manager);

            FirebaseRecyclerOptions<Notes> options =
                    new FirebaseRecyclerOptions.Builder<Notes>()
                            .setQuery(userNotesRef, Notes.class)
                            .build();
            adapter = new NoteAdapter(options, (item, noteKey) -> {
                showDetailActivity(item, noteKey);
            });
            binding.rvNote.setAdapter(adapter);

        }
        binding.loading.setVisibility(View.INVISIBLE);
    }

    private void showDetailActivity(Notes item, String noteKey) {
        Intent intent = new Intent(requireContext(), DetailNoteActivity.class);
        intent.putExtra("notes",item);
        intent.putExtra("note_key", noteKey);
        startActivity(intent);
    }

    private void gotoAdd() {
        Intent intent = new Intent(requireContext(), AddNoteActivity.class);
        startActivity(intent);
    }


    private void setupUser() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();

    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.stopListening();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}