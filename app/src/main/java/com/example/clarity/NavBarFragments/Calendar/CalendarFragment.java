package com.example.clarity.NavBarFragments.Calendar;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.example.clarity.MainActivity;
import com.example.clarity.MyDataRepository;
import com.example.clarity.R;
import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.repository.RestRepo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * CalendarFragment handles all logic for Monthly view, which is the default view
 */
public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1"; private static final String ARG_PARAM2 = "param2";

    private String mParam1; private String mParam2;
    private RecyclerView monthlyRecyclerView;
    private RecyclerView agendaRecyclerView;
    private CalendarEventAdapter adapterMonthly;
    private CalendarEventAdapter adapterAgenda;
    private com.applandeo.materialcalendarview.CalendarView calendarView;
    private TextView monthLabelTextView;
    private Calendar selectedDate;
    private RestRepo db;
    public enum CalendarDisplayState {MONTHLY_VIEW, AGENDA_VIEW}
    private CalendarDisplayState calendarDisplayState;
    private ImageView displayToggle; // to toggle between monthly view and agenda view
    private String[] intToMonth = {"JANUARY", "FEBRUARY", "MARCH", "APRIL",
            "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"}; // Convert integer representation of month to String
    private PreferenceUtils prefUtils;
    private MyDataRepository dataRepo;

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

        // Fetch database (RestRepo instance)
        Activity activity = getActivity();
        if (activity != null) {
            // Example: Accessing activity's method
            db = ((MainActivity) activity).database;
        }

        // Get Data Manager instances
        prefUtils = PreferenceUtils.getInstance(getActivity()); // manages local storage
        dataRepo = MyDataRepository.getInstance(); // manages list of saved events (Post objects)

        // Other attributes
        calendarDisplayState = CalendarDisplayState.MONTHLY_VIEW;
        selectedDate = Calendar.getInstance(); // get current date

        Log.i(TAG, "onCreate: finished");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // get reference to all UI elements
        calendarView = view.findViewById(R.id.calendarView);
        displayToggle = view.findViewById(R.id.displayToggle);
        monthlyRecyclerView = view.findViewById(R.id.monthlyRecycler);
        agendaRecyclerView = view.findViewById(R.id.agendaRecycler);
        monthLabelTextView = view.findViewById(R.id.monthLabel);

        // Set up RecyclerViews (Monthly and Agenda)
        monthlyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Linear Scroll
        adapterMonthly = new CalendarEventAdapter(getActivity(), calendarDisplayState);
        monthlyRecyclerView.setAdapter(adapterMonthly);

        // Agenda View: RecyclerView and adapter
        agendaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Linear Scroll
        adapterAgenda = new CalendarEventAdapter(getActivity(), CalendarDisplayState.AGENDA_VIEW);
        agendaRecyclerView.setAdapter(adapterAgenda);

        // Initialize month label
        monthLabelTextView.setText(intToMonth[selectedDate.get(Calendar.MONTH)] + " " + selectedDate.get(Calendar.YEAR));

        Log.i(TAG, "onCreateView: finished");
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
        // onViewCreated is executed after onCreateView (after Views have been set up)
        super.onViewCreated(view, savedInstanceState);

        // set default view (monthly)
        showMonthlyView();

        // Set up observer for saved events (savedEventsLiveData from dataRepo)
        // When there is a change in the saved events list (e.g. Posts removed, etc), update the UI
        dataRepo.getSavedEventsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                Log.d(TAG, "savedEventsLiveData triggered: refresh UI");
                // Update UI
                updateMonthlyRecyclerView();
                updateAgendaRecyclerView();

                // Adds graphical element on days with saved events
                List<CalendarDay> calendarDays = new ArrayList<>();
                for (Post post: dataRepo.getSavedEvents()) {
                    CalendarDay calendarDay = new CalendarDay(post.getEventStart());
                    calendarDay.setBackgroundResource(R.drawable.calendar_dot_2);
                    calendarDays.add(calendarDay);
                }
                calendarView.setCalendarDays(calendarDays);
            }
        });

        // Load in saved posts from data base
        db.getPostsRequest(prefUtils.getCalendarPostIdsArrayList(), new RestRepo.RepositoryCallback<ArrayList<Post>>() {
            @Override
            public void onComplete(ArrayList<Post> result) {
                if (result == null) { // no Posts fetched
                    result = new ArrayList<>(); // set result to be an empty list rather than null
                }

                // TODO: Perhaps sort result (by start date) before storing?
                dataRepo.loadSavedEventsOnWorkerThread(result);
                Log.d(TAG, "List of saved events pulled from database");

                // Note: we cannot directly update UI Views here as onComplete will be executed on a worker thread
                // We instead trigger observers that will update the UI in the main thread.
            }
        });

        // Listener for when you click on a day on the calendar
        // Only show events in that
        calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
            @Override
            public void onClick(@NonNull CalendarDay calendarDay) {
                selectedDate = calendarDay.getCalendar();
                updateMonthlyRecyclerView(); // Only display events on selected day
            }
        });

        // Update month label in purple header bar on calendar page change
        calendarView.setOnForwardPageChangeListener(new calendarPageChangeListener());
        calendarView.setOnPreviousPageChangeListener(new calendarPageChangeListener());

        // For switching between Monthly and Agenda view (toggles visibility)
        displayToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "displayToggle");

                // Switch between Monthly view and Agenda view (Toggles visibility of View objects)
                switch (calendarDisplayState) {
                    case AGENDA_VIEW: // Switch from agenda to monthly view
                        calendarDisplayState = CalendarDisplayState.MONTHLY_VIEW;
                        showMonthlyView();
                        break;
                    case MONTHLY_VIEW: // Switch from monthly to agenda view
                        calendarDisplayState = CalendarDisplayState.AGENDA_VIEW;
                        showAgendaView();
                        break;
                }

                Log.d("displayToggle", "Switched to: " + calendarDisplayState);
            }
        });
    }

    //***Helper functions***//

    private void updateMonthlyRecyclerView() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH); // January is 0 (not 1)
        int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

        // Only display Events on selected date
        List<Post> eventList = new ArrayList<>();
        for (Post p: dataRepo.getSavedEvents()){
            Calendar c = p.getEventStart();
            if (year == c.get(Calendar.YEAR) &&
                    month == c.get(Calendar.MONTH) &&
                    dayOfMonth == c.get(Calendar.DAY_OF_MONTH)) {
                eventList.add(p);
            }
        }

        eventList.sort(new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o1.getEventStart().compareTo(o2.getEventStart());
            }
        });

        adapterMonthly.updateEventList(eventList);

    }

    private void updateAgendaRecyclerView() {
        List<Post> eventList = dataRepo.getSavedEvents();
        eventList.sort(new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o1.getEventStart().compareTo(o2.getEventStart());
            }
        });
        adapterAgenda.updateEventList(Objects.requireNonNull(eventList));
    }

    // sets date for Calendar object
    public static Calendar setDate(Calendar date, int day, int month, int year) {
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1); // JANUARY is 0
        date.set(Calendar.DAY_OF_MONTH, day);
        Log.d("setDate", year+String.valueOf(date.get(Calendar.YEAR)));
        // seconds and milliseconds don't matter
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }

    /**
     * Hides Agenda view UI then display Monthly view UI
     */
    private void showMonthlyView() {
        // hide Agenda view UI
        agendaRecyclerView.setVisibility(View.GONE);

        // show Monthly view UI
        displayToggle.setImageResource(R.drawable.monthly_view);
        calendarView.setVisibility(View.VISIBLE);
        monthlyRecyclerView.setVisibility(View.VISIBLE);
        monthLabelTextView.setVisibility(View.VISIBLE);
    }

    /**
     * Hides Monthly view UI then display Agenda view UI
     */
    private void showAgendaView() {
        // hide Monthly view UI
        calendarView.setVisibility(View.GONE);
        monthlyRecyclerView.setVisibility(View.GONE);
        monthLabelTextView.setVisibility(View.GONE);

        // show Agenda view UI
        displayToggle.setImageResource(R.drawable.agenda_view);
        agendaRecyclerView.setVisibility(View.VISIBLE);
    }

    // Listener for Calendar Page change (changing between months
    private class calendarPageChangeListener implements OnCalendarPageChangeListener {
        @Override
        public void onChange() {
            Calendar c = calendarView.getCurrentPageDate();
            monthLabelTextView.setText(intToMonth[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR));
        }
    }
}




