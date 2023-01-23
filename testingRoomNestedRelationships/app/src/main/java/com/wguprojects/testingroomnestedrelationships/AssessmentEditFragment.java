package com.wguprojects.testingroomnestedrelationships;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;


public class AssessmentEditFragment extends Fragment {


    private TimePicker startTime;
    private TimePicker endTime;
    private DatePicker startDate;
    private DatePicker endDate;
    private Spinner assessmentType;
    private TextView assessmentTitle;

    private Button saveButton;
    private Button cancelButton;

    private TermViewModel termViewModel;



    public AssessmentEditFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle bundle = this.getArguments();

        View view = inflater.inflate(R.layout.fragment_assessment_edit, container, false);

        startTime = view.findViewById(R.id.start_time);
        endTime = view.findViewById(R.id.end_time);
        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        assessmentType = view.findViewById(R.id.assessment_type);
        assessmentTitle = view.findViewById(R.id.title_edit);

        saveButton = view.findViewById(R.id.button_save);
        cancelButton = view.findViewById(R.id.button_cancel);

        int assessment_id = bundle.getInt("assessment_id");

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        termViewModel.getAllAssessments().observe(getViewLifecycleOwner(), assessments -> {
            for (int i = 0; i < assessments.size(); i++){
                if (assessments.get(i).getAssessment_id() == assessment_id){
                    startTime.setHour(assessments.get(i).getAssessment_start_date().getHour());
                    startTime.setMinute(assessments.get(i).getAssessment_start_date().getMinute());

                    endTime.setHour(assessments.get(i).getAssessment_end_date().getHour());
                    endTime.setMinute(assessments.get(i).getAssessment_end_date().getMinute());

                    startDate.updateDate(assessments.get(i).getAssessment_start_date().getYear(),
                            assessments.get(i).getAssessment_start_date().getMonthValue() -1,
                            assessments.get(i).getAssessment_start_date().getDayOfMonth());

                    endDate.updateDate(assessments.get(i).getAssessment_end_date().getYear(),
                            assessments.get(i).getAssessment_end_date().getMonthValue() -1,
                            assessments.get(i).getAssessment_end_date().getDayOfMonth());

                    Resources res = getResources();
                    String[] types = res.getStringArray(R.array.type_list);

                    for (int j = 0; j < types.length; j++){
                        if (assessments.get(i).getAssessment_type().equals(types[j])){
                            assessmentType.setSelection(j);
                        }
                    }

                    assessmentTitle.setText(assessments.get(i).getAssessment_title());

                }
            }

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
                        addAssessment.setAssessment_id(assessment_id);
                        termViewModel.updateAssessment(addAssessment);

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
        });

        return view;
    }
}