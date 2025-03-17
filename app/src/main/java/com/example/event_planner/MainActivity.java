package com.example.event_planner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the dual-pane container exists (landscape mode)
        if (findViewById(R.id.fragment_detail_container) != null) {
            // We're in landscape mode: load both fragments side by side.
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Load EventListFragment into the list container.
            transaction.replace(R.id.fragment_list_container, new EventListFragment());

            // Load EventDetailFragment into the detail container.
            transaction.replace(R.id.fragment_detail_container, new EventDetailFragment());

            transaction.commit();
        } else {
            // Portrait mode: only load the list fragment.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new EventListFragment())
                    .commit();
        }
    }
    public void navigateToEventList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new EventListFragment())
                .addToBackStack(null)
                .commit();
    }
}
