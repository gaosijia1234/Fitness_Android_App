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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.Duration;


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
//        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
        String DROP_WORKOUT_TABLE = "DROP TABLE IF EXISTS " + TABLE_WORKOUT;

        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER +
                "(" +
                ATTRIBUTE_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ATTRIBUTE_USER_NAME + " VARCHAR(255) NOT NULL, " +
                ATTRIBUTE_USER_GENDER + " VARCHAR(255) NOT NULL, " +
                ATTRIBUTE_USER_WEIGHT + " DOUBLE NOT NULL" +
                ")";
        String CREATE_WORKOUT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_WORKOUT +
                "(" +
                ATTRIBUTE_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ATTRIBUTE_WORKOUT_DATE + " VARCHAR(255) NOT NULL, " +
                ATTRIBUTE_WORKOUT_DISTANCE + " DOUBLE NOT NULL, " +
                ATTRIBUTE_WORKOUT_CALORIES + " DOUBLE NOT NULL, " +
                ATTRIBUTE_WORKOUT_DURATION + " DOUBLE NOT NULL, " +
                ATTRIBUTE_WORKOUT_AVE_VELOCITY + " DOUBLE NOT NULL, " +
                ATTRIBUTE_WORKOUT_MAX_VELOCITY + " DOUBLE NOT NULL, " +
                ATTRIBUTE_WORKOUT_MIN_VELOCITY + " DOUBLE NOT NULL" +
                ")";

//        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_WORKOUT_TABLE);
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
        // per tuple is one time
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try{
            ContentValues values = new ContentValues();
            values.put(ATTRIBUTE_WORKOUT_DATE, workout.getDate());
            values.put(ATTRIBUTE_WORKOUT_DISTANCE, workout.getDistance());
            values.put(ATTRIBUTE_WORKOUT_CALORIES, workout.getCalories());
            values.put(ATTRIBUTE_WORKOUT_DURATION, workout.getDuration());
            values.put(ATTRIBUTE_WORKOUT_AVE_VELOCITY, workout.getAveVelocity());
            values.put(ATTRIBUTE_WORKOUT_MIN_VELOCITY, workout.getMinVelocity());
            values.put(ATTRIBUTE_WORKOUT_MAX_VELOCITY, workout.getMaxVelocity());

            db.insertOrThrow(TABLE_WORKOUT, null, values);
            db.setTransactionSuccessful();

        }catch (Exception e){
            Log.d(TAG,"Error while trying to add a user in WORKOUT_TABLE in database");
        }finally {
            db.endTransaction();
        }

    }

    // always get current week's average
    public String[] getWorkoutAverage(){

        SQLiteDatabase db = getReadableDatabase();
        List<String> dates = new ArrayList<>(); // ignore the case of the week across the year

        // data should come from db cursor to get all the info

        // 1. find all the workouts within that week of the current week.

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int currentWeekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

        String date1 = "";

        double distance = 0.0;
        double duration = 0.0;
        double calories = 0.0;

        double totalDistance = 0.0;
        double totalDuration = 0.0;
        double totalCalories = 0.0;

        // cursor the whole table find the date within that week
        String DATE_QUERY = "SELECT * FROM " + TABLE_WORKOUT;
        Cursor c = db.rawQuery(DATE_QUERY, null);

        try{
            c.moveToFirst();
            while (!c.isAfterLast()) {
                // 1, get the date of the attribute
                date1 = c.getString(c.getColumnIndex(ATTRIBUTE_WORKOUT_DATE));
                distance = c.getDouble(c.getColumnIndex(ATTRIBUTE_WORKOUT_DISTANCE));
                duration = c.getDouble(c.getColumnIndex(ATTRIBUTE_WORKOUT_DURATION));
                calories = c.getDouble(c.getColumnIndex(ATTRIBUTE_WORKOUT_CALORIES));
                // 2. cast the attribute date into Date
                Date date2 = new Date(date1);
                cal.setTime(date2);
                int weekOfYearOfAttri = cal.get(Calendar.WEEK_OF_YEAR);

                if (weekOfYearOfAttri == currentWeekOfYear){
                    dates.add(date1);
                    // 2. find the total distance: select distance from table workout
                    totalDistance += distance;
                    // 3. find the total time
                    totalDuration += duration;
                    // 4. find the total cal
                    totalCalories += calories;
                }

                c.moveToNext();
            }
        }catch (Exception e){
            Log.d(TAG, "Error while trying to get existing grocery item count in grocery table from database");
        }finally {
            if( c != null && !c.isClosed()){
                c.close();
            }
        }

        double totalTimes = dates.size();
        double aveDistance = totalDistance / totalTimes;
        double aveDuration = totalDuration / totalTimes;
        double aveCalories = totalCalories / totalTimes;

        String aveDurationToString = castingSeconds(aveDuration);

        // put them in an array
        return new String[]{aveDistance + " km", aveDurationToString
                + totalTimes + " times", aveCalories + " Cal"};
    }

    private double getAllTimeDistance(){
        SQLiteDatabase db = getReadableDatabase();

        double allTimeDistance = 0.0;
        // select count(*) to get all distance
        String DISTANCE_QUERY = "SELECT SUM(distance) FROM " + TABLE_WORKOUT;
        Cursor c1 = db.rawQuery(DISTANCE_QUERY, null);

        try{
            c1.moveToFirst();
            while (!c1.isAfterLast()) {
                allTimeDistance = c1.getDouble(0);
                c1.moveToNext();
            }
        }catch (Exception e){
            Log.d(TAG, "Error while trying to get recipe ingredients in recipe ingredient table from database");
        } finally {
            if( c1 != null && !c1.isClosed()){
                c1.close();
            }
        }
        return allTimeDistance;
    }

    private int getAllTimeTimes(){
        SQLiteDatabase db = getReadableDatabase();
        int allTimeTimes = 0;

        // select all tuples to get Workouts times
        String TIMES_QUERY = "SELECT COUNT(workout_id) FROM " + TABLE_WORKOUT;
        Cursor c3 = db.rawQuery(TIMES_QUERY, null);

        try{
            c3.moveToFirst();
            while (!c3.isAfterLast()) {
                allTimeTimes = c3.getInt(0);
                c3.moveToNext();
            }
        }catch (Exception e){
            Log.d(TAG, "Error while trying to get recipe ingredients in recipe ingredient table from database");
        } finally {
            if( c3 != null && !c3.isClosed()){
                c3.close();
            }
        }

        return allTimeTimes;

    }

    private int getAllTimeDuration(){
        SQLiteDatabase db = getReadableDatabase();
        int allTimeDuration = 0;

        // select count(*) to get all time
        String Duration_QUERY = "SELECT SUM(duration) FROM " + TABLE_WORKOUT;
        Cursor c2 = db.rawQuery(Duration_QUERY, null);

        try{
            c2.moveToFirst();
            while (!c2.isAfterLast()) {
                allTimeDuration = c2.getInt(0);
                c2.moveToNext();
            }
        }catch (Exception e){
            Log.d(TAG, "Error while trying to get recipe ingredients in recipe ingredient table from database");
        } finally {
            if( c2 != null && !c2.isClosed()){
                c2.close();
            }
        }

        return allTimeDuration;
    }

    private double getAllTimeCalories(){
        SQLiteDatabase db = getReadableDatabase();

        double allTimeCalories = 0.0;

        // select count(*) to get all calories
        String CALORIES_QUERY = "SELECT SUM(calories) FROM " + TABLE_WORKOUT;
        Cursor c4 = db.rawQuery(CALORIES_QUERY, null);

        try{
            c4.moveToFirst();
            while (!c4.isAfterLast()) {
                allTimeCalories = c4.getInt(0);
                c4.moveToNext();
            }
        }catch (Exception e){
            Log.d(TAG, "Error while trying to get recipe ingredients in recipe ingredient table from database");
        } finally {
            if( c4 != null && !c4.isClosed()){
                c4.close();
            }
        }
        return allTimeCalories;
    }

    private String castingSeconds(double duration){

        int day = (int) (duration / 86400);
        int hour = (int) ((duration - day) / 3600);
        int minute =  (int) ((duration - day - hour) / 60);
        int second =  (int) (duration - day - hour - minute);

        int[] theTime = new int[]{day, hour, minute, second};
        return theTime[0] + " day " + theTime[1] + " hr " + theTime[2] + " min " + theTime[3] + " sec ";
    }

    public String[] getWorkoutAllTime(){

        double allTimeDistance = getAllTimeDistance();
        int allTimeTimes =  getAllTimeTimes();
        int allTimeDuration = getAllTimeDuration();
        double allTimeCalories = getAllTimeCalories();

        String theTime = castingSeconds(allTimeDuration);

        return new String[]{allTimeDistance + "km", theTime, allTimeTimes + " times", allTimeCalories + " Cal"};
    }

}