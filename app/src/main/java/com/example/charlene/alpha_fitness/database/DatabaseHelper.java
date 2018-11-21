package com.example.charlene.alpha_fitness.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.charlene.alpha_fitness.model.User;
import com.example.charlene.alpha_fitness.model.Workout;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper{

    // DATABASE
    private static final String DATABASE_NAME = "Alpha_Fitness";
    private static final int DATABASE_VERSION = 1;

    // TABLE
    private static final String TABLE_USER = "user";
    private static final String TABLE_WORKOUT = "workout";

    // USER TABLE
    private static final String ATTRIBUTE_USER_ID = "user_id";
    private static final String ATTRIBUTE_USER_NAME = "username";
    private static final String ATTRIBUTE_USER_GENDER = "gender";
    private static final String ATTRIBUTE_USER_WEIGHT = "weight";

    // WORKOUT TABLE
    private static final String ATTRIBUTE_WORKOUT_ID = "workout_id";
    private static final String ATTRIBUTE_WORKOUT_DATE = "date";
    private static final String ATTRIBUTE_WORKOUT_DISTANCE = "distance";
    private static final String ATTRIBUTE_WORKOUT_CALORIES = "calories";
    private static final String ATTRIBUTE_WORKOUT_DURATION = "duration";
    private static final String ATTRIBUTE_WORKOUT_AVE_VELOCITY = "ave_velocity";
    private static final String ATTRIBUTE_WORKOUT_MAX_VELOCITY = "max_velocity";
    private static final String ATTRIBUTE_WORKOUT_MIN_VELOCITY = "min_velocity";

    private static final String TAG = DatabaseHelper.class.getName();
    private static DatabaseHelper sInstance;

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Get an instance of database.
     * @param context the activity itself
     * @return an instance of database
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Configure database settings for things like foreign key support, write-ahead logging, etc.
     * Called when the database connection is being configured.
     * @param db the database
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public void onCreate(SQLiteDatabase db){
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER +
                "(" +
                ATTRIBUTE_USER_ID + " INT PRIMARY KEY AUTOINCREMENT, " +
                ATTRIBUTE_USER_NAME + " VARCHAR(255) NOT NULL, " +
                ATTRIBUTE_USER_GENDER + " VARCHAR(255) NOT NULL, " +
                ATTRIBUTE_USER_WEIGHT + " DOUBLE NOT NULL" +
                ")";
        String CREATE_WORKOUT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_WORKOUT +
                "(" +
                ATTRIBUTE_WORKOUT_ID + " INT PRIMARY KEY AUTOINCREMENT, " +
                ATTRIBUTE_WORKOUT_DATE + " VARCHAR(255) NOT NULL, " +
                ATTRIBUTE_WORKOUT_DISTANCE + " DOUBLE NOT NULL, " +
                ATTRIBUTE_WORKOUT_CALORIES + " DOUBLE NOT NULL, " +
                ATTRIBUTE_WORKOUT_DURATION + " DOUBLE NOT NULL, " +
                ATTRIBUTE_WORKOUT_AVE_VELOCITY + " DOUBLE NOT NULL, " +
                ATTRIBUTE_WORKOUT_MAX_VELOCITY + " DOUBLE NOT NULL, " +
                ATTRIBUTE_WORKOUT_MIN_VELOCITY + " DOUBLE NOT NULL" +
                ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_WORKOUT_TABLE);
    }

    /**
     * Upgrade database from the old version to a new version.
     * @param db the database
     * @param oldVersion the old version number
     * @param newVersion the new version number
     */
    @Override
    //not used
    public void onUpgrade(SQLiteDatabase db , int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);
            onCreate(db);
        }
    }

    /*--------------------------------Table User---------------------------------------*/

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(ATTRIBUTE_USER_NAME, user.getUsername());
            values.put(ATTRIBUTE_USER_GENDER, user.getGender());
            values.put(ATTRIBUTE_USER_WEIGHT, user.getWeight());

            db.insertOrThrow(TABLE_USER, null, values);
            db.setTransactionSuccessful();

        }catch (Exception e){
            Log.d(TAG,"Error while trying to add a user in USER_TABLE in database");
        }finally {
            db.endTransaction();
        }
    }

    private void updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try{
            ContentValues values = new ContentValues();
            values.put(ATTRIBUTE_USER_NAME, user.getUsername());
            values.put(ATTRIBUTE_USER_GENDER, user.getGender());
            values.put(ATTRIBUTE_USER_WEIGHT, user.getWeight());

            db.update(TABLE_USER, values,
                    ATTRIBUTE_USER_NAME + " = ? AND "
                    + ATTRIBUTE_USER_GENDER + " = ? AND "
                    + ATTRIBUTE_USER_WEIGHT + " = ?",
                    new String[]{user.getUsername(), user.getGender(), Double.toString(user.getWeight())});
            db.setTransactionSuccessful();

        }catch (Exception e){
            Log.d(TAG, "Error while trying to update username in user table in database");
        }finally {
            db.endTransaction();
        }
    }

    private void deleteUser(String userName){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try{
            db.delete(TABLE_USER, ATTRIBUTE_USER_NAME + " = ?",
                    new String[]{userName});
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error while trying to delete a user from user table by userName in database");
        }finally {
            db.endTransaction();
        }
    }

    /*--------------------------------Table Workout---------------------------------------*/
// final call after every workout is done, only being called once each time
    public void addWorkout(Workout workout) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try{
            ContentValues values = new ContentValues();
            values.put(ATTRIBUTE_WORKOUT_DATE, workout.getDate());
            values.put(ATTRIBUTE_WORKOUT_DISTANCE, workout.getDistance());
            values.put(ATTRIBUTE_WORKOUT_CALORIES, workout.getCalories());
            values.put(ATTRIBUTE_WORKOUT_DURATION, workout.getDuration());
            values.put(ATTRIBUTE_WORKOUT_AVE_VELOCITY, workout.getAveVelocity());
            values.put(ATTRIBUTE_WORKOUT_MAX_VELOCITY, workout.getMaxVelocity());
            values.put(ATTRIBUTE_WORKOUT_MIN_VELOCITY, workout.getMinVelocity());

            db.insertOrThrow(TABLE_WORKOUT, null, values);
            db.setTransactionSuccessful();

        }catch (Exception e){
            Log.d(TAG,"Error while trying to add a user in WORKOUT_TABLE in database");
        }finally {
            db.endTransaction();
        }

    }

    public String[] getWorkoutAverage(String date, int weekday){
        // data should come from db cursor to get all the info

        // 1. according to the date, find all the workouts within that week.

        // 2. find the total distance: select distance from table workout
        // ave distance = total distance / times

        // 3. find the total time
        // ave

        // 4. find the total cal
        // ave

        // put them in an array
        return new String[]{"2.4km", "1h34min","2 times", "1,000 Cal"};
    }

    public String[] getWorkoutAllTime(){
        return new String[]{"1548.4km", "1day23hr","112 times", "410,000 Cal"};
    }

}



















