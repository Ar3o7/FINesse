package com.example.finesse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddJobActivity extends AppCompatActivity {

    private EditText jobNameEditText;
    private EditText jobHoursRateEditText;
    private EditText jobHoursDayEditText;
    private EditText jobDayWeekEditText;
    private EditText jobDateStartEditText;
    private EditText jobDateEndEditText;
    private EditText jobBonusEditText;

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
        String dateStart = jobDateStartEditText.getText().toString();
        String dateEnd = jobDateEndEditText.getText().toString();
        String bonus = jobBonusEditText.getText().toString();

        // TODO: Add code here to save the job to your data source

        finish();
    }
}