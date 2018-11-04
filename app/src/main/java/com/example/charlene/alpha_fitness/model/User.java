package com.example.charlene.alpha_fitness.model;

public class User {
    private String username;
    private Gender gender;
    private double weight;

    public User(String username, Gender gender, double weight) {
        this.username = username;
        this.gender = gender;
        this.weight = weight;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", gender=" + gender +
                ", weight=" + weight +
                '}';
    }
}

enum Gender{
    FEMALE,
    MALE
}
