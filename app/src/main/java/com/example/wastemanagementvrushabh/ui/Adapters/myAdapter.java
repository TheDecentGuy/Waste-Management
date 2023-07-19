package com.example.wastemanagementvrushabh.ui.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wastemanagementvrushabh.Model;
import com.example.wastemanagementvrushabh.R;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myviewholder> {
   ArrayList<Model> list ;
   Context context;

    public myAdapter(ArrayList<Model> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.singlerow,parent,false);

        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        final Model current = list.get(position);
        Model model = list.get(position);
        Glide.with(holder.img.getContext()).load(model.getImageUri()).into(holder.img);
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());
        holder.latitude.setText(model.getLatitude());
        holder.longitude.setText(model.getLongitude());
        holder.status.setText(model.getStatus());

    }

    @Override
    public int getItemCount() {
       return list.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name, phone, latitude,longitude,status;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.sImg);
            name= itemView.findViewById(R.id.sName);
            phone= itemView.findViewById(R.id.sPhone);
            latitude = itemView.findViewById(R.id.sLatitude);
            longitude = itemView.findViewById(R.id.sLongitude);
            status = itemView.findViewById(R.id.tvStatus);

        }
    }
}
