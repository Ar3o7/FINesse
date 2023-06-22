package com.example.finesse;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddHoursDialogActivity extends DialogFragment {

    public interface AddHoursDialogListener {
        void onHoursAdded(Double hoursWorked);
    }

    private EditText editTextHours;
    private AddHoursDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_add_hours_dialog, null);
        editTextHours = view.findViewById(R.id.editTextHours);

        Button buttonAdd = view.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoursStr = editTextHours.getText().toString().trim();
                if (!hoursStr.isEmpty()) {
                    Double hoursWorked = Double.parseDouble(hoursStr);
                    listener.onHoursAdded(hoursWorked);
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



