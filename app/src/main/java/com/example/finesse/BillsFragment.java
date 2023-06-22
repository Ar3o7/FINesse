package com.example.finesse;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillsFragment extends Fragment {

    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bills, container, false);
        View addBillButton = view.findViewById(R.id.ButtonAddExpenses);

        String user = currentFirebaseUser.getUid();
        CollectionReference expenses = db.collection("users").document(user).collection("expenses");

        listExpenses(view);

        addBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });

        View manageExpensesButton = view.findViewById(R.id.ButtonManageExpenses);
        manageExpensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageExpenses();
            }
        });

        return view;
    }

    public void listExpenses(View view) {
        String user = currentFirebaseUser.getUid();
        CollectionReference expenses = db.collection("users").document(user).collection("expenses");

        TextView recentExpense = view.findViewById(R.id.TextViewExpense1);
        expenses.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getDocuments().size() > 0) {
                String name = task.getResult().getDocuments().get(0).get("name").toString();
                String amount = task.getResult().getDocuments().get(0).get("amount").toString();
                String recurring = task.getResult().getDocuments().get(0).get("recurring").toString();

                String recurringText = recurring.equals("true") ? "Monthly " : " ";
                String expense = name + ", " + amount + "€  " + recurringText;
                recentExpense.setText(expense);
            } else {
                recentExpense.setText("No expenses yet");
            }
        });
    }

    public void addExpense() {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String user = currentFirebaseUser.getUid();
                CollectionReference expenses = db.collection("users").document(user).collection("expenses");

                EditText expenseName = dialogView.findViewById(R.id.expense_name);
                String name = expenseName.getText().toString();

                EditText expenseAmount = dialogView.findViewById(R.id.expense_price);
                String amount = expenseAmount.getText().toString();

                CheckBox expenseRecurring = dialogView.findViewById(R.id.expense_recurrence);
                boolean recurring = expenseRecurring.isChecked();



                if (!name.isEmpty() && !amount.isEmpty()) {

                    TextView recentExpense = getView().findViewById(R.id.TextViewExpense1);
                    String recurringText = recurring ? "Monthly " : " ";
                    recentExpense.setText(name + ", " + amount + "€  " + recurringText);

                    Map<String, Object> expense = new HashMap<>();
                    expense.put("name", name);
                    expense.put("amount", amount);
                    expense.put("recurring", recurring);
                    expense.put("timestamp", FieldValue.serverTimestamp());

                    expenses.document(name).set(expense);
                } else {
                    Toast.makeText(getContext(), "Please enter a name and amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();

    }


    public void refreshFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
    }

    private void manageExpenses() {
        List<String> recentExpenses = new ArrayList<>();
        String user = currentFirebaseUser.getUid();
        CollectionReference expenses = db.collection("users").document(user).collection("expenses");

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_manage_expenses, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        builder.setTitle("Manage Expenses");

        builder.setNegativeButton("Cancel", null);
        ListView listView = dialogView.findViewById(R.id.expenses_list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        expenses.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recentExpenses.clear();
                for (int i = 0; i < task.getResult().size(); i++) {
                    String name = task.getResult().getDocuments().get(i).get("name").toString();
                    String amount = task.getResult().getDocuments().get(i).get("amount").toString();
                    String recurring = task.getResult().getDocuments().get(i).get("recurring").toString();

                    String recurringText = recurring.equals("true") ? "Monthly " : " ";
                    String expense = name + ", " + amount + "€  " + recurringText;
                    adapter.add(expense);
                    recentExpenses.add(expense);
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String expense = adapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Expense");
                builder.setMessage("Do you want to delete this expense?");
                builder.setPositiveButton("Cancel", null);
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String user = currentFirebaseUser.getUid();
                        CollectionReference expenses = db.collection("users").document(user).collection("expenses");

                        String expenseName = expense.split(",")[0];
                        expenses.document(expenseName).delete();

                        adapter.remove(expense);
                        adapter.notifyDataSetChanged();

                        recentExpenses.remove(expense);
                        listExpenses(getView());
                    }
                });
                builder.show();
            }
        });
        builder.show();
    }
}
