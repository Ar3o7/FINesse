package com.example.finesse;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddHoursDialogActivity extends DialogFragment {

    public interface AddHoursDialogListener {
        void onHoursAdded(String date, Double hoursWorked);

        void onHoursAdded(String date, Integer hoursWorked);
    }

    private EditText editTextHours;
    private DatePicker datePicker;
    private AddHoursDialogListener listener;
    private Button buttonSelectDate;
    private String selectedDate;

    public Integer hoursWorked;

    public Integer dDay;
    public Integer dMonth;

    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String user = currentFirebaseUser.getUid();

    public static void showAddHoursDialog(@NonNull Context context, @Nullable androidx.fragment.app.Fragment targetFragment) {
        AddHoursDialogActivity dialogFragment = new AddHoursDialogActivity();
        dialogFragment.setTargetFragment(targetFragment, 0);
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "AddHoursDialogActivity");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_add_hours_dialog, null);
        editTextHours = view.findViewById(R.id.editTextHours);


        buttonSelectDate = view.findViewById(R.id.buttonSelectDate);
        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        Button buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoursStr = editTextHours.getText().toString().trim();
                if (!hoursStr.isEmpty() && selectedDate!=null) {
                   hoursWorked = Integer.parseInt(hoursStr);
                    DBSender(hoursWorked,dMonth,dDay);
                    listener.onHoursAdded(selectedDate, hoursWorked);

                    dismiss();
                } else {
                    dismiss();
                }
            }
        });

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(view);
        return dialog;
    }

    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int  month = calendar.get(Calendar.MONTH);
         int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        dMonth = monthOfYear+1;
                        dDay = dayOfMonth;
                        buttonSelectDate.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void DBSender(int hoursWorked, int m, int d){
        String dateM =  Integer.toString(m);
        String dateD =  Integer.toString(d);
        String hw = Integer.toString(hoursWorked);
        Map<String, Object> wDay = new HashMap<>();
        wDay.put("hours worked", hoursWorked);
        wDay.put("day", dateD);
        wDay.put("timestamp", FieldValue.serverTimestamp());


        CollectionReference workdays = db.collection("users").document(user).collection(dateM);
        workdays.document(dateD).set(wDay);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host fragment implements the listener interface
        try {
            listener = (AddHoursDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("The target fragment must implement AddHoursDialogListener");
        }
    }
}