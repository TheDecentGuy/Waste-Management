package com.example.wastemanagementvrushabh.ui.contact_us;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wastemanagementvrushabh.MainActivity;
import com.example.wastemanagementvrushabh.Model;
import com.example.wastemanagementvrushabh.databinding.FragmentSlideshowBinding;
import com.example.wastemanagementvrushabh.ui.Adapters.myAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    FragmentSlideshowBinding binding;
    FirebaseUser user;
    DatabaseReference reference;
    String userId;
    myAdapter adapter;
    DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users").child("complaints");



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        binding.recview.setLayoutManager(new LinearLayoutManager(getActivity()));

        ((MainActivity)getActivity()).getSupportActionBar().show();
        ((MainActivity)getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();



//        final ImageView imageurlImageview = (ImageView)getActivity().findViewById(R.id.sImg);
//        final TextView nameTextview = (TextView)getActivity().findViewById(R.id.sName);
//        final TextView phoneTextview= (TextView)getActivity().findViewById(R.id.sPhone);
////        final TextView locationTextview = (TextView)getActivity().findViewById(R.id.sLocation);

        ArrayList<Model> list = new ArrayList<>();

        reference.child(userId).child("complaints").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Model userProfile =snapshot.getValue(Model.class);
                list.clear();
//                if (userProfile!=null){
//                    String imgurl = userProfile.getImageUri();
//                    String name = userProfile.getName();
//                    String phone = userProfile.getPhone();
//                    String latitude = userProfile.getLatitude();
//                    String longitude = userProfile.getLongitude();
//                    String status = userProfile.getStatus();
//
//                    list.add(new Model(imgurl,name,phone,latitude,longitude,userId,status));
//                    myAdapter adapter = new myAdapter(list,getActivity());
//                    binding.recview.setAdapter(adapter);
//
//                }
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Model model = snapshot1.getValue(Model.class);
                    String imgurl = model.getImageUri();
                    String name = model.getName();
                    String phone = model.getPhone();
                    String latitude = model.getLatitude();
                    String longitude = model.getLongitude();
                    String status = model.getStatus();
                    String complaintId = model.getComplaintId();
                    boolean note = model.isNote();


                    list.add(new Model(imgurl,name,phone,latitude,longitude,userId,status,complaintId,note));
                    myAdapter adapter = new myAdapter(list,getActivity());
                    binding.recview.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }



}