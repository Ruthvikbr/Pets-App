/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetsContract;

import static com.example.android.pets.data.PetsContract.PetEntry.COLUMN_PET_BREED;
import static com.example.android.pets.data.PetsContract.PetEntry.COLUMN_PET_GENDER;
import static com.example.android.pets.data.PetsContract.PetEntry.COLUMN_PET_NAME;
import static com.example.android.pets.data.PetsContract.PetEntry.COLUMN_PET_WEIGHT;
import static com.example.android.pets.data.PetsContract.PetEntry.CONTENT_URI;
import static com.example.android.pets.data.PetsContract.PetEntry.TABLE_NAME;
import static com.example.android.pets.data.PetsContract.PetEntry._ID;


public class CatalogActivity extends AppCompatActivity {
    private PetDbHelper mDbHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new PetDbHelper(this);
        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        String[] projection = {_ID,COLUMN_PET_NAME,COLUMN_PET_BREED,COLUMN_PET_GENDER,COLUMN_PET_WEIGHT
        };
       Cursor cursor = getContentResolver().query(CONTENT_URI,projection,null,null,null);
        TextView displayView =  findViewById(R.id.text_view_pet);

        try {

            displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
            displayView.append(PetsContract.PetEntry._ID + " - " +
                    PetsContract.PetEntry.COLUMN_PET_NAME + " - " +
                    PetsContract.PetEntry.COLUMN_PET_BREED + " - " +
                    PetsContract.PetEntry.COLUMN_PET_GENDER + " - " +
                    PetsContract.PetEntry.COLUMN_PET_WEIGHT + "\n");

            int idColumnIndex = cursor.getColumnIndex(PetsContract.PetEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(PetsContract.PetEntry.COLUMN_PET_NAME);
            int weightCoulmnIndex = cursor.getColumnIndex(COLUMN_PET_WEIGHT);
            int breedCoulmnIndex = cursor.getColumnIndex(COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(COLUMN_PET_GENDER);

            while (cursor.moveToNext()) {

                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentWeight = cursor.getInt(weightCoulmnIndex);
                String  currentBreed = cursor.getString(breedCoulmnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);

                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentBreed + " - " +
                        currentGender + " - " +
                        currentWeight));
            }
        } finally {

            cursor.close();
        }
    }
    private void  insertPet(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PetsContract.PetEntry.COLUMN_PET_NAME,"toto");
        values.put(PetsContract.PetEntry.COLUMN_PET_BREED,"Terrier");
        values.put(PetsContract.PetEntry.COLUMN_PET_GENDER, PetsContract.PetEntry.GENDER_MALE);
        values.put(PetsContract.PetEntry.COLUMN_PET_WEIGHT,1);
      Uri insertUri =  getContentResolver().insert(CONTENT_URI,values);
        Log.v("CatalogActivity","pet added" +insertUri );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                    insertPet();
                    displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
