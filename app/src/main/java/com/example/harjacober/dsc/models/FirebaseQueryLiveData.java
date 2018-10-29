package com.example.harjacober.dsc.models;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/** The LiveData will listen to changes to a change in the database query
 * and notify the activity of these changes so iy can update its UI*/
public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {
    private final DatabaseReference reference;
    private final MyValueEventListener listener =
            new MyValueEventListener();

    public FirebaseQueryLiveData(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        reference.removeEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener{

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            /** The observer is notified using the below method  whenever the data
             * from the query given in the constructor changes*/
            setValue(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
    /**
     * All LiveData callbacks to onChanged() run on the main thread, as well as any transformations
     * as the quantity and size of the objects increase the time it will take  the
     * Realtime Database SDK to  deserialize a DataSnapshot to a JavaBean type object,
     * might exceed the budget for a unit of work on the main thread*/
}
