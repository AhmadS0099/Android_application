package com.example.event_planner;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.example.event_planner.ui.login.LoginFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use the appropriate layout (activity_main.xml in portrait, activity_main-land.xml in landscape)
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Instead of checking for dual-pane mode, always load the LoginFragment.
            // In portrait mode, use the container defined in res/layout/activity_main.xml.
            // In landscape mode, if you have a dual-pane layout, load LoginFragment into the primary container.
            if (findViewById(R.id.fragment_detail_container) != null) {
                // Landscape: load LoginFragment into the list container.
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_list_container, new LoginFragment());
                // Optionally, you can leave the detail container empty or show a placeholder.
                transaction.commit();
            } else {
                // Portrait: load LoginFragment.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_list_container, new LoginFragment())
                        .commit();
            }
        }
    }

    /**
     * Navigates to the EventListFragment after login.
     * This method clears the back stack so that the user cannot return to the login screen.
     */
    public void navigateToEventList() {
        // Clear the back stack
        getSupportFragmentManager().popBackStack(null,
                getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);

        if (findViewById(R.id.fragment_detail_container) != null) {
            // Landscape mode: load EventListFragment into the list container
            // and, optionally, load an initial EventDetailFragment into the detail container.
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_list_container, new EventListFragment());
            // Optionally, if you want to show the detail fragment by default:
            transaction.replace(R.id.fragment_detail_container, new EventDetailFragment());
            transaction.commit();
        } else {
            // Portrait mode: load EventListFragment into the single container.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_list_container, new EventListFragment())
                    .commit();
        }
    }
}
