package com.example.finesse;

import android.os.Bundle;

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

public class HomeFragment extends Fragment implements AddHoursDialogActivity.AddHoursDialogListener{

    TextView textEstimateIncome, textExpenseTotal, textTotalEst;

    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Integer hoursPerDay, daysPerWeek ;
    String name, dateStart, dateEnd;

    Button addHoursButton;

    public Double hoursRate, bonus, FinEstimateIncome, total, est;

    //TODO: treba dodat nacin da aplikacija zna koliko je dana izmedu start i end da moze racunat kako treba, trenutno je na mjesec dana

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textEstimateIncome = view.findViewById(R.id.textEstimateIncome);
        textExpenseTotal = view.findViewById(R.id.textExpenseTotal);
        textTotalEst = view.findViewById(R.id.textTotalEstimate);
        addHoursButton = view.findViewById(R.id.addHoursButton);
        String user = currentFirebaseUser.getUid();

        CollectionReference jobs = db.collection("users").document(user).collection("jobs");
        CollectionReference expenses = db.collection("users").document(user).collection("expenses");

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
                textEstimateIncome.setText("Estimated income: " + FinEstimateIncome.toString() + "€");
                //TODO fix the 892.80000000000000001 euro glitch
            } else {
                textEstimateIncome.setText("No job yet");
            }
        });
        expenses.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getDocuments().size() > 0) {
                for (int i = 0; i < task.getResult().size(); i++) {
                    String exp = task.getResult().getDocuments().get(i).get("amount").toString();
                    Double amount = Double.parseDouble(exp);

                    if (i == 0)
                    {
                        total = amount;
                    }else{

                        total = total + amount;
                    }

                }
                if(total != null && FinEstimateIncome !=null){
                    textExpenseTotal.setText("Estimated expenses: " + total.toString() + "€");
                    est = FinEstimateIncome - total;
                    textTotalEst.setText("Estimated total: " + est.toString() + "€");

                }else{
                    textExpenseTotal.setText("No expense yet");
                    textTotalEst.setText("No estimate yet");
                }
            }else{
                textExpenseTotal.setText("No expense yet");
            }
        });

        addHoursButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showAddHoursDialog();
            }
        });

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
        Double income = hoursRate * hoursWorked;
        Toast.makeText(requireContext(), "Hours added: " + hoursWorked, Toast.LENGTH_SHORT).show();
        Toast.makeText(requireContext(), "Income for " + date + ": " + income, Toast.LENGTH_SHORT).show();

    }
}