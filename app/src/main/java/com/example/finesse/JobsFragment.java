package com.example.finesse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class JobsFragment extends Fragment {

    private static final int ADD_JOB_REQUEST_CODE = 1;

    private Button addJobButton;
    TextView textJobName, textH_rate, textH_day, textD_week, textBonus, textDate_start, textDate_end;

    String name, dateStart, hoursRate, dateEnd, bonus, hoursPerDay, daysPerWeek;

    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs, container, false);
        addJobButton = view.findViewById(R.id.ButtonAddJob);

        textJobName = view.findViewById(R.id.textJobName);
        textH_rate = view.findViewById(R.id.textH_rate);
        textH_day = view.findViewById(R.id.textH_day);
        textD_week = view.findViewById(R.id.textD_week);
        textBonus = view.findViewById(R.id.textBonus);
        textDate_start = view.findViewById(R.id.textDate_start);
        textDate_end = view.findViewById(R.id.textDate_end);

        addJobs();

        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddJobActivity.class);
                startActivityForResult(intent, ADD_JOB_REQUEST_CODE);
            }
        });

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
                textJobName.setText("Name: " + name);
                textH_rate.setText("Hourly rate: " + hoursRate + "€");
                textH_day.setText("Hours per day: " + hoursPerDay);
                textD_week.setText("Days per week: " + daysPerWeek);
                textBonus.setText("Bonus: " + bonus);
                textDate_start.setText("Date started: " + dateStart);
                textDate_end.setText("End Date: " + dateEnd);
            } else {
                textD_week.setText("No job yet");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_JOB_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            addJobs();
        }
    }
}
