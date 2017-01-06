package Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

import Models.Expert;

/**
 * Created by sails on 12.12.2016.
 */

public class DBHelperExperts extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SystemAnalis";
    public static final int DATABASE_VERSION = 3;

    public static final String TABLE_EXPERT = "experts";
    public static final String TABLE_EXPERT_ID = "ID";
    public static final String TABLE_EXPERT_RATINGS = "Ratings";
    public static final String TABLE_EXPERT_IMPORTANCE = "Importance";

    public static final String LOG_TAG = "dbLogs";

    ArrayList<String> tables;

    public DBHelperExperts(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Log.d(LOG_TAG, "DBHelperExperts constructor worked.");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(LOG_TAG, "class DBHelperExperts, method onCreate is working.");

        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_EXPERT + " (" + TABLE_EXPERT_ID + " INTEGER, " + TABLE_EXPERT_RATINGS + " TEXT, " +
                TABLE_EXPERT_IMPORTANCE + " INTEGER);";
        sqLiteDatabase.execSQL(query);
        Log.d(LOG_TAG, query);

        Log.d(LOG_TAG, query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(LOG_TAG, "Method onUpgrade is working. Current version: " + i + "; New version: " + i1);

        onCreate(sqLiteDatabase);

        if (i1 > i) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPERT + ";");

            onCreate(sqLiteDatabase);

            Log.d(LOG_TAG, "New version > Current version. Upgrading DB.");
        }

    }

    public ArrayList<Expert> getExpertsFromDB() {
        ArrayList<Expert> experts = new ArrayList<>();

        Cursor cursor;
        cursor = getReadableDatabase().rawQuery("SELECT * FROM " + DBHelperExperts.TABLE_EXPERT + ";", new String[]{});

        if (cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(DBHelperExperts.TABLE_EXPERT_ID);
            int ratingsColumn = cursor.getColumnIndex(DBHelperExperts.TABLE_EXPERT_RATINGS);
            int importanceColumn = cursor.getColumnIndex(DBHelperExperts.TABLE_EXPERT_IMPORTANCE);

            do {
                int id = cursor.getInt(idColumn);
                String ratings = cursor.getString(ratingsColumn);
                int importance = cursor.getInt(importanceColumn);

                experts.add(new Expert(id, ratings, importance));
            } while (cursor.moveToNext());
        }

        for (int i = 0; i < experts.size() - 1; i++) {
            boolean swapped = false;
            for(int j = 0; j < experts.size() - i - 1; j++){
                if (experts.get(j).getId() > experts.get(j + 1).getId()) {
                    Expert temp = experts.get(j + 1);
                    experts.set(j + 1, experts.get(j));
                    experts.set(j, temp);
                    swapped = true;
                }
            }
            if(!swapped)
                break;
        }


        return experts;
    }
}