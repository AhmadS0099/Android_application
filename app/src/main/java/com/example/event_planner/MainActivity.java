package com.example.event_planner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.event_planner.ui.login.LoginFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load LoginFragment when the app starts
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    //.replace(R.id.fragment_container, new LoginFragment())
                    .replace(R.id.fragment_container, new EventListFragment())
                    .commit();
        }
    }

    // Method to replace the fragment after login
    public void navigateToEventList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new EventListFragment())
                .addToBackStack(null)
                .commit();
    }
}
