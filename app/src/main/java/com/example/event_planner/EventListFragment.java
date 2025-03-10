package com.example.event_planner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.event_planner.Remote.IpService;
import com.example.event_planner.model.Ip;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventListFragment extends Fragment {
    private IpService mService;
    private ListView listView;
    private Button btnGetHoliday;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Initialize views
        listView = view.findViewById(R.id.List_Of_Holiday);
        btnGetHoliday = view.findViewById(R.id.btn_Get_Holiday_Web);
        Button btnGoToDetail = view.findViewById(R.id.btn_go_to_detail);

        // Setup service
        mService = Common.getIpService();

        // Button click listeners
        btnGetHoliday.setOnClickListener(v -> fetchHolidays());
        btnGoToDetail.setOnClickListener(v -> navigateToDetail());

        return view;
    }

    private void fetchHolidays() {
        mService.getHolidays().enqueue(new Callback<List<Ip>>() {
            @Override
            public void onResponse(Call<List<Ip>> call, Response<List<Ip>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> holidayStrings = new ArrayList<>();
                    for (Ip holiday : response.body()) {
                        holidayStrings.add(holiday.getLocalName() + " - " + holiday.getDate());
                    }

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