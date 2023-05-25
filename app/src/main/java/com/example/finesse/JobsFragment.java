package com.example.finesse;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class JobsFragment extends Fragment {

    private Button addJobButton;
    TextView textJobName,textH_rate,textH_day,textD_week,textBonus,textDate_start,textDate_end;

    String name, dateStart, hoursRate, dateEnd, bonus, hoursPerDay, daysPerWeek ;



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


        addJobs();

        //TODO: treba sredit da se refresha ;-;







        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddJobActivity.class);
                startActivity(intent);
                addJobs();
            }
        });
        addJobs();
        return view;

        }

    private void addJobs() {
        String user = currentFirebaseUser.getUid();
        CollectionReference jobs = db.collection("users").document(user).collection("jobs");
        jobs.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getDocuments().size() > 0) {
                name = task.getResult().getDocuments().get(0).get("job name").toString();
                hoursRate = task.getResult().getDocuments().get(0).get("hourly rate").toString();
                hoursPerDay = task.getResult().getDocuments().get(0).get("hours per day").toString();
                daysPerWeek = task.getResult().getDocuments().get(0).get("days per week").toString();
                dateStart = task.getResult().getDocuments().get(0).get("date start").toString();
                dateEnd = task.getResult().getDocuments().get(0).get("date end").toString();
                bonus = task.getResult().getDocuments().get(0).get("bonus").toString();
                textJobName.setText("name: " + name);
                textH_rate.setText("Hourly rate: " + hoursRate + "€");
                textH_day.setText("Hours per day: " + hoursPerDay);
                textD_week.setText("Days per week: " + daysPerWeek);
                textBonus.setText("bonus: " + bonus);
                textDate_start.setText("Date started: " + dateStart);
                textDate_end.setText("date to end: " + dateEnd);

            }else {
                textJobName.setText("No job yet");
            }
        });
    }
    }


