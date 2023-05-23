package com.example.finesse;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.HashSet;
import java.util.Set;


public class BillsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bills, container, false);
        View addBillButton = view.findViewById(R.id.ButtonAddExpenses);
        addBillButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { addExpense(); }
        });

        View historyButton = view.findViewById(R.id.ButtonHistoryExpenses);

        // Maybe remove the Manage Expenses button, nemogu to prokljuvit

        historyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showHistory();
            }
        });

        View manageExpensesButton = view.findViewById(R.id.ButtonManageExpenses);
        manageExpensesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {manageExpenses();}
        });

        return view;

    }

    private static final String PREFS_NAME = "expenses";
    private static final String PREFS_KEY = "expenses_key";

    private void addExpense() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        builder.setTitle("Add Expense");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText expenseName = dialogView.findViewById(R.id.expense_name);
                String name = expenseName.getText().toString();

                EditText expenseAmount = dialogView.findViewById(R.id.expense_price);
                String amount = expenseAmount.getText().toString();
                double price = Double.parseDouble(amount);

                CheckBox expenseRecurring = dialogView.findViewById(R.id.expense_recurrence);
                boolean recurring = expenseRecurring.isChecked();

                SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
                Set<String> expenses = prefs.getStringSet(PREFS_KEY, new HashSet<String>());
                expenses.add(name + " " + amount + " " + recurring);
                prefs.edit().putStringSet(PREFS_KEY, expenses).apply();

                TextView recentExpense = getView().findViewById(R.id.TextViewExpense1);
                recentExpense.setText(name + " " + amount + " " + recurring);

            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();

        }

    private void showHistory() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_history_expense, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        builder.setTitle("Expense History");

        builder.setNegativeButton("Cancel", null);
        ListView listView = dialogView.findViewById(R.id.listViewHistoryExpense);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        Set<String> expenses = prefs.getStringSet(PREFS_KEY, new HashSet<String>());
        adapter.addAll(expenses);

        builder.show();

    }

    private void manageExpenses() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_manage_expenses, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        builder.setTitle("Manage Expenses");

        builder.setNegativeButton("Cancel", null);
        ListView listView = dialogView.findViewById(R.id.expenses_list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        Set<String> expenses = prefs.getStringSet(PREFS_KEY, new HashSet<String>());
        adapter.addAll(expenses);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String expense = adapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit or Delete Expense");
                builder.setMessage("Do you want to edit or delete this expense?");
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        expenses.remove(expense);

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putStringSet(PREFS_KEY, expenses);
                        editor.apply();

                        adapter.remove(expense);
                        adapter.notifyDataSetChanged();

                        TextView recentExpense = getView().findViewById(R.id.TextViewExpense1);
                        if (expense.equals(recentExpense.getText().toString())) {
                            recentExpense.setText("No Expenses Yet");
                            // TODO: When the most recent expense is deleted, make the TextView show the next most recent expense
                        }
                    }
                    });
                    builder.show();
                }
            });
            builder.show();
        }
}
