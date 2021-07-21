package com.example.asms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//Custom Adapter class used to display each individual animal
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    //Classwide variables
    ArrayList<Animal> animalList;
    Context context;
    RequestQueue queue;

    //Sets the custom adapter and variables that were passed in
    public CustomAdapter(Context context, ArrayList<Animal> animalList, RequestQueue queue) {
        this.context = context;
        this.animalList = animalList;
        this.queue =  queue;
    }

    //Sets the viewholder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /*Corresponds custom adapter variables with what was sent in
    Along with creating an on click listener for the delete button for each animal */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.name.setText(animalList.get(position).getAnimalName());
        Glide.with(context).load(animalList.get(position).getAnimalImage()).into(holder.photo);
        holder.breed.setText(animalList.get(position).getAnimalBreed());
        holder.age.setText(animalList.get(position).getAnimalAge() + " years old");
        holder.intakeReason.setText(animalList.get(position).getAnimalIntakeReason());

        holder.itemView.setOnClickListener(view -> Toast.makeText(context, animalList.get(position).getAnimalName(), Toast.LENGTH_SHORT).show());

        holder.delete.setOnClickListener(v -> {
            String animalId = animalList.get(position).getAnimalId();
            String deleteUrl = "https://asms.herokuapp.com/animals/" + animalId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, deleteUrl,
                    response -> {
                        if (context instanceof AnimalActivity) {
                            ((AnimalActivity)context).update();
                        }
                    }, error -> Toast.makeText(context, "That didn't work", Toast.LENGTH_SHORT).show());
                    queue.add(stringRequest);
        });
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    // Corresponds variables with the xml layout for each card
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
