package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import static com.example.android.pets.data.PetsContract.PetEntry.GENDER_FEMALE;
import static com.example.android.pets.data.PetsContract.PetEntry.GENDER_MALE;
import static com.example.android.pets.data.PetsContract.PetEntry.GENDER_UNKNOWN;
import static com.example.android.pets.data.PetsContract.PetEntry.TABLE_NAME;


public class PetProvider extends ContentProvider {
    private PetDbHelper mDbHelper;
    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int PETS = 100;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int PET_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher
        sUriMatcher.addURI(PetsContract.CONTENT_AUTHORITY, PetsContract.PATH_PETS, PETS);
        sUriMatcher.addURI(PetsContract.CONTENT_AUTHORITY, PetsContract.PATH_PETS + "/#", PET_ID);

    }

    public static final String LOG_TAG = PetProvider.class.getSimpleName();


    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                // TODO: Perform database query on pets table
                cursor = database.query(TABLE_NAME, projection, null, null,
                        null, null, null);
                break;
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PetsContract.PetEntry._ID + "=?";

                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues values) {
        long id = 0;
        String name = values.getAsString(PetsContract.PetEntry.COLUMN_PET_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        Integer gender = values.getAsInteger(PetsContract.PetEntry.COLUMN_PET_GENDER);

        if (gender == null || !PetsContract.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet requires valid gender");
        }
        Integer weight = values.getAsInteger(PetsContract.PetEntry.COLUMN_PET_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // TODO: Insert a new pet into the pets database table with the given ContentValues
        id = database.insert(TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;

        }
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PetsContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(PetsContract.PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetsContract.PetEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }


        if (values.containsKey(PetsContract.PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetsContract.PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetsContract.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }


        if (values.containsKey(PetsContract.PetEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = values.getAsInteger(PetsContract.PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

            if (values.size() == 0) {
                return 0;
            }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        return database.update(PetsContract.PetEntry.TABLE_NAME, values, selection, selectionArgs);
        }



        @Override
        public int delete (Uri uri, String selection, String[]selectionArgs){
            SQLiteDatabase database = mDbHelper.getWritableDatabase();

            final int match = sUriMatcher.match(uri);
            switch (match) {
                case PETS:
                    return database.delete(PetsContract.PetEntry.TABLE_NAME, selection, selectionArgs);
                case PET_ID:
                    selection = PetsContract.PetEntry._ID + "=?";
                    selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                    return database.delete(PetsContract.PetEntry.TABLE_NAME, selection, selectionArgs);
                default:
                    throw new IllegalArgumentException("Deletion is not supported for " + uri);
            }
        }

        @Override
        public String getType (Uri uri){
            final int match = sUriMatcher.match(uri);
            switch (match) {
                case PETS:
                    return PetsContract.PetEntry.CONTENT_LIST_TYPE;
                case PET_ID:
                    return PetsContract.PetEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
            }
        }
    }

