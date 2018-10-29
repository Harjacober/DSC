package com.example.harjacober.dsc.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.harjacober.dsc.DSC;
import com.example.harjacober.dsc.models.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/** The view model object is used to hook the livedata object with
 * the with the activity
 * The ViewModel object survives Activity configuration changes, hence
 *  its LiveData member object will be retained when the user rotates the device.*/
public class DscInstructorViewModel extends ViewModel {
    DatabaseReference reference =
            FirebaseDatabase.getInstance().getReference().child("dsc-futa");
    FirebaseQueryLiveData liveData =
            new FirebaseQueryLiveData(reference);

    /** Trandformations is an utils class which returns a Livedata object
     * given a livedata object and a function*/
    private LiveData<DSC> dscLiveData =
            Transformations.map(liveData, new Deserializer());
    /**Deseializes a Datasnapshot object into a DSC object*/
    private class Deserializer implements Function<DataSnapshot, DSC>{

        @Override
        public DSC apply(DataSnapshot input) {
            return input.getValue(DSC.class);
        }
    }

    /** Not a good practice as data is not full ready to be consumed by the UI
     **/
    @NonNull
    public LiveData<DataSnapshot> getDataSnaphotLiveData(){
        return liveData;
    }

    /** Data is ready to be consumed by the UI,no trace of firebase SDK*/
    @NonNull
    public LiveData<DSC> getDscLiveData(){
        return dscLiveData;
    }

    /**MediatorLiveData is built on top of a map transform,
     *  and allows us to observe changes of other LiveData sources,
     *  deciding what to do with each event*/
    private MediatorLiveData<DSC> dscMediatorLiveData = new MediatorLiveData<>();
    public DscInstructorViewModel() {
        dscMediatorLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           //post value is the safe way to perform update ina thread
                           dscMediatorLiveData.postValue(dataSnapshot.getValue(DSC.class));
                       }
                   }).start();
                }else {
                    //set value can be called on the main thread
                    dscMediatorLiveData.setValue(null);
                }
            }
        });
    }
    @NonNull
    public MediatorLiveData<DSC> getDscMediatorLiveData(){
        return dscMediatorLiveData;
    }
}
