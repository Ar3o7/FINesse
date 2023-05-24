package com.example.finesse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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

        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        jobDateStartEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month  = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddJobActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month , int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        String dateStart = dateFormat.format(calendar.getTime());
                        jobDateStartEditText.setText(dateStart);

                        calendar.add(Calendar.DAY_OF_MONTH, month == Calendar.FEBRUARY ? 28 : 30);
                        String dateEnd = dateFormat.format(calendar.getTime());
                        jobDateEndEditText.setText(dateEnd);
                    }
                },year,month,dayOfMonth);
                datePickerDialog.show();
            }
        });

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
        String user = currentFirebaseUser.getUid();

        // TODO: Add validation to check if all fields are filled in

        // TODO: Promijeni end date u TextView tako da ga korisnik ne moze editat

        //TODO: nisi me bas skuzio moras pormjenit da u pozicijama di se trazi broj samo mozes broj upisat

        // NOTE: kad kliknes da pickas date, pojavi se tastatura, pretp radi toga sto je edit text
        //NOTE: gle ja nmg birat nista nego upisat tako da idk what you talking about...
        //NOTE: Okay skuzio sam nakon sto sam probo duplo kliknut i uspilo je. tehnički mozes stavit
        //da je button ili da text thingy nije edditable mozda bi to radilo.



        Map<String,Object> job = new HashMap<>();
        job.put("job name",name);
        job.put("hourly rate",hoursDayRate);
        job.put("hours per day",hoursPerDay);
        job.put("days per week",daysPerWeek);
        job.put("date start",dateStart);
        job.put("date end",dateEnd);
        job.put("bonus",bonus);

        CollectionReference jobs = db.collection("users").document(user).collection("jobs");
        jobs.document(name).set(job);





        finish();
    }
}