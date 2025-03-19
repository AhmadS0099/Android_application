package com.example.event_planner;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.event_planner.Remote.IpService;
import com.example.event_planner.local.HolidayDatabase;
import com.example.event_planner.model.Ip;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment that displays a list of holidays.
 * It fetches data from the internet, saves it locally, and displays the list.
 */
public class EventListFragment extends Fragment {

    private IpService mService;
    private ListView listView;
    private Button btnGetHoliday, btnAddHoliday;
    private EditText inputDate, inputName;
    private TextView txtTodayDate;
    private HolidayDatabase db;
    private ArrayAdapter<String> adapter;
    private List<Ip> holidayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        listView = view.findViewById(R.id.List_Of_Holiday);
        btnGetHoliday = view.findViewById(R.id.btn_Get_Holiday_Web);
        btnAddHoliday = view.findViewById(R.id.btn_add_holiday);
        inputDate = view.findViewById(R.id.input_date);
        inputName = view.findViewById(R.id.input_name);
        txtTodayDate = view.findViewById(R.id.txt_today_date);
        Button btnGoToDetail = view.findViewById(R.id.btn_go_to_detail);

        db = HolidayDatabase.getInstance(requireContext());
        mService = Common.getIpService();

        setTodayDate();
        loadHolidaysFromDatabase();

        btnGetHoliday.setOnClickListener(v -> fetchHolidays());
        btnAddHoliday.setOnClickListener(v -> addCustomHoliday());
        btnGoToDetail.setOnClickListener(v -> navigateToDetail());

        // Pre-fill the date if a selected date argument was passed
        if (getArguments() != null && getArguments().containsKey("SELECTED_DATE")) {
            String selectedDate = getArguments().getString("SELECTED_DATE");
            inputDate.setText(selectedDate);
        }

        return view;
    }

    /**
     * Updates the date field in the UI with the selected date.
     *
     * @param date The selected date as a String.
     */
    public void setSelectedDate(String date) {
        if (inputDate != null) {
            inputDate.setText(date);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setTodayDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        txtTodayDate.setText("Today's Date: " + today.format(formatter));
    }

    private void fetchHolidays() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.ipDao().deleteAllHolidays();
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                mService.getHolidays().enqueue(new Callback<List<Ip>>() {
                    @Override
                    public void onResponse(Call<List<Ip>> call, Response<List<Ip>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            holidayList = response.body();
                            new SaveHolidaysTask().execute(holidayList);
                        } else {
                            Toast.makeText(getContext(), "Geen data gevonden", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Ip>> call, Throwable t) {
                        Toast.makeText(getContext(), "Fout: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.execute();
    }

    private void addCustomHoliday() {
        String date = inputDate.getText().toString();
        String name = inputName.getText().toString();
        if (date.isEmpty() || name.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a valid date and name", Toast.LENGTH_SHORT).show();
            return;
        }
        Ip newHoliday = new Ip(date, name, name);
        new AddHolidayTask().execute(newHoliday);
        inputDate.setText("");
        inputName.setText("");
    }

    private void loadHolidaysFromDatabase() {
        new LoadHolidaysTask().execute();
    }

    private void updateListView(List<Ip> holidays) {
        List<String> holidayStrings = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Ip holiday : holidays) {
            LocalDate holidayDate = LocalDate.parse(holiday.getDate());
            if (!holidayDate.isBefore(today)) {
                holidayStrings.add(holiday.getLocalName() + " - " + holiday.getDate());
            }
        }
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, holidayStrings);
        listView.setAdapter(adapter);
    }

    private void navigateToDetail() {
        EventDetailFragment detailFragment = new EventDetailFragment();
        if (getActivity().findViewById(R.id.fragment_detail_container) != null) {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_container, detailFragment)
                    .commit();
        } else {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack("detail")
                    .commit();
        }
    }

    private class SaveHolidaysTask extends AsyncTask<List<Ip>, Void, Void> {
        @Override
        protected Void doInBackground(List<Ip>... lists) {
            db.ipDao().deleteAllHolidays();
            db.ipDao().insertHolidays(lists[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            loadHolidaysFromDatabase();
        }
    }

    private class AddHolidayTask extends AsyncTask<Ip, Void, Void> {
        @Override
        protected Void doInBackground(Ip... holidays) {
            db.ipDao().insertCustomHoliday(holidays[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void unused) {
            loadHolidaysFromDatabase();
        }
    }

    private class LoadHolidaysTask extends AsyncTask<Void, Void, List<Ip>> {
        @Override
        protected List<Ip> doInBackground(Void... voids) {
            return db.ipDao().getAllHolidays();
        }
        @Override
        protected void onPostExecute(List<Ip> holidays) {
            updateListView(holidays);
        }
    }
}
