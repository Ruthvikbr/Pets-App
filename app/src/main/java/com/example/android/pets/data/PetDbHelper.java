package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.pets.data.PetsContract.PetEntry.COLUMN_PET_BREED;
import static com.example.android.pets.data.PetsContract.PetEntry.COLUMN_PET_GENDER;
import static com.example.android.pets.data.PetsContract.PetEntry.COLUMN_PET_NAME;
import static com.example.android.pets.data.PetsContract.PetEntry.COLUMN_PET_WEIGHT;
import static com.example.android.pets.data.PetsContract.PetEntry.TABLE_NAME;
import static com.example.android.pets.data.PetsContract.PetEntry._ID;

public class PetDbHelper extends SQLiteOpenHelper {
    public final static int DATABASE_VERSION=1;
    public final static String DATABASE_NAME = "shelter.db";

    @Override
    public void onCreate(SQLiteDatabase db) {
           String SQL_CREATE_PETS_TABLE = " CREATE TABLE " + TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "+COLUMN_PET_NAME + " TEXT NOT NULL, "+COLUMN_PET_BREED + " TEXT ," +COLUMN_PET_GENDER +" INTEGER NOT NULL ," + COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";
            db.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
    public PetDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }
}
