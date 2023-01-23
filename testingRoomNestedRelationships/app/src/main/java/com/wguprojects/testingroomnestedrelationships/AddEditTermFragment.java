package com.wguprojects.testingroomnestedrelationships;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;


public class AddEditTermFragment extends Fragment {

    private TimePicker startTime;
    private TimePicker endTime;
    private DatePicker startDate;
    private DatePicker endDate;

    private EditText termEditText;
    private Button saveButton;
    private Button cancelButton;

    TermViewModel termViewModel;

    private Entities.Term newTerm;
    private Entities.Term updatedTerm;

    public AddEditTermFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_term, container, false);

        startTime = view.findViewById(R.id.term_start_time);
        startDate = view.findViewById(R.id.term_start_date);
        endTime = view.findViewById(R.id.term_end_time);
        endDate = view.findViewById(R.id.term_end_date);
        termEditText = view.findViewById(R.id.edit_term_title);
        saveButton = view.findViewById(R.id.button_save);
        cancelButton = view.findViewById(R.id.button_cancel);


        Bundle bundle = this.getArguments();
        boolean addEdit = bundle.getBoolean("addEdit");
        int termID = bundle.getInt("term_id");
        System.out.println("termID: " + termID);
        System.out.println(addEdit);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        termViewModel.getAllTerms().observe(getViewLifecycleOwner(), allTerms ->{

            if(!addEdit){
                System.out.println("addEdit false");
                    for (int i = 0; i < allTerms.size(); i++){
                        if (Repository.termID == allTerms.get(i).getTerm_id()){
                            termEditText.setText(allTerms.get(i).getTerm_title());
                            System.out.println("THE TERM TITLE IS: " + allTerms.get(i).getTerm_title());

                            startTime.setMinute(allTerms.get(i).getTerm_start_date().getMinute());
                            startTime.setHour(allTerms.get(i).getTerm_start_date().getHour());
                            startDate.updateDate(allTerms.get(i).getTerm_start_date().getYear(),
                                    allTerms.get(i).getTerm_start_date().getMonthValue(),
                                    allTerms.get(i).getTerm_start_date().getDayOfMonth());

                            endTime.setMinute(allTerms.get(i).getTerm_end_date().getMinute());
                            endTime.setHour(allTerms.get(i).getTerm_end_date().getHour());
                            endDate.updateDate(allTerms.get(i).getTerm_end_date().getYear(),
                                    allTerms.get(i).getTerm_end_date().getMonthValue() ,
                                    allTerms.get(i).getTerm_end_date().getDayOfMonth());
                        }
                    }
                }

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        String title = termEditText.getText().toString();

                        int startDay = startDate.getDayOfMonth();
                        int startMonth = startDate.getMonth();
                        int startYear = startDate.getYear();
                        int startTimeHour = startTime.getHour();
                        int startTimeMinute = startTime.getMinute();

                        LocalDateTime ldtStart = LocalDateTime.of(startYear, startMonth, startDay, startTimeHour, startTimeMinute);

                        int endDay = endDate.getDayOfMonth();
                        int endMonth = endDate.getMonth();
                        int endYear = endDate.getYear();
                        int endTimeHour = endTime.getHour();
                        int endTimeMinute = endTime.getMinute();

                        LocalDateTime ldtEnd = LocalDateTime.of(endYear, endMonth, endDay, endTimeHour,endTimeMinute);

                        newTerm = new Entities.Term(title,ldtStart, ldtEnd);
                        updatedTerm = new Entities.Term(Repository.termID, title,ldtStart, ldtEnd);
                    }catch (NullPointerException e){
                        Toast toast = Toast.makeText(getContext(), "Please enter valid values into all fields!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    //Passed boolean saves if long click or fab was used to navigate to the fragment
                    if (!addEdit) {
                        termViewModel.updateTerm(updatedTerm);
                    }else{
                        termViewModel.insertTerm(newTerm);
                    }

                    try {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }catch (NullPointerException e){
                        //empty body
                    }



                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        getActivity().getSupportFragmentManager().popBackStack();
                    }catch (NullPointerException e){
                        //empty catch body
                    }

                }
            });
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    String title = termEditText.getText().toString();

                    int startDay = startDate.getDayOfMonth();
                    int startMonth = startDate.getMonth();
                    int startYear = startDate.getYear();
                    int startTimeHour = startTime.getHour();
                    int startTimeMinute = startTime.getMinute();

                    LocalDateTime ldtStart = LocalDateTime.of(startYear, startMonth, startDay, startTimeHour, startTimeMinute);

                    int endDay = endDate.getDayOfMonth();
                    int endMonth = endDate.getMonth();
                    int endYear = endDate.getYear();
                    int endTimeHour = endTime.getHour();
                    int endTimeMinute = endTime.getMinute();

                    LocalDateTime ldtEnd = LocalDateTime.of(endYear, endMonth, endDay, endTimeHour,endTimeMinute);

                    newTerm = new Entities.Term(title,ldtStart, ldtEnd);
                    updatedTerm = new Entities.Term(Repository.termID, title,ldtStart, ldtEnd);
                }catch (NullPointerException e){
                    Toast toast = Toast.makeText(getContext(), "Please enter valid values into all fields!", Toast.LENGTH_SHORT);
                    toast.show();
                }

                //Passed boolean saves if long click or fab was used to navigate to the fragment
                if (!addEdit) {
                    termViewModel.updateTerm(updatedTerm);
                }else{
                    termViewModel.insertTerm(newTerm);
                }

                try {
                    getActivity().getSupportFragmentManager().popBackStack();
                }catch (NullPointerException e){
                    //empty body
                }



            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    getActivity().getSupportFragmentManager().popBackStack();
                }catch (NullPointerException e){
                    //empty catch body
                }

            }
        });


        return view;
    }
}