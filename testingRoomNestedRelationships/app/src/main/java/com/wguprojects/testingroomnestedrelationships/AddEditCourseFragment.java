package com.wguprojects.testingroomnestedrelationships;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class AddEditCourseFragment extends Fragment {

    private TimePicker startTime;
    private DatePicker startDate;

    private TimePicker endTime;
    private DatePicker endDate;

    private EditText courseTitle;
    private EditText optionalNote;
    private Spinner instructorSpinner;
    private Spinner courseStatus;

    private Button buttonSave;
    private Button buttonCancel;

    TermViewModel termViewModel;

    String spinnerText;
    String statusText;

   private Entities.Course newCourse;
   private Entities.Course updatedCourse;
   int instructorId;

    public AddEditCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_course, container, false);


        startTime = view.findViewById(R.id.start_time);
        startDate = view.findViewById(R.id.start_date);
        endTime = view.findViewById(R.id.end_time);
        endDate = view.findViewById(R.id.end_date);
        courseTitle = view.findViewById(R.id.title_edit);
        optionalNote = view.findViewById(R.id.edit_optional_note);
        instructorSpinner = view.findViewById(R.id.spinner_instructor);
        courseStatus = view.findViewById(R.id.spinner_status);

        buttonSave = view.findViewById(R.id.button_save);
        buttonCancel = view.findViewById(R.id.button_cancel);


        Bundle bundle = this.getArguments();

        boolean addEdit = bundle.getBoolean("addEdit");
        int courseID = bundle.getInt("course_id");

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        List<Entities.Instructor> allInstructor = new ArrayList<>();
        termViewModel.getAllInstructor().observe(getViewLifecycleOwner(), instructors ->{
            allInstructor.addAll(instructors);
            System.out.println("allInstructor size in the termViewModel is: " + allInstructor.size());

            ArrayAdapter<Entities.Instructor> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item,
                    allInstructor);
            System.out.println("outside of termViewModel: " + allInstructor.size());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            instructorSpinner.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            instructorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    spinnerText = instructorSpinner.getSelectedItem().toString();
                    System.out.println(spinnerText);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            if(!addEdit){
                System.out.println("addEdit false");

                if (courseID >= 0){
                    termViewModel.getAllCourses().observe(getViewLifecycleOwner(), allCourses ->{
                        for(int i = 0; i < allCourses.size(); i++){
                            if(courseID == allCourses.get(i).getCourse_id()){
                                courseTitle.setText(allCourses.get(i).getCourse_title());
                                optionalNote.setText(allCourses.get(i).getOptional_note());

                                startTime.setHour(allCourses.get(i).getStart_date().getHour());
                                startTime.setMinute(allCourses.get(i).getStart_date().getMinute());
                                startDate.updateDate(allCourses.get(i).getStart_date().getYear(),
                                        allCourses.get(i).getStart_date().getMonthValue(), allCourses.get(i).getStart_date().getDayOfMonth());

                                endTime.setHour(allCourses.get(i).getEnd_date().getHour());
                                endTime.setMinute(allCourses.get(i).getEnd_date().getMinute());
                                endDate.updateDate(allCourses.get(i).getEnd_date().getYear(),
                                        allCourses.get(i).getEnd_date().getMonthValue(), allCourses.get(i).getEnd_date().getDayOfMonth());

                                instructorSpinner.setSelection(allCourses.get(i).getAssociated_instructor_id());

                                String[] statusArray = getResources().getStringArray(R.array.status_list);
                                ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(getActivity(), R.layout.spinner_item,
                                        statusArray);
                                adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                courseStatus.setAdapter(adapterStatus);
                                adapterStatus.notifyDataSetChanged();

                                int spinnerPosition = adapterStatus.getPosition(allCourses.get(i).getStatus());
                                courseStatus.setSelection(spinnerPosition);

                                courseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        statusText = courseStatus.getSelectedItem().toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                        }
                    });
                }

            }
        });




        buttonSave.setOnClickListener(new View.OnClickListener() {
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

                    String selectedInstructor = spinnerText;
                    System.out.println("The Spinner is set to: " + spinnerText);
                    termViewModel.getAllInstructor().observe(getViewLifecycleOwner(), allInstructor ->{
                        for(int i = 0; i < allInstructor.size(); i++){
                            if (allInstructor.get(i).getInstructor_name().equals(selectedInstructor)){
                                instructorId = allInstructor.get(i).getInstructor_id()-1;
                                System.out.println(instructorId);
                            }
                        }
                    });

                    newCourse = new Entities.Course(courseTitle.getText().toString(), ldtStart, ldtEnd,
                            statusText, optionalNote.getText().toString(),Repository.termID, instructorId);
                    updatedCourse = new Entities.Course(courseID, courseTitle.getText().toString(), ldtStart, ldtEnd,
                            statusText, optionalNote.getText().toString(),Repository.termID, instructorId);


                    System.out.println(courseTitle.getText().toString() + ldtStart + ldtEnd +
                            courseStatus.toString() + optionalNote.toString() + Repository.termID + instructorId);
                    if (courseTitle.getText().toString().isEmpty() || optionalNote.getText().toString().isEmpty()){
                        Toast toast = Toast.makeText(getContext(), "Please enter valid values into all fields!", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                }catch (NullPointerException e){
                    Toast toast = Toast.makeText(getContext(), "Please enter valid values into all fields!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                //Passed boolean saves if long click or fab was used to navigate to the fragment
                if (!addEdit) {
                    termViewModel.updateCourse(updatedCourse);
                }else{
                    termViewModel.insertCourse(newCourse);
                }

                try {
                    getActivity().getSupportFragmentManager().popBackStack();
                }catch (NullPointerException e){
                    //empty body
                }



            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    getActivity().getSupportFragmentManager().popBackStack();
                }catch (NullPointerException e){
                    //empty body
                }

            }
        });

        return view;
    }

}