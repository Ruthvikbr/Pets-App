package com.example.android.pets.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.R;

import static com.example.android.pets.data.PetsContract.PetEntry.COLUMN_PET_BREED;
import static com.example.android.pets.data.PetsContract.PetEntry.COLUMN_PET_NAME;

public class PetCursorAdapter extends CursorAdapter {

    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTV = view.findViewById(R.id.name);
        TextView summaryTV = view.findViewById(R.id.summary);
        int nameColumnIndex = cursor.getColumnIndex(COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(COLUMN_PET_BREED);

        String petName = cursor.getString(nameColumnIndex);
        String petBreed = cursor.getString(breedColumnIndex);
        nameTV.setText(petName);
        summaryTV.setText(petBreed);

    }
}
