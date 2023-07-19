package com.example.wastemanagementvrushabh.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.wastemanagementvrushabh.MainActivity;
import com.example.wastemanagementvrushabh.R;
import com.example.wastemanagementvrushabh.databinding.FragmentGalleryBinding;
import com.example.wastemanagementvrushabh.databinding.FragmentHomeBinding;
import com.example.wastemanagementvrushabh.ui.contact_us.SlideshowFragment;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
FragmentHomeBinding binding;
FirebaseAuth auth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();
        ((MainActivity)getActivity()).getSupportActionBar().hide();
        ((MainActivity)getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        binding.btnComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_complaint);
            }
        });
        return binding.getRoot();
    }
}