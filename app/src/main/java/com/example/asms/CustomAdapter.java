package com.example.asms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<Animal> animalList;
    Context context;
    RequestQueue queue;

    public CustomAdapter(Context context, ArrayList<Animal> animalList, RequestQueue queue) {
        this.context = context;
        this.animalList = animalList;
        this.queue =  queue;
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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String animalId = animalList.get(position).getAnimalId();
                String deleteUrl = "https://asms.herokuapp.com/animals/" + animalId;
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, deleteUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (context instanceof AnimalActivity) {
                                    ((AnimalActivity)context).update();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, "That didn't work", Toast.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(stringRequest);
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
