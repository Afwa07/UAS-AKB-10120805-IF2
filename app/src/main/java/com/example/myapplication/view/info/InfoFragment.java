package com.example.myapplication.view.info;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ViewPagerItem;
import com.example.myapplication.adapter.VpAdapter;
import com.example.myapplication.databinding.FragmentInfoBinding;
import com.example.myapplication.view.login.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class InfoFragment extends Fragment {

    private FragmentInfoBinding binding;
    private FirebaseAuth auth;

    ViewPager2 viewPager;

    ArrayList<ViewPagerItem> viewPagerItemArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            binding.tvWelcome.setText("Hello There! ");
        } else {
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        }

        int[] images = {R.drawable.notelist_1, R.drawable.notelist_2};
        String[] headings = {"Aplikasi ini dibuat untuk memenuhi tugas matakuliah Aplikasi Komputasi Bergerak", "Aplikasi ini adalah sebuah aplikasi Note di mana user bisa membuat, mengubah, dan menghapus catatan dengan mudah."};

        viewPagerItemArrayList = new ArrayList<>();

        for (int i = 0; i < images.length; i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem(images[i], headings[i]);
            viewPagerItemArrayList.add(viewPagerItem);
        }

        VpAdapter vpAdapter = new VpAdapter(requireContext(), viewPagerItemArrayList);

        viewPager = binding.viewpager;
        viewPager.setAdapter(vpAdapter);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(2);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        TabLayout tabLayout = binding.tabLayout;
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Set the tab text or icon here
                    tab.setText("Tab " + (position + 1));
                });
        tabLayoutMediator.attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}