package com.example.event_planner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.event_planner.Remote.IpService;
import com.example.event_planner.model.Ip;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventListFragment extends Fragment {
    private IpService mService;
    private ListView listView;
    private Button btnGetHoliday;
    private TextView txtTodayDate; // TextView to display today's date

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Initialize views
        listView = view.findViewById(R.id.List_Of_Holiday);
        btnGetHoliday = view.findViewById(R.id.btn_Get_Holiday_Web);
        Button btnGoToDetail = view.findViewById(R.id.btn_go_to_detail);
        txtTodayDate = view.findViewById(R.id.txt_today_date); // Get reference to the TextView

        // Setup service
        mService = Common.getIpService();

        // Set today's date
        setTodayDate();

        // Button click listeners
        btnGetHoliday.setOnClickListener(v -> fetchHolidays());
        btnGoToDetail.setOnClickListener(v -> navigateToDetail());

        return view;
    }

    private void setTodayDate() {
        // Get today's date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String formattedDate = today.format(formatter);

        // Set the formatted date in the TextView
        txtTodayDate.setText("Today's Date: " + formattedDate);
    }

    private void fetchHolidays() {
        mService.getHolidays().enqueue(new Callback<List<Ip>>() {
            @Override
            public void onResponse(Call<List<Ip>> call, Response<List<Ip>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> holidayStrings = new ArrayList<>();
                    LocalDate today = LocalDate.now();

                    for (Ip holiday : response.body()) {
                        // Convert holiday date to LocalDate
                        String holidayDateString = holiday.getDate(); // e.g., "2025-12-25"
                        LocalDate holidayDate = LocalDate.parse(holidayDateString);

                        // Only show upcoming holidays (not passed)
                        if (!holidayDate.isBefore(today)) {
                            holidayStrings.add(holiday.getLocalName() + " - " + holiday.getDate());
                        }
                    }

                    // Show holidays in ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            holidayStrings
                    );
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Ip>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToDetail() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new EventDetailFragment())
                .addToBackStack(null)
                .commit();
    }
}
