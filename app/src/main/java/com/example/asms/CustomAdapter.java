package com.example.asms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<Animal> animalList;
    Context context;

    public CustomAdapter(Context context, ArrayList<Animal> animalList) {
        this.context = context;
        this.animalList = animalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(animalList.get(position).getAnimalName());
        Glide.with(context).load(animalList.get(position).getAnimalImage()).into(holder.photo);
        holder.breed.setText(animalList.get(position).getAnimalBreed());
        holder.age.setText(animalList.get(position).getAnimalAge() + " years old");
        holder.intakeReason.setText(animalList.get(position).getAnimalIntakeReason());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, animalList.get(position).getAnimalName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button delete;
        TextView name, breed, age, intakeReason;
        ImageView photo;

        public MyViewHolder(View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            name = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photo);
            breed = itemView.findViewById(R.id.breed);
            age = itemView.findViewById(R.id.age);
            intakeReason = itemView.findViewById(R.id.intakeReason);
        }
    }

}
