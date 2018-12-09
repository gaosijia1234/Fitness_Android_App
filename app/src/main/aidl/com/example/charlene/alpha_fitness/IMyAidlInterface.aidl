// IMyAidlInterface.aidl
package com.example.charlene.alpha_fitness;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
//
//
//    int square(in int value);
//
        int countSteps();
        int countSec();
        int calcAverage();
        int calcMax();
        int calcMin();
        void startCounting();
        int getCurrentWorkoutStepCount();
}
