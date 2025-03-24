package com.example.event_planner;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.example.event_planner.local.HolidayDatabase;
import com.example.event_planner.model.Ip;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that displays the details of a week.
 * It shows a weekly calendar with navigation to previous and next weeks.
 */
public class EventDetailFragment extends Fragment {
    // UI elements for the calendar
    private TableLayout calendarTable;
    private TextView txtWeekRange;
    // The starting day (Monday) of the current week being displayed
    private LocalDate currentWeekStart;
    // Reference to the local Room database
    private HolidayDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment from XML
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        // Get an instance of the Room database
        db = HolidayDatabase.getInstance(requireContext());

        // Initialize the back button and set its click listener
        Button btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        // Initialize UI components from the layout
        calendarTable = view.findViewById(R.id.calendar_table);
        txtWeekRange = view.findViewById(R.id.txt_week_range);
        Button btnPrev = view.findViewById(R.id.btn_prev_week);
        Button btnNext = view.findViewById(R.id.btn_next_week);

        // Set the starting week to the current week (starting from Monday)
        currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        updateWeekCalendar();

        // Set click listeners for previous and next week navigation
        btnPrev.setOnClickListener(v -> {
            currentWeekStart = currentWeekStart.minusWeeks(1);
            updateWeekCalendar();
        });
        btnNext.setOnClickListener(v -> {
            currentWeekStart = currentWeekStart.plusWeeks(1);
            updateWeekCalendar();
        });
        return view;
    }

    /**
     * Updates the weekly calendar UI.
     * This method calculates the week range, clears previous entries, and starts loading events for the week.
     */
    private void updateWeekCalendar() {
        // Calculate the end date of the week (6 days after the start)
        LocalDate endOfWeek = currentWeekStart.plusDays(6);
        // Format the week range text (e.g., "10 Apr - 16 Apr 2023")
        String weekRange = currentWeekStart.format(DateTimeFormatter.ofPattern("d MMM"))
                + " - " + endOfWeek.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        txtWeekRange.setText(weekRange);

        // Remove all existing rows from the calendar table
        calendarTable.removeAllViews();

        // Load events for the current week from the database asynchronously
        new LoadWeekEventsTask().execute();
    }

    /**
     * AsyncTask to load events from the local database for the current week.
     */
    private class LoadWeekEventsTask extends AsyncTask<Void, Void, List<Ip>> {
        @Override
        protected List<Ip> doInBackground(Void... voids) {
            // Format start and end dates in ISO format
            String startDate = currentWeekStart.format(DateTimeFormatter.ISO_DATE);
            String endDate = currentWeekStart.plusDays(6).format(DateTimeFormatter.ISO_DATE);
            // Query the database for events between these dates
            return db.ipDao().getEventsBetweenDates(startDate, endDate);
        }

        @Override
        protected void onPostExecute(List<Ip> events) {
            // Loop through each day of the week (7 days)
            for (int i = 0; i < 7; i++) {
                // Calculate the date for this row
                LocalDate date = currentWeekStart.plusDays(i);
                List<Ip> dailyEvents = filterEventsForDate(events, date);

                // Create a new table row for this day
                TableRow row = new TableRow(requireContext());
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                // Create a TextView to display the day and date; make it clickable
                TextView tvDate = new TextView(requireContext());
                tvDate.setText(date.format(DateTimeFormatter.ofPattern("E\nd MMM")));
                tvDate.setPadding(16, 16, 16, 16);
                tvDate.setBackgroundResource(R.drawable.clickable_background); // Provide visual feedback
                tvDate.setGravity(Gravity.CENTER);
                tvDate.setClickable(true);
                tvDate.setFocusable(true);

                // Set a click listener on the date TextView
                tvDate.setOnClickListener(v -> {
                    // Format the selected date as a string
                    String formattedDate = date.format(DateTimeFormatter.ISO_DATE);

                    // Check if we are in dual-pane mode (landscape)
                    if (getActivity().findViewById(R.id.fragment_list_container) != null) {
                        // Landscape mode: find the already loaded EventListFragment
                        EventListFragment listFragment = (EventListFragment)
                                getParentFragmentManager().findFragmentById(R.id.fragment_list_container);
                        if (listFragment != null) {
                            // Update the list fragment's date field
                            listFragment.setSelectedDate(formattedDate);
                        } else {
                            Toast.makeText(requireContext(), "List fragment not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Portrait mode: create a new EventListFragment, pass the selected date, and replace the current fragment
                        EventListFragment listFragment = new EventListFragment();
                        Bundle args = new Bundle();
                        args.putString("SELECTED_DATE", formattedDate);
                        listFragment.setArguments(args);
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_list_container, listFragment)
                                .addToBackStack(null) // So the user can navigate back
                                .commit();
                    }
                });

                // Add the date TextView to the row
                row.addView(tvDate);

                // Create a LinearLayout to list all events for this day
                LinearLayout eventsLayout = new LinearLayout(requireContext());
                eventsLayout.setOrientation(LinearLayout.VERTICAL);
                // For each event on this day, add a TextView (you can add a click listener if needed)
                for (Ip event : dailyEvents) {
                    TextView tvEvent = new TextView(requireContext());
                    tvEvent.setText("• " + event.getLocalName());
                    tvEvent.setPadding(8, 4, 8, 4);
                    tvEvent.setClickable(true);
                    tvEvent.setFocusable(true);
                    // (Optional) Set a click listener for individual event actions here if desired
                    eventsLayout.addView(tvEvent);
                }
                // Add the events layout to the table row
                row.addView(eventsLayout);

                // Add the completed row to the calendar table
                calendarTable.addView(row);
            }
        }

        /**
         * Filters a list of events for a specific date.
         *
         * @param events The full list of events.
         * @param date   The date to filter by.
         * @return A list of events that occur on the specified date.
         */
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
