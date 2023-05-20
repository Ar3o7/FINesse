package com.example.finesse;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class JobsFragment extends Fragment {

    private Button addJobButton;
    TextView textJobName,textH_rate,textH_day,textD_week,textBonus,textDate_start,textDate_end;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);
        Button addJobButton = view.findViewById(R.id.ButtonAddJob);

        textJobName = view.findViewById(R.id.textJobName);
        textH_rate = view.findViewById(R.id.textH_rate);
        textH_day = view.findViewById(R.id.textH_day);
        textD_week = view.findViewById(R.id.textD_week);
        textBonus = view.findViewById(R.id.textBonus);
        textDate_start = view.findViewById(R.id.textDate_start);
        textDate_end = view.findViewById(R.id.textDate_end);
        String user = currentFirebaseUser.getUid();

        DocumentReference documentReference = db.collection("users").document(user).collection("jobs").document();
        documentReference.addSnapshotListener(JobsFragment.super.getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

            }
        });

        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddJobActivity.class);
                startActivity(intent);
            }
        });
        return view;
        
        }
    }


