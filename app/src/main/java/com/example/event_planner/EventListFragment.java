package com.example.event_planner;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

/**
 * Fragment that displays a list of events.
 * Clicking on an event (or button) navigates to the EventDetailFragment.
 */
public class EventListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Find the button that navigates to the event detail screen
        Button btnGoToDetail = view.findViewById(R.id.btn_go_to_detail);
        btnGoToDetail.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new EventDetailFragment())
                    .addToBackStack(null) // Allow user to go back
                    .commit();
        });
        return view;
    }
}