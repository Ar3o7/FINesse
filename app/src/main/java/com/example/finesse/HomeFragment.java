package com.example.finesse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment implements AddHoursDialogActivity.AddHoursDialogListener {

    private TextView textEstimateIncome;
    private TextView textExpenseTotal;
    private TextView textTotalEst;
    private TextView textNewTitle;
    private FirebaseUser currentFirebaseUser;
    private FirebaseFirestore db;
    private Integer hoursPerDay;
    private Integer daysPerWeek;
    private String name;
    private String dateStart;
    private String dateEnd;
    private Button addHoursButton;
    private Double hoursRate;
    private Double bonus;
    private Double FinEstimateIncome;
    private Double total;
    private Double est;
    private Double income;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textEstimateIncome = view.findViewById(R.id.textEstimateIncome);
        textExpenseTotal = view.findViewById(R.id.textExpenseTotal);
        textTotalEst = view.findViewById(R.id.textTotalEstimate);
        addHoursButton = view.findViewById(R.id.addHoursButton);
        textNewTitle = view.findViewById(R.id.textNewTitle);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        String user = currentFirebaseUser.getUid();

        CollectionReference jobs = db.collection("users").document(user).collection("jobs");
        CollectionReference expenses = db.collection("users").document(user).collection("expenses");

        // Update the textNewTitle TextView with the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        textNewTitle.setText("Current Date: " + currentDate);

        jobs.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getDocuments().size() > 0) {
                name = task.getResult().getDocuments().get(0).get("job name").toString();
                hoursRate = Double.parseDouble(task.getResult().getDocuments().get(0).get("hourly rate").toString());
                hoursPerDay = Integer.parseInt(task.getResult().getDocuments().get(0).get("hours per day").toString());
                daysPerWeek = Integer.parseInt(task.getResult().getDocuments().get(0).get("days per week").toString());
                dateStart = task.getResult().getDocuments().get(0).get("date start").toString();
                dateEnd = task.getResult().getDocuments().get(0).get("date end").toString();
                bonus = Double.parseDouble(task.getResult().getDocuments().get(0).get("bonus").toString());
                FinEstimateIncome = hoursRate * hoursPerDay * daysPerWeek * 4;
                textEstimateIncome.setText("Estimated income: " + FinEstimateIncome + "€");

            } else {
                textEstimateIncome.setText("No job yet");
            }
        });

        expenses.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getDocuments().size() > 0) {
                total = 0.0;
                for (int i = 0; i < task.getResult().size(); i++) {
                    String exp = task.getResult().getDocuments().get(i).get("amount").toString();
                    Double amount = Double.parseDouble(exp);
                    total += amount;
                }

                if (total != null && FinEstimateIncome != null) {
                    textExpenseTotal.setText("Estimated expenses: " + total + "€");
                    est = FinEstimateIncome - total;
                    textTotalEst.setText("Estimated total: " + est + "€");
                } else {
                    textExpenseTotal.setText("No expenses yet");
                    textTotalEst.setText("No estimate yet");
                }
            } else {
                textExpenseTotal.setText("No expenses yet");
            }
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
        TextView textMoneyMade = getView().findViewById(R.id.textMoneyMade);
        textMoneyMade.setText("Money Made: " + income + "€");

        Toast.makeText(requireContext(), "Money made for " + date + ": €" + income, Toast.LENGTH_SHORT).show();
    }
}
