package com.example.finesse;

import org.junit.Test;
import org.mockito.Mockito;
import android.text.Editable;
import android.widget.EditText;
import static org.mockito.Mockito.*;

public class AddJobActivityTest {
    @Test
    public void testAddJob() {
        AddJobActivity addJobActivity = Mockito.mock(AddJobActivity.class);
        EditText jobNameEditText = Mockito.mock(EditText.class);
        EditText jobHoursRateEditText = Mockito.mock(EditText.class);
        EditText jobHoursDayEditText = Mockito.mock(EditText.class);
        EditText jobDayWeekEditText = Mockito.mock(EditText.class);
        EditText jobDateStartEditText = Mockito.mock(EditText.class);
        EditText jobDateEndEditText = Mockito.mock(EditText.class);
        EditText jobBonusEditText = Mockito.mock(EditText.class);

        Editable mockEditable = Mockito.mock(Editable.class);

        when(jobNameEditText.getText()).thenReturn(mockEditable);
        when(jobHoursRateEditText.getText()).thenReturn(mockEditable);
        when(jobHoursDayEditText.getText()).thenReturn(mockEditable);
        when(jobDayWeekEditText.getText()).thenReturn(mockEditable);
        when(jobDateStartEditText.getText()).thenReturn(mockEditable);
        when(jobDateEndEditText.getText()).thenReturn(mockEditable);
        when(jobBonusEditText.getText()).thenReturn(mockEditable);

        addJobActivity.addJob();


    }
}