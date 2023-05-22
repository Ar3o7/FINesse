package com.example.finesse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddJobActivity extends AppCompatActivity {

    private EditText jobNameEditText;
    private EditText jobHoursRateEditText;
    private EditText jobHoursDayEditText;
    private EditText jobDayWeekEditText;
    private EditText jobDateStartEditText;
    private EditText jobDateEndEditText;
    private EditText jobBonusEditText;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        jobNameEditText = findViewById(R.id.job_name);
        jobHoursRateEditText = findViewById(R.id.job_hrate);
        jobHoursDayEditText = findViewById(R.id.job_hday);
        jobDayWeekEditText = findViewById(R.id.job_dweek);
        jobDateStartEditText = findViewById(R.id.job_dateStart);
        jobDateEndEditText = findViewById(R.id.job_dateEnd);
        jobBonusEditText = findViewById(R.id.job_bonus);
        Button addJobButton = findViewById(R.id.add_job_button);
        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJob();
            }
        });
    }

    private void addJob() {
        String name = jobNameEditText.getText().toString();
        String hoursDayRate = jobHoursRateEditText.getText().toString();
        String hoursPerDay = jobHoursDayEditText.getText().toString();
        String daysPerWeek = jobDayWeekEditText.getText().toString();
        String dateStart = jobDateStartEditText.toString();
        String dateEnd = jobDateEndEditText.getText().toString();
        String bonus = jobBonusEditText.getText().toString();
        String user = currentFirebaseUser.getUid();
        // TODO: NOA NADI NACIN DA NIJE SVE STRING JER KUZIS BROJEVI
        Map<String,Object> job = new HashMap<>();
        job.put("job name",name);
        job.put("hourly rate",hoursDayRate);
        job.put("hours per day",hoursPerDay);
        job.put("days per week",daysPerWeek);
        job.put("date start",dateStart);
        job.put("date end",dateEnd);
        job.put("bonus",bonus);


        db.collection("users").document(user).collection("jobs").add(job)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddJobActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddJobActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });


        // TODO: Find a way to send date created to firebase so we know what job is current

        finish();
    }
}