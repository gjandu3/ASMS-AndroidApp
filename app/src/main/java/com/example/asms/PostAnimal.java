package com.example.asms;

import com.google.gson.annotations.SerializedName;

/*PostAnimal class to define the animal object that
the user sends to the api*/
public class PostAnimal {

    @SerializedName("Name")
    private final String animalName;

    @SerializedName("selectedFile")
    private final String animalImage;

    @SerializedName("Breed")
    private final String animalBreed;

    @SerializedName("Age")
    private final String animalAge;

    @SerializedName("IntakeReason")
    private final String animalIntakeReason;

    @SerializedName("Species")
    private final String animalSpecies;

    @SerializedName("Status")
    private final String animalStatus;

    PostAnimal (String animalName, String animalSpecies,String animalBreed,
                String animalAge, String animalStatus, String animalIntakeReason,
                String animalImage) {
        this.animalName = animalName;
        this.animalSpecies = animalSpecies;
        this.animalBreed = animalBreed;
        this.animalAge = animalAge;
        this.animalStatus = animalStatus;
        this.animalIntakeReason = animalIntakeReason;
        this.animalImage = animalImage;
    }

}
