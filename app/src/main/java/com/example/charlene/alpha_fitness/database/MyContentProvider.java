package com.example.charlene.alpha_fitness.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

public class MyContentProvider extends ContentProvider {
    private final static String TAG = MyContentProvider.class.getSimpleName();


    static final String PROVIDER = "com.charlene.myprovider";
    static final String URL = "content://" + PROVIDER + "/devices";
    static final Uri URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String NAME = "name";
    static final String BATTERY = "battery";

    Context mContext;

    private static HashMap<String, String> DEVICES_PROJECTION_MAP;

    static final int DEVICES = 1;
    static final int DEVICE_ID = 2;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "devices", DEVICES);
        uriMatcher.addURI(PROVIDER, "devices/#", DEVICE_ID);
    }

    private DatabaseHelper db;
    static final String DATABASE_NAME = "myprovider";
    static final String DEVICES_TABLE_NAME = "devices";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE = " CREATE TABLE " + DEVICES_TABLE_NAME +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + " name TEXT NOT NULL< " + "battery TEXT NOT NULL);";

    @Override
    public boolean onCreate() {
        return false;
    }

    private static class DB extends SQLiteOpenHelper {

        public DB(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DEVICES_TABLE_NAME);
            onCreate(db);
        }
    }


    private void notifyChange(Uri uri) {
        ContentResolver resolver = mContext.getContentResolver();
        if (resolver != null) resolver.notifyChange(uri, null);
    }

    private int getMatchedID(Uri uri) {
        int matchedID = uriMatcher.match(uri);
        if (!(matchedID == DEVICES || matchedID == DEVICE_ID))
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        return matchedID;

    }

    private String getIdString(Uri uri) {
        return (_ID + " = " + uri.getPathSegments().get(1));

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(DEVICES_TABLE_NAME);

        if (getMatchedID(uri) == DEVICES) {
            sqLiteQueryBuilder.appendWhere(getIdString(uri));
        } else {
            sqLiteQueryBuilder.appendWhere( getIdString(uri) );
        }

        if (sortOrder == null || sortOrder == "") {

            sortOrder = NAME;
        }

        Cursor c = sqLiteQueryBuilder.query(null, projection, selection, selectionArgs, null, null, sortOrder );
        return c;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        return 0;
    }



}
