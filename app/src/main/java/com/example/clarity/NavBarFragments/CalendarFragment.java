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

import com.example.clarity.R;
import com.example.clarity.adapters.CalendarEventAdapter;
import com.example.clarity.model.Event; // PLACEHOLDER for data source

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * CalendarFragment handles all logic for Monthly view, which is the default view
 */
public class CalendarFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1"; private static final String ARG_PARAM2 = "param2";

    private String mParam1; private String mParam2;
    private Calendar calendar;
    private CalendarView calendarView;
    private TextView aabbcc;
    private RecyclerView monthlyRecyclerView;
    private RecyclerView agendaRecyclerView;
    private List<Event> eventList = new ArrayList<>(); // placeholder Event list for data source
    public enum CalendarDisplayState {MONTHLY_VIEW, AGENDA_VIEW}
    private CalendarDisplayState calendarDisplayState;
    private ImageView displayToggle; // to toggle between monthly view and agenda view

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

        // RECYCLER VIEW SET-UP
        // Placeholder for data source
        Calendar upop = Calendar.getInstance();
        setDate(upop, 2, 1, 2023);
        Calendar toilet = Calendar.getInstance();
        setDate(toilet, 3, 1, 2023);

        eventList.add(new Event("UPOP", "1300-1500", "Think Tank 3", upop));
        eventList.add(new Event("ML Workshop", "1100-1900", "Classroom 1", upop));
        eventList.add(new Event("Career Fair", "1500-2000", "Student Centre", upop));
        eventList.add(new Event("Lame Event", "1200-1300", "Somewhere", upop));
        eventList.add(new Event("Toilet Break", "1500-1501", "Toilet", toilet));

        // get reference to all UI elements
        calendarView = view.findViewById(R.id.calendarView);
        aabbcc = view.findViewById(R.id.AABBCC);
        calendar = Calendar.getInstance(); // calendar is like datetime in python
        displayToggle = view.findViewById(R.id.displayToggle);
        monthlyRecyclerView = view.findViewById(R.id.recyclerView);

        // other variables
        calendarDisplayState = CalendarDisplayState.MONTHLY_VIEW;

        // Set up RecyclerView and adapter for monthly view
        monthlyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Linear Scroll
        CalendarEventAdapter adapterMonthly = new CalendarEventAdapter(getActivity(), eventList, calendarDisplayState);
        monthlyRecyclerView.setAdapter(adapterMonthly);

        // Set up RecyclerView and adapter for agenda view
        agendaRecyclerView = view.findViewById(R.id.agendaRecycler);
        agendaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Linear Scroll
        CalendarEventAdapter adapterAgenda = new CalendarEventAdapter(getActivity(), eventList, CalendarDisplayState.AGENDA_VIEW);
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
        setDate(calendar,1,1,2023);
        calendarView.setDate(calendar.getTimeInMillis());
        showMonthlyView();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Log.d("CalendarFragment", "date set");

                aabbcc.setText(String.valueOf(dayOfMonth+"/"+ (month + 1) +"/"+year));
                Calendar selectedDate = Calendar.getInstance();
//                Log.d("CalendarFragment", dayOfMonth + "/" + (month + 1) + "/" + year);
                setDate(selectedDate, dayOfMonth, month+1, year);
//                Log.d("CalendarFragment", String.valueOf(selectedDate.get(Calendar.DAY_OF_WEEK)));
                List<Event> subEventList = new ArrayList<>();

//                Log.d("CalendarFragment", year + String.valueOf(selectedDate.get(Calendar.YEAR)));
                for (Event e: eventList) {
//                    Log.d("CalendarFragment", e.getName() +e.getDate().get(Calendar.YEAR)+e.getDate().get(Calendar.MONTH)+e.getDate().get(Calendar.DAY_OF_MONTH));

//                    Log.d("CalendarFragment", selectedDate.toString());

                    if (selectedDate.get(Calendar.YEAR) == e.getDate().get(Calendar.YEAR) &&
                            selectedDate.get(Calendar.MONTH) == e.getDate().get(Calendar.MONTH) &&
                            selectedDate.get(Calendar.DAY_OF_MONTH) == e.getDate().get(Calendar.DAY_OF_MONTH)) {
                        subEventList.add(e);
                    }
                }

                // Set up RecyclerView and adapter
                monthlyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Linear Scroll
                CalendarEventAdapter adapter = new CalendarEventAdapter(getActivity(), subEventList, calendarDisplayState);
                Log.d("CalendarFragment", adapter.getEventList());
                monthlyRecyclerView.setAdapter(adapter);
            }
        });

        displayToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CalendarFragment", "displayToggle");

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
    public void setDate(Calendar date, int day, int month, int year) {
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1); // JANUARY is 0
        date.set(Calendar.DAY_OF_MONTH, day);
        Log.d("setDate", year+String.valueOf(date.get(Calendar.YEAR)));
        // seconds and milliseconds don't matter
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
    }
    public void getDate() {
        long date = calendarView.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        calendar.setTimeInMillis(date);
        String selected_date = simpleDateFormat.format(calendar.getTime());
        Toast.makeText(getActivity(), selected_date, Toast.LENGTH_SHORT).show();
    }

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

        // show Agenda view UIt
        agendaRecyclerView.setVisibility(View.VISIBLE);
    }
}

