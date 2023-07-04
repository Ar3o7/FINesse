package com.example.finesse;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomeFragment extends Fragment implements AddHoursDialogActivity.AddHoursDialogListener {

    private TextView textEstimateIncome, textExpenseTotal, textNewTitle, textDaysWorked,textHoursWorked,textMoneyMade,textTotalEstimate, textAmountMade;
    private FirebaseUser currentFirebaseUser;
    private FirebaseFirestore db;
    private Integer hoursPerDay,daysPerWeek;
    private String name,dateStart,dateEnd;
    private Button addHoursButton;
    public Double hoursRate, bonus, FinEstimateIncome, total, est, income;

    public Integer numHours, numDays;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textExpenseTotal = view.findViewById(R.id.textExpenseTotal);
        addHoursButton = view.findViewById(R.id.addHoursButton);
        textNewTitle = view.findViewById(R.id.textNewTitle);
        textDaysWorked = view.findViewById(R.id.textDaysWorked);
        textHoursWorked = view.findViewById(R.id.textHoursWorked);
        textMoneyMade = view.findViewById(R.id.textMoneyMade);
        textTotalEstimate = view.findViewById(R.id.textTotalEstimate);
        textAmountMade = view.findViewById(R.id.textAmountMade);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        String user = currentFirebaseUser.getUid();

        CollectionReference jobs = db.collection("users").document(user).collection("jobs");
        CollectionReference expenses = db.collection("users").document(user).collection("expenses");


        // Update the textNewTitle TextView with the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        textNewTitle.setText("Current Date: " + currentDate);

        // Month DB sender
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String currentMonth = monthFormat.format(calendar.getTime());

        CollectionReference workDays = db.collection("users").document(user).collection(currentMonth);


        jobs.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(querySnapshot1 -> {
            if (querySnapshot1.isSuccessful()) {
                QuerySnapshot qs1 = querySnapshot1.getResult();
                if (qs1 != null && qs1.getDocuments().size() > 0) {
                    double hoursRate = Double.parseDouble(qs1.getDocuments().get(0).get("hourly rate").toString());
                    int hoursPerDay = Integer.parseInt(qs1.getDocuments().get(0).get("hours per day").toString());
                    int daysPerWeek = Integer.parseInt(qs1.getDocuments().get(0).get("days per week").toString());
                    double before = hoursRate * hoursPerDay * daysPerWeek * 4;
                    DecimalFormat df = new DecimalFormat("#.##");
                    double FinEstimateIncome = Double.parseDouble(df.format(before));

                    textAmountMade.setText("Estimated income: " + FinEstimateIncome + "€");
                }
            } else {
                Exception e = querySnapshot1.getException();
                e.printStackTrace();
            }
        });

        //Query for income and expenses
        jobs.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(querySnapshot1 -> {
            expenses.get().addOnCompleteListener(querySnapshot2 -> {
                if (querySnapshot1.isSuccessful() && querySnapshot2.isSuccessful()) {
                    QuerySnapshot qs1 = querySnapshot1.getResult();
                    QuerySnapshot qs2 = querySnapshot2.getResult();
                    if (qs1 != null && qs1.getDocuments().size() > 0 && qs2 != null && qs2.getDocuments().size() > 0) {
                        double hoursRate = Double.parseDouble(qs1.getDocuments().get(0).get("hourly rate").toString());
                        int hoursPerDay = Integer.parseInt(qs1.getDocuments().get(0).get("hours per day").toString());
                        int daysPerWeek = Integer.parseInt(qs1.getDocuments().get(0).get("days per week").toString());
                        double before = hoursRate * hoursPerDay * daysPerWeek * 4;
                        DecimalFormat df = new DecimalFormat("#.##");
                        double FinEstimateIncome = Double.parseDouble(df.format(before));

                        double total = 0.0;
                        for (int i = 0; i < qs2.size(); i++) {
                            double d = Double.parseDouble(qs2.getDocuments().get(i).get("amount").toString());
                            total += d;
                        }

                        double diff = FinEstimateIncome - total;
                        double difference = Double.parseDouble(df.format(diff));
                        textExpenseTotal.setText("Total expenses: " + total + "€");
                        textTotalEstimate.setText("Estimated balance: " + difference + "€");
                    }
                } else {
                    Exception e = querySnapshot1.getException();

                    if (e == null) {
                        e = querySnapshot2.getException();
                    }
                    e.printStackTrace();
                }
            });
        });


        //Query for daily work
        jobs.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(querySnapshot1 -> {
            workDays.get().addOnCompleteListener(querySnapshot3 -> {
                if (querySnapshot1.isSuccessful() && querySnapshot3.isSuccessful()) {
                    QuerySnapshot qs1 = querySnapshot1.getResult();
                    QuerySnapshot qs3 = querySnapshot3.getResult();
                    if (qs1 != null && qs1.getDocuments().size() > 0 && qs3 != null && qs3.getDocuments().size() > 0) {
                        double hoursRate = Double.parseDouble(qs1.getDocuments().get(0).get("hourly rate").toString());


                        numHours = 0;
                        numDays = 0;
                        for (int i = 0; i < qs3.size(); i++) {
                            int d = Integer.parseInt(qs3.getDocuments().get(i).get("hours worked").toString());
                            numHours += d;
                            numDays++;
                        }
                        DecimalFormat df = new DecimalFormat("#.##");
                        double bfActualIncome = numHours * hoursRate;
                        double actualIncome = Double.parseDouble(df.format(bfActualIncome));
                        textDaysWorked.setText("Days worked: " + numDays);
                        textHoursWorked.setText("Hours worked: " + numHours + "h");
                        textMoneyMade.setText("Income: " + actualIncome + "€");
                    }
                } else {
                    Exception e = querySnapshot1.getException();

                    if (e == null) {
                        e = querySnapshot3.getException();
                    }
                    e.printStackTrace();
                }
            });
        });


        addHoursButton.setOnClickListener(v -> showAddHoursDialog());

        return view;
    }

    private void showAddHoursDialog() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        AddHoursDialogActivity dialogFragment = new AddHoursDialogActivity();
        dialogFragment.setTargetFragment(HomeFragment.this, 0);
        dialogFragment.show(fragmentManager, "AddHoursDialogActivity");
    }




    @Override
    public void onHoursAdded(String date, Double hoursWorked) {
        income = hoursRate * hoursWorked;

        // Update money made textView
        textMoneyMade.setText("Money Made: " + income + "€");

        Toast.makeText(requireContext(), "Money made for " + date + ": €" + income, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHoursAdded(String date, Integer hoursWorked) {

    }
}
