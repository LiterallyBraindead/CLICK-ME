package com.wguprojects.testingroomnestedrelationships;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AssessmentAddFragment extends Fragment {

    private TimePicker startTime;
    private TimePicker endTime;
    private DatePicker startDate;
    private DatePicker endDate;
    private Spinner assessmentType;
    private EditText assessmentTitle;

    private Button saveButton;
    private Button cancelButton;

    private TermViewModel termViewModel;

    public AssessmentAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assessment_add, container, false);

        startTime = view.findViewById(R.id.start_time);
        endTime = view.findViewById(R.id.end_time);
        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        assessmentType = view.findViewById(R.id.assessment_type);
        assessmentTitle = view.findViewById(R.id.title_edit);

        saveButton = view.findViewById(R.id.button_save);
        cancelButton = view.findViewById(R.id.button_cancel);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    int startDay = startDate.getDayOfMonth();
                    int startMonth = startDate.getMonth() + 1;
                    int startYear = startDate.getYear();
                    int startTimeHour = startTime.getHour();
                    int startTimeMinute = startTime.getMinute();

                    LocalDateTime ldtStart = LocalDateTime.of(startYear, startMonth, startDay, startTimeHour, startTimeMinute);

                    int endDay = endDate.getDayOfMonth();
                    int endMonth = endDate.getMonth() + 1;
                    int endYear = endDate.getYear();
                    int endTimeHour = endTime.getHour();
                    int endTimeMinute = endTime.getMinute();

                    LocalDateTime ldtEnd = LocalDateTime.of(endYear, endMonth, endDay, endTimeHour,endTimeMinute);

                    String title = assessmentTitle.getText().toString();
                    String type = assessmentType.getSelectedItem().toString();

                    Entities.Assessment addAssessment = new Entities.Assessment(title, type, ldtStart, ldtEnd, Repository.courseId);

                    termViewModel.insertAssessment(addAssessment);

                    getActivity().getSupportFragmentManager().popBackStack();

                }catch (NullPointerException e) {
                    Toast toast = Toast.makeText(getContext(),"Please input valid values in all fields!", Toast.LENGTH_SHORT );
                    toast.show();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });







        return view;
    }
}