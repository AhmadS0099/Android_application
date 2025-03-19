package com.example.event_planner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.event_planner.ui.login.LoginFragment;

/**
 * MainActivity is the entry point of the application.
 * It determines whether to load one or two fragments based on screen orientation.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout defined in activity_main.xml (or activity_main in landscape)
        setContentView(R.layout.activity_main);

        // Check if the dual-pane container exists (landscape mode)
        if (findViewById(R.id.fragment_detail_container) != null) {
            // We're in landscape mode: load both fragments side by side.
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Load EventListFragment into the list container.
            transaction.replace(R.id.fragment_list_container, new EventListFragment());

            // Load EventDetailFragment into the detail container.
            transaction.replace(R.id.fragment_detail_container, new EventDetailFragment());

            // Commit the transaction to display both fragments
            transaction.commit();
        } else {
            // Portrait mode: only load the list fragment.
            getSupportFragmentManager().beginTransaction()
                    //.replace(R.id.fragment_container, new LoginFragment())
                    .replace(R.id.fragment_container, new EventListFragment())
                    .commit();
        }
    }

    /**
     * Navigates to the EventListFragment.
     * This method can be called by other fragments to trigger navigation.
     */
    public void navigateToEventList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new EventListFragment())
                .addToBackStack(null) // Allows the user to navigate back
                .commit();
    }
}
