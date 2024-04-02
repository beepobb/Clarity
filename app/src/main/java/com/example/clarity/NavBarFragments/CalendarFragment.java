package com.example.clarity.NavBarFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.example.clarity.R;
import com.example.clarity.adapters.CalendarEventAdapter;
import com.example.clarity.model.Event; // PLACEHOLDER for data source

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kotlin.jvm.functions.Function1;

/**
 * CalendarFragment handles all logic for Monthly view, which is the default view
 */
public class CalendarFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1"; private static final String ARG_PARAM2 = "param2";

    private String mParam1; private String mParam2;
    private TextView aabbcc;
    private RecyclerView monthlyRecyclerView;
    private RecyclerView agendaRecyclerView;
    private CalendarEventAdapter adapterMonthly;
    private CalendarEventAdapter adapterAgenda;
    private com.applandeo.materialcalendarview.CalendarView calendarView;
    private TextView monthLabelTextView;
    private List<Event> dataSource = new ArrayList<>(); // placeholder Event list for data source
    public enum CalendarDisplayState {MONTHLY_VIEW, AGENDA_VIEW}
    private CalendarDisplayState calendarDisplayState;
    private ImageView displayToggle; // to toggle between monthly view and agenda view
    private final String[] intToMonth = new String[] {"JANUARY", "FEBRUARY", "MARCH", "APRIL",
            "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "NOVEMBER", "DECEMBER"}; // to map integers to strings

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calendar.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // stores arguments passed to Fragment,
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.i("CalendarFragment", "onCreate run");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // get reference to all UI elements
        calendarView = view.findViewById(R.id.calendarView);
        aabbcc = view.findViewById(R.id.AABBCC);
        displayToggle = view.findViewById(R.id.displayToggle);
        monthlyRecyclerView = view.findViewById(R.id.monthlyRecycler);
        monthLabelTextView = view.findViewById(R.id.monthLabel);

        // other variables
        calendarDisplayState = CalendarDisplayState.MONTHLY_VIEW;

        // Initialize placeholder data source //
        Calendar upop = setDate(Calendar.getInstance(), 4, 4, 2024);
        Calendar ml = setDate(Calendar.getInstance(), 3, 4, 2024);
        Calendar cf = setDate(Calendar.getInstance(), 4, 4, 2024);
        Calendar le = setDate(Calendar.getInstance(), 5, 4, 2024);
        Calendar toilet = Calendar.getInstance(); // current date

        dataSource.add(new Event("UPOP", "1300-1500", "Think Tank 3", upop));
        dataSource.add(new Event("ML Workshop", "1100-1900", "Classroom 1", ml));
        dataSource.add(new Event("Career Fair", "1500-2000", "Student Centre", cf));
        dataSource.add(new Event("Lame Event", "1200-1300", "Somewhere", le));
        dataSource.add(new Event("Toilet Break", "1500-1501", "Toilet", toilet));

        // Fetch current date (for initialization purposes)
        Calendar today = Calendar.getInstance();
        monthLabelTextView.setText(intToMonth[today.get(Calendar.MONTH)] + " " + today.get(Calendar.YEAR));
        List<Event> eventsToday = new ArrayList<>();
        for (Event e: dataSource) {
            Calendar c = e.getDate();
            if (today.get(Calendar.YEAR) == c.get(Calendar.YEAR) &&
                    today.get(Calendar.MONTH) == c.get(Calendar.MONTH) &&
                    today.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)) {
                eventsToday.add(e);
            }
        }


        // Monthly View: RecyclerView and adapter
        monthlyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Linear Scroll
        adapterMonthly = new CalendarEventAdapter(getActivity(), eventsToday, calendarDisplayState);
        monthlyRecyclerView.setAdapter(adapterMonthly);

        // Agenda View: RecyclerView and adapter
        agendaRecyclerView = view.findViewById(R.id.agendaRecycler);
        agendaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Linear Scroll
        adapterAgenda = new CalendarEventAdapter(getActivity(), dataSource, CalendarDisplayState.AGENDA_VIEW);
        agendaRecyclerView.setAdapter(adapterAgenda);

        return view; // Inflate the layout for this fragment
    }

    /**
     * view lookups and attaching view listeners done here
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // onViewCreated is executed after onCreateView
        super.onViewCreated(view, savedInstanceState);
        Log.i("CalendarFragment", "onViewCreate");

        // set default view
        showMonthlyView();

        calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
            @Override
            public void onClick(@NonNull CalendarDay calendarDay) {
                Calendar selectedDate = calendarDay.getCalendar();
                int year = selectedDate.get(Calendar.YEAR);
                int month = selectedDate.get(Calendar.MONTH); // January is 0 (not 1)
                int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);
                aabbcc.setText(String.valueOf(dayOfMonth+"/"+ (month+1) +"/"+year));

                // Fetch events with given date
                List<Event> eventList = new ArrayList<>();
                for (Event e: dataSource) {
                    Calendar c = e.getDate();
                    if (year == c.get(Calendar.YEAR) &&
                            month == c.get(Calendar.MONTH) &&
                            dayOfMonth == c.get(Calendar.DAY_OF_MONTH)) {
                        eventList.add(e);
                    }
                }

                adapterMonthly.setData(eventList); // Update the RecyclerView
            }
        });

        // Update month label in purple header bar on calendar page change
        calendarView.setOnForwardPageChangeListener(new calendarPageChangeListener());
        calendarView.setOnPreviousPageChangeListener(new calendarPageChangeListener());

        displayToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CalendarFragment", "displayToggle");

                // Switch between Monthly view and Agenda view (Toggles visibility of View objects)
                switch (calendarDisplayState) {
                    case AGENDA_VIEW:
                        displayToggle.setImageResource(R.drawable.monthly_view);
                        calendarDisplayState = CalendarDisplayState.MONTHLY_VIEW;
                        showMonthlyView();
                        break;
                    case MONTHLY_VIEW:
                        displayToggle.setImageResource(R.drawable.agenda_view);
                        calendarDisplayState = CalendarDisplayState.AGENDA_VIEW;
                        showAgendaView();
                        break;
                }

                Log.d("displayToggle", "value is: " + calendarDisplayState);
            }
        });
    }

    // Helper functions:

    // sets date for Calendar object
    public Calendar setDate(Calendar date, int day, int month, int year) {
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1); // JANUARY is 0
        date.set(Calendar.DAY_OF_MONTH, day);
        Log.d("setDate", year+String.valueOf(date.get(Calendar.YEAR)));
        // seconds and milliseconds don't matter
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }
//    public void getDate() {
//        long date = calendarView.getDate();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
//        calendar.setTimeInMillis(date);
//        String selected_date = simpleDateFormat.format(calendar.getTime());
//        Toast.makeText(getActivity(), selected_date, Toast.LENGTH_SHORT).show();
//    }

    /**
     * Hides Agenda view UI then display Monthly view UI
     */
    public void showMonthlyView() {
        // hide Agenda view UI
        agendaRecyclerView.setVisibility(View.GONE);

        // show Monthly view UI
        calendarView.setVisibility(View.VISIBLE);
        monthlyRecyclerView.setVisibility(View.VISIBLE);
        aabbcc.setVisibility(View.VISIBLE);
    }

    /**
     * Hides Monthly view UI then display Agenda view UI
     */
    public void showAgendaView() {
        // hide Monthly view UI
        calendarView.setVisibility(View.GONE);
        monthlyRecyclerView.setVisibility(View.GONE);
        aabbcc.setVisibility(View.GONE);

        // show Agenda view UI
        agendaRecyclerView.setVisibility(View.VISIBLE);
    }

    // Listener for Calendar Page change (changing between months
    class calendarPageChangeListener implements OnCalendarPageChangeListener {
        @Override
        public void onChange() {
            Calendar c = calendarView.getCurrentPageDate();
            monthLabelTextView.setText(intToMonth[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR));
        }
    }
}




