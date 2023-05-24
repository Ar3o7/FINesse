package com.example.finesse;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment{

    TextView textEstimateIncome, TextTotal, textTotalEst;

    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Integer hoursPerDay, daysPerWeek ;
    String name, dateStart, dateEnd;

    Double hoursRate, bonus, FinEstimateIncome;

    //TODO: treba dodat nacin da aplikacija zna koliko je dana izmedu start i end da moze racunat kako treba, trenutno je na mjesec dana

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textEstimateIncome = view.findViewById(R.id.textEstimateIncome);
        TextTotal = view.findViewById(R.id.textExpenseTotal);
        textTotalEst = view.findViewById(R.id.textTotalEstimate);
        String user = currentFirebaseUser.getUid();

        CollectionReference jobs = db.collection("users").document(user).collection("jobs");

        //TODO: treba sredit da nekako uzima najnoviji poso
        jobs.document("lord d.o.o").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null && documentSnapshot.exists()){

                        name = documentSnapshot.getString("job name");
                        hoursRate = Double.parseDouble(documentSnapshot.getString("hourly rate"));
                        hoursPerDay = Integer.parseInt(documentSnapshot.getString("hours per day"));
                        daysPerWeek = Integer.parseInt(documentSnapshot.getString("days per week"));
                        dateStart = documentSnapshot.getString("date start");
                        dateEnd = documentSnapshot.getString("date end");
                        bonus = Double.parseDouble(documentSnapshot.getString("bonus"));

                        FinEstimateIncome = hoursRate * hoursPerDay * daysPerWeek * 4;
                        textEstimateIncome.setText(FinEstimateIncome.toString());

                        //TODO: treba napravit sad i za expences i onda za tota

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeFragment.super.getActivity(), "Error = " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }
}