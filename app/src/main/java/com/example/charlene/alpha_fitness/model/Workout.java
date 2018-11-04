package com.example.charlene.alpha_fitness.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

public class Workout {
    private Date date;
    private double distance;
    private double calories;
    private Duration duration;
    private double aveVelocity;
    private ArrayList<Double> velocityRecords;

    public Workout(Date date, double distance, double calories, Duration duration, double aveVelocity, ArrayList<Double> velocityRecords) {
        this.date = date;
        this.distance = distance;
        this.calories = calories;
        this.duration = duration;
        this.aveVelocity = aveVelocity;
        this.velocityRecords = velocityRecords;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public double getAveVelocity() {
        return aveVelocity;
    }

    public void setAveVelocity(double aveVelocity) {
        this.aveVelocity = aveVelocity;
    }

    public ArrayList<Double> getVelocityRecords() {
        return velocityRecords;
    }

    public void setVelocityRecords(ArrayList<Double> velocityRecords) {
        this.velocityRecords = velocityRecords;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "date=" + date +
                ", distance=" + distance +
                ", calories=" + calories +
                ", duration=" + duration +
                ", aveVelocity=" + aveVelocity +
                ", velocityRecords=" + velocityRecords +
                '}';
    }


}
