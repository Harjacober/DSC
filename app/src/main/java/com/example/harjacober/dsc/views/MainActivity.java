package com.example.harjacober.dsc.views;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.harjacober.dsc.DSC;
import com.example.harjacober.dsc.R;
import com.example.harjacober.dsc.viewmodels.DscInstructorViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TextView lead;
    private TextView mobileInstructor;
    private TextView webInstructor;
    private TextView mlInstructor;
    private TextView pDesignInstructor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lead = findViewById(R.id.team_lead);
        mobileInstructor = findViewById(R.id.mobile);
        webInstructor = findViewById(R.id.web);
        mlInstructor = findViewById(R.id.data_scioence);
        pDesignInstructor = findViewById(R.id.product_design);

//        queryDataDirectly();
//        getDataFromViewModel();
        getDataFromViewModelUsingMediatorLiveData();


    }

    /** here the ui has no idea where the data is coming from,,
     * whether it is from a room databse or firebase database or cloud firestore*/
    private void getDataFromViewModel() {
        DscInstructorViewModel viewModel =
                ViewModelProviders.of(this).get(DscInstructorViewModel.class);
        LiveData<DSC> dscLiveData = viewModel.getDscLiveData();
        dscLiveData.observe(this, new Observer<DSC>() {
            @Override
            public void onChanged(@Nullable DSC dsc) {
                if (dsc != null) {
                    lead.setText(dsc.getLeadInstructor());
                    mobileInstructor.setText(dsc.getMobileInstructor());
                    webInstructor.setText(dsc.getWebInstructor());
                    mlInstructor.setText(dsc.getMlInstructor());
                    pDesignInstructor.setText(dsc.getpDesignInstructor());
                }
            }
        });
    }

    /** Using the mediator livedata to convert the DataSnapshot object
     * to a DSC object off the main thread...this is a good practice */
    private void getDataFromViewModelUsingMediatorLiveData(){
        DscInstructorViewModel viewModel =
                ViewModelProviders.of(this).get(DscInstructorViewModel.class);
        MediatorLiveData<DSC> dscMediatorLiveData =
                viewModel.getDscMediatorLiveData();
        dscMediatorLiveData.observe(this, new Observer<DSC>() {
            @Override
            public void onChanged(@Nullable DSC dsc) {
                lead.setText(dsc.getLeadInstructor());
                mobileInstructor.setText(dsc.getMobileInstructor());
                webInstructor.setText(dsc.getWebInstructor());
                mlInstructor.setText(dsc.getMlInstructor());
                pDesignInstructor.setText(dsc.getpDesignInstructor());
            }
        });
    }

    /** UI is not abstracted from data, UI has direct access to data
     * code will be difficult to maintain especially when trying to get data
     * from a different source
     * There will be a lot of boiler plate code as data becomes complex
     * it's difficult to write pure unit tests that verify the logic, line by line.
     * Everything is crammed into a single Activity object, which becomes difficult to read and manage*/
    public void queryDataDirectly(){
        DatabaseReference reference =
                FirebaseDatabase.getInstance().getReference();
        reference.child("dsc-futa").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lead.setText(dataSnapshot.child("leadInstructor")
                                .getValue(String.class));
                        mobileInstructor.setText(dataSnapshot.child("mobileInstructor")
                                .getValue(String.class));
                        webInstructor.setText(dataSnapshot.child("webInstructor")
                                .getValue(String.class));
                        mlInstructor.setText(dataSnapshot.child("mlInstructor")
                                .getValue(String.class));
                        pDesignInstructor.setText(dataSnapshot.child("pDesignInstructor")
                                .getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

}
