package be.milants.triviatrainer.service.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import be.milants.triviatrainer.data.TriviaData;
import be.milants.triviatrainer.helper.DatabaseHelper;
import be.milants.triviatrainer.service.TriviaDatabaseService;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class DefaultTriviaDatabaseService implements TriviaDatabaseService {

    private static final String DATABASE = "TRIVIA";
    private static final String TRIVIA_ENTRIES_TABLE = "TRIVIA_ENTRIES";
    private static final String TRIVIA_ENTRIES_SUBJECT = "SUBJECT";
    private static final String TRIVIA_ENTRIES_MAIN_CATEGORY = "MAIN_CATEGORY";
    private static final String TRIVIA_ENTRIES_SECONDARY_CATEGORY = "SECONDAREY_CATEGORY";
    private static final String TRIVIA_ENTRIES_IMAGE = "IMAGE";
    private static final String TRIVIA_ENTRIES_TAGS = "TAGS";
    private static final String MAIN_CATEGORY_TABLE = "MAIN_CATEGORIES";
    private static final String SECONDARY_CATEGORY_TABLE = "SECONDARY_CATEGORIES";
    private static final String MAIN_CATEGORY_TABLE_NAME = "NAME";
    private static final String SECONDARY_CATEGORY_TABLE_NAME = "NAME";
    private static final String SECONDARY_CATEGORY_TABLE_MAIN_CATEGORY = "MAIN_CATEGORY";
    private DatabaseHelper helper;

    public DefaultTriviaDatabaseService() {
    }

    public void deleteEntryFromDataBase(Context context, String subject) {
        SQLiteDatabase triviaDatabase = getDatabase(context);

        triviaDatabase.delete(TRIVIA_ENTRIES_TABLE, TRIVIA_ENTRIES_SUBJECT + "=?", new String[]{subject});
        triviaDatabase.close();
    }

    public List<TriviaData> getEntriesFromDataBase(Context context) {
        SQLiteDatabase triviaDatabase = getDatabase(context);

        try {
            final String getEntriesQuery = "select * from " + TRIVIA_ENTRIES_TABLE;
            Cursor cursor = triviaDatabase.rawQuery(getEntriesQuery, null);

            List<TriviaData> triviaDataList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String subject = cursor.getString(cursor.getColumnIndex(TRIVIA_ENTRIES_SUBJECT));
                String mainCategory = cursor.getString(cursor.getColumnIndex(TRIVIA_ENTRIES_MAIN_CATEGORY));
                String secondaryCategory = cursor.getString(cursor.getColumnIndex(TRIVIA_ENTRIES_SECONDARY_CATEGORY));
                String tags = cursor.getString(cursor.getColumnIndex(TRIVIA_ENTRIES_TAGS));
                byte[] image = cursor.getBlob(cursor.getColumnIndex(TRIVIA_ENTRIES_IMAGE));

                TriviaData triviaData = new TriviaData();
                triviaData.setSubject(subject);
                triviaData.setMainCategory(mainCategory);
                triviaData.setSecondaryCategory(secondaryCategory);
                triviaData.setTags(Arrays.asList(tags.split("\\|")));

                triviaDataList.add(triviaData);
            }
            cursor.close();
            triviaDatabase.close();
            return triviaDataList;
        } catch (Exception e) {
            Log.v("err retrieving entries", e.getMessage());
            return null;
        }
    }

    public void addEntryToDatabase(Context context, TriviaData triviaData) {
        SQLiteDatabase triviaDatabase = getDatabase(context);

        try {
            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS " + TRIVIA_ENTRIES_TABLE + " ("
                    + TRIVIA_ENTRIES_SUBJECT + " TEXT primary key, "
                    + TRIVIA_ENTRIES_MAIN_CATEGORY + " TEXT, "
                    + TRIVIA_ENTRIES_SECONDARY_CATEGORY + " TEXT, "
                    + TRIVIA_ENTRIES_TAGS + " TEXT, "
                    + TRIVIA_ENTRIES_IMAGE + " BLOB )";
            triviaDatabase.execSQL(CREATE_TABLE_CONTAIN);
            triviaDatabase.execSQL("INSERT or replace INTO " + TRIVIA_ENTRIES_TABLE + " (" + TRIVIA_ENTRIES_SUBJECT + "," + TRIVIA_ENTRIES_MAIN_CATEGORY + "," +
                    TRIVIA_ENTRIES_SECONDARY_CATEGORY + "," + TRIVIA_ENTRIES_TAGS + "," + TRIVIA_ENTRIES_IMAGE + ") " +
                    "VALUES('" + triviaData.getSubject() + "','" + triviaData.getMainCategory() + "','" + triviaData.getSecondaryCategory() + "','" +
                    TextUtils.join("|", triviaData.getTags()) + "','" + null + "')");
        } catch (Exception e) {
            Log.v("error occured", e.getMessage());
        }
    }

    public void addMainCategoriesToDatabase(Context context) {
        SQLiteDatabase triviaDatabase = getDatabase(context);

        try {
            final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS " + MAIN_CATEGORY_TABLE + " ("
                    + MAIN_CATEGORY_TABLE_NAME + " TEXT primary key)";
            triviaDatabase.execSQL(CREATE_TABLE_CONTAIN);
            triviaDatabase.execSQL("INSERT or replace INTO " + MAIN_CATEGORY_TABLE + " (" + MAIN_CATEGORY_TABLE_NAME + ") VALUES('Geography')");
            triviaDatabase.execSQL("INSERT or replace INTO " + MAIN_CATEGORY_TABLE + " (" + MAIN_CATEGORY_TABLE_NAME + ") VALUES('Woorden')");
            triviaDatabase.execSQL("INSERT or replace INTO " + MAIN_CATEGORY_TABLE + " (" + MAIN_CATEGORY_TABLE_NAME + ") VALUES('Bekende personen')");
            triviaDatabase.execSQL("INSERT or replace INTO " + MAIN_CATEGORY_TABLE + " (" + MAIN_CATEGORY_TABLE_NAME + ") VALUES('Gebeurtenissen')");

            final String CREATE_SECONDARY_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS " + SECONDARY_CATEGORY_TABLE + " ("
                    + SECONDARY_CATEGORY_TABLE_NAME + " TEXT primary key,"
                    + SECONDARY_CATEGORY_TABLE_MAIN_CATEGORY + " TEXT)";
            triviaDatabase.execSQL(CREATE_SECONDARY_TABLE_CONTAIN);
            triviaDatabase.execSQL("INSERT or replace INTO " + SECONDARY_CATEGORY_TABLE + " (" + SECONDARY_CATEGORY_TABLE_NAME + "," + SECONDARY_CATEGORY_TABLE_MAIN_CATEGORY + ") VALUES('Hoofdsteden', 'Geography')");
            triviaDatabase.execSQL("INSERT or replace INTO " + SECONDARY_CATEGORY_TABLE + " (" + SECONDARY_CATEGORY_TABLE_NAME + "," + SECONDARY_CATEGORY_TABLE_MAIN_CATEGORY + ") VALUES('Landen', 'Geography')");
            triviaDatabase.execSQL("INSERT or replace INTO " + SECONDARY_CATEGORY_TABLE + " (" + SECONDARY_CATEGORY_TABLE_NAME + "," + SECONDARY_CATEGORY_TABLE_MAIN_CATEGORY + ") VALUES('Historisch figuur', 'Bekende personen')");
            triviaDatabase.execSQL("INSERT or replace INTO " + SECONDARY_CATEGORY_TABLE + " (" + SECONDARY_CATEGORY_TABLE_NAME + "," + SECONDARY_CATEGORY_TABLE_MAIN_CATEGORY + ") VALUES('Actueel figuur', 'Bekende personen')");
        } catch (Exception e) {
            // do something
        }
    }

    public List<String> getMainCategories(Context context) {
        SQLiteDatabase triviaDatabase = getDatabase(context);

        Cursor cursor = triviaDatabase.rawQuery("select * from " + MAIN_CATEGORY_TABLE, null);
        List<String> result = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(MAIN_CATEGORY_TABLE_NAME));
            result.add(name);
        }
        cursor.close();
        triviaDatabase.close();
        Collections.sort(result);
        return result;
    }

    public List<String> getSecondaryCategoriesForMainCategory(String mainCategory, Context context) {
        SQLiteDatabase triviaDatabase = getDatabase(context);

        Cursor cursor = triviaDatabase.rawQuery("select * from " + SECONDARY_CATEGORY_TABLE + " WHERE " + SECONDARY_CATEGORY_TABLE_MAIN_CATEGORY + " = '" + mainCategory + "'", null);
        List<String> result = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(SECONDARY_CATEGORY_TABLE_NAME));
            result.add(name);
        }
        cursor.close();
        triviaDatabase.close();
        Collections.sort(result);
        return result;
    }

    private SQLiteDatabase getDatabase(Context context) {
        //initDataBase(context);
        helper = new DatabaseHelper(context);
        try {
            helper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            helper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        return helper.getWritableDatabase();
    }
}
