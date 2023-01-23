package com.wguprojects.testingroomnestedrelationships;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class CourseInfoFragment extends Fragment {

    private TextView courseInfo;
    private TextView status;
    private TextView instructor;
    private TextView optionalNote;
    private Button shareButton;

    private TermViewModel termViewModel;

    private String instructorInfo;

    public CourseInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle bundle = this.getArguments();

        View view = inflater.inflate(R.layout.fragment_course_info_fragement, container, false);

        courseInfo = view.findViewById(R.id.course_info_title);
        status = view.findViewById(R.id.tv_course_status);
        instructor = view.findViewById(R.id.tv_course_instructor);
        optionalNote = view.findViewById(R.id.tv_course_note);
        shareButton = view.findViewById(R.id.share_button);

        int course_id = bundle.getInt("course_id");
        System.out.println("Course Info Fragment is displaying the information for course: " + course_id);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        termViewModel.getAllTermWithCourses().observe(getViewLifecycleOwner(), termWithCourses -> {

            for (RelationalEntities.TermWithCourses term : termWithCourses){
                for (Entities.Course course : term.courses){
                    if (course.getCourse_id() == course_id){
                        courseInfo.setText(course.getCourse_title());
                        status.setText(course.getStatus());

                        termViewModel.getAllInstructor().observe(getViewLifecycleOwner(),instructors -> {
                            for (Entities.Instructor instructor1 : instructors){
                                if (course.getAssociated_instructor_id() == instructor1.getInstructor_id()){
                                    instructorInfo = instructor1.getInstructor_name() + " \n" +
                                            instructor1.getInstructor_phone() + " \n" +
                                            instructor1.getInstructor_email();

                                    instructor.setText(instructorInfo);
                                    System.out.println(instructorInfo);
                                }


                            }
                        });

                        optionalNote.setText(course.getOptional_note());

                    }
                }

            }
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, optionalNote.getText().toString());
                        //emailIntent.setType("message/rfc822");
                        emailIntent.setData(Uri.parse("mailto:"));
                        startActivity(emailIntent);
                    }catch (NullPointerException e){
                        Toast.makeText(getContext(), "The application does not recognize any email applications on your device!",
                                Toast.LENGTH_SHORT).show();

                    }
                }
            });

        });



        return view;
    }

}