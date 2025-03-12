package com.example.event_planner;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.event_planner.local.HolidayDatabase;
import com.example.event_planner.model.Ip;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that displays the details of an event.
 * This fragment includes a back button to navigate to the previous screen.
 */
public class EventDetailFragment extends Fragment {
    private TableLayout calendarTable;
    private TextView txtWeekRange;
    private LocalDate currentWeekStart;
    private HolidayDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        db = HolidayDatabase.getInstance(requireContext());

        // Initialiseer de back-knop
        Button btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            // Navigeer terug naar het vorige fragment
            getParentFragmentManager().popBackStack();
        });

        // Initialiseer UI
        calendarTable = view.findViewById(R.id.calendar_table);
        txtWeekRange = view.findViewById(R.id.txt_week_range);
        Button btnPrev = view.findViewById(R.id.btn_prev_week);
        Button btnNext = view.findViewById(R.id.btn_next_week);

        // Start met huidige week
        currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        updateWeekCalendar();

        // Week navigatie
        btnPrev.setOnClickListener(v -> {
            currentWeekStart = currentWeekStart.minusWeeks(1);
            updateWeekCalendar();
        });

        btnNext.setOnClickListener(v -> {
            currentWeekStart = currentWeekStart.plusWeeks(1);
            updateWeekCalendar();
        });

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }

    private void updateWeekCalendar() {
        // Toon weekbereik (bijv. "10 Apr - 16 Apr 2023")
        LocalDate endOfWeek = currentWeekStart.plusDays(6);
        String weekRange = currentWeekStart.format(DateTimeFormatter.ofPattern("d MMM")) + " - " +
                endOfWeek.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        txtWeekRange.setText(weekRange);

        // Verwijder oude kalenderrijen
        calendarTable.removeAllViews();

        // Laad events voor deze week uit de database
        new LoadWeekEventsTask().execute();
    }

    private class LoadWeekEventsTask extends AsyncTask<Void, Void, List<Ip>> {
        @Override
        protected List<Ip> doInBackground(Void... voids) {
            // Query voor events in deze week
            String startDate = currentWeekStart.format(DateTimeFormatter.ISO_DATE);
            String endDate = currentWeekStart.plusDays(6).format(DateTimeFormatter.ISO_DATE);
            return db.ipDao().getEventsBetweenDates(startDate, endDate);
        }

        @Override
        protected void onPostExecute(List<Ip> events) {
            // Bouw de kalender op
            for (int i = 0; i < 7; i++) {
                LocalDate date = currentWeekStart.plusDays(i);
                List<Ip> dailyEvents = filterEventsForDate(events, date);

                // Maak een rij voor elke dag
                TableRow row = new TableRow(requireContext());
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                // Dag en datum
                TextView tvDate = new TextView(requireContext());
                tvDate.setText(date.format(DateTimeFormatter.ofPattern("E\nd MMM")));
                tvDate.setPadding(8, 8, 8, 8);
                row.addView(tvDate);

                // Events voor deze dag
                LinearLayout eventsLayout = new LinearLayout(requireContext());
                eventsLayout.setOrientation(LinearLayout.VERTICAL);
                for (Ip event : dailyEvents) {
                    TextView tvEvent = new TextView(requireContext());
                    tvEvent.setText("• " + event.getLocalName());
                    tvEvent.setPadding(8, 4, 8, 4);
                    eventsLayout.addView(tvEvent);
                }
                row.addView(eventsLayout);

                calendarTable.addView(row);
            }
        }

        private List<Ip> filterEventsForDate(List<Ip> events, LocalDate date) {
            List<Ip> result = new ArrayList<>();
            String targetDate = date.format(DateTimeFormatter.ISO_DATE);
            for (Ip event : events) {
                if (event.getDate().equals(targetDate)) {
                    result.add(event);
                }
            }
            return result;
        }
    }
}
