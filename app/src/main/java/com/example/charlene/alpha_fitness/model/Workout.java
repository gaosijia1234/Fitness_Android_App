package com.example.charlene.alpha_fitness.model;

public class Workout {
    private String date;
    private double distance;
    private double calories;
    private double duration;
    private double aveVelocity;
    private double maxVelocity;
    private double minVelocity;

    public Workout(String date, double distance, double calories, double duration, double aveVelocity, double maxVelocity, double minVelocity) {
        this.date = date;
        this.distance = distance;
        this.calories = calories;
        this.duration = duration;
        this.aveVelocity = aveVelocity;
        this.maxVelocity = maxVelocity;
        this.minVelocity = minVelocity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // every 5 minutes' distance
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

    // for every 5 minutes, update the duration
    public double getDuration() {
        double init = 0.0;
        if (duration < 5 * 3600 * 1000){
            // unfinished
        }
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getAveVelocity() {
        return aveVelocity;
    }

    public void setAveVelocity(double aveVelocity) {
        this.aveVelocity = aveVelocity;
    }

    public double getMaxVelocity() {
        double max = getMaxVelocity();
        if (maxVelocity > max){
            max = maxVelocity;
        }
        return max;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public double getMinVelocity() {
        double min = getMinVelocity();
        if (minVelocity < min){
            min = minVelocity;
        }
        return min;
    }

    public void setMinVelocity(double minVelocity) {
        this.minVelocity = minVelocity;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "date=" + date +
                ", distance=" + distance +
                ", calories=" + calories +
                ", duration=" + duration +
                ", aveVelocity=" + aveVelocity +
                ", maxVelocity=" + maxVelocity +
                ", minVelocity=" + minVelocity +
                '}';
    }
}
