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

        // Simulacija unosa teksta
        String jobName = "Example Job";
        String hoursRate = "10";
        String hoursPerDay = "8";
        String daysPerWeek = "5";
        String startDate = "2023-07-06";
        String endDate = "2023-07-31";
        String bonus = "100";

        when(jobNameEditText.getText().toString()).thenReturn(jobName);
        when(jobHoursRateEditText.getText().toString()).thenReturn(hoursRate);
        when(jobHoursDayEditText.getText().toString()).thenReturn(hoursPerDay);
        when(jobDayWeekEditText.getText().toString()).thenReturn(daysPerWeek);
        when(jobDateStartEditText.getText().toString()).thenReturn(startDate);
        when(jobDateEndEditText.getText().toString()).thenReturn(endDate);
        when(jobBonusEditText.getText().toString()).thenReturn(bonus);

        addJobActivity.addJob();

        // Provjera da li su unosi teksta ispravno prikupljeni
        verify(jobNameEditText).getText();
        verify(jobHoursRateEditText).getText();
        verify(jobHoursDayEditText).getText();
        verify(jobDayWeekEditText).getText();
        verify(jobDateStartEditText).getText();
        verify(jobDateEndEditText).getText();
        verify(jobBonusEditText).getText();
    }
}
