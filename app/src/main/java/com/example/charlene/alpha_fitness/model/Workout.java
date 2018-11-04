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
    private double maxVelocity;
    private double minVelocity;

    public Workout(Date date, double distance, double calories, Duration duration, double aveVelocity, double maxVelocity, double minVelocity) {
        this.date = date;
        this.distance = distance;
        this.calories = calories;
        this.duration = duration;
        this.aveVelocity = aveVelocity;
        this.maxVelocity = maxVelocity;
        this.minVelocity = minVelocity;
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

    public double getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public double getMinVelocity() {
        return minVelocity;
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
