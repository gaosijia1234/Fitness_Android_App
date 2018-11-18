package com.example.charlene.alpha_fitness.model;

public class User {
    private String username;
    private String gender;
    private double weight;

    public User(java.lang.String username, String gender, double weight) {
        this.username = username;
        this.gender = gender;
        this.weight = weight;
    }

    public java.lang.String getUsername() {
        return username;
    }

    public void setUsername(java.lang.String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public java.lang.String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", gender=" + gender +
                ", weight=" + weight +
                '}';
    }
}

