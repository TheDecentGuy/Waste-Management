package com.example.wastemanagementvrushabh.ui.complaint;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.example.wastemanagementvrushabh.MainActivity;
import com.example.wastemanagementvrushabh.R;
import com.example.wastemanagementvrushabh.databinding.FragmentThanksBinding;


public class thanksFragment extends Fragment {

    FragmentThanksBinding binding;

    public thanksFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentThanksBinding.inflate(inflater, container, false);
//
        ((MainActivity)getActivity()).getSupportActionBar().hide();
        ((MainActivity)getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.yourcomplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_thanksFragment_to_nav_contactus);
            }
        });

        
        return binding.getRoot();
    }
}