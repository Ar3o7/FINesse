package com.example.finesse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class BillsFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Before
    public void setup() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Test
    public void testListExpenses() {
        Context context = ApplicationProvider.getApplicationContext();
        BillsFragment fragment = new BillsFragment();
        activityRule.getActivity().getSupportFragmentManager().beginTransaction().add(fragment, null).commit();

        // Inflate the fragment's view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_bills, null);

        // Invoke the method to be tested
        fragment.listExpenses(view);

        // Find the TextView within the fragment's view
        TextView recentExpense = view.findViewById(R.id.TextViewExpense1);

        // Perform assertions
        assertNotNull("TextView not found", recentExpense);
        assertEquals("expenses 1", recentExpense.getText().toString());
    }
}
