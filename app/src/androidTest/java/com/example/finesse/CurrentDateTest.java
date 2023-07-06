package com.example.finesse;

import android.content.Context;
import android.widget.TextView;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static org.junit.Assert.assertEquals;

public class CurrentDateTest {

    @Test
    public void testCurrentDate() {
        Context context = ApplicationProvider.getApplicationContext();
        TextView textNewTitle = new TextView(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        textNewTitle.setText("Current Date: " + currentDate);
        assertEquals(textNewTitle.getText().toString(), "Current Date: " + currentDate);
    }
}
