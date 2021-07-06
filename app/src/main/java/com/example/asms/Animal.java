
package com.example.asms;

public class Animal {

    public String animalId;
    public String animalName;
    public String animalImage;
    public String animalBreed;
    public String animalAge;
    public String animalIntakeReason;

    Animal (String animalId, String animalName, String animalImage, String animalBreed,
            String animalAge, String animalIntakeReason) {
        this.animalId = animalId;
        this.animalName = animalName;
        this.animalImage = animalImage;
        this.animalBreed = animalBreed;
        this.animalAge = animalAge;
        this.animalIntakeReason = animalIntakeReason;
    }

    public String getAnimalId() { return animalId; }

    public String getAnimalName() {
        return animalName;
    }

    public String getAnimalImage() {
        return animalImage;
    }

    public String getAnimalBreed() {
        return animalBreed;
    }

    public String getAnimalAge() {
        return animalAge;
    }

    public String getAnimalIntakeReason() {
        return animalIntakeReason;
    }

}

