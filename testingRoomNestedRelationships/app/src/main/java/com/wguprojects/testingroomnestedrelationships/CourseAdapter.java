package com.wguprojects.testingroomnestedrelationships;

import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {

    List<Entities.Course> allCourses = new ArrayList<>();
    private OnItemClickListener listener;
    private OnLongItemClickListener longItemClickListener;

    @NonNull
    @Override
    public CourseAdapter.CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new CourseHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseHolder holder, int position) {
        Entities.Course currentCourse = allCourses.get(position);

        Random random = new Random();
        holder.itemView.setBackgroundColor(Color.argb(255, random.nextInt(256),
                random.nextInt(256), random.nextInt(256)));

        holder.textViewTitle.setText(currentCourse.getCourse_title());
        holder.textViewStartDate.setText(String.valueOf(currentCourse.getStart_date()));
        holder.textViewEndDate.setText(String.valueOf(currentCourse.getEnd_date()));

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("button clicked");

                Bundle bundle = new Bundle();
                bundle.putInt("course_id", currentCourse.getCourse_id());

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                CourseInfoFragment courseInfoFragment = new CourseInfoFragment();

                courseInfoFragment.setArguments(bundle);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.course_container,
                        courseInfoFragment).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return allCourses.size();
    }

    public void setAllCourses(List<RelationalEntities.TermWithCourses> allCourses){

        if (allCourses != null){
            this.allCourses = allCourses.get(0).courses;
            notifyDataSetChanged();
        }

    }

    class CourseHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewStartDate;
        private TextView textViewEndDate;
        private Button button;

        public CourseHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.course_title);
            textViewStartDate = itemView.findViewById(R.id.course_start_date);
            textViewEndDate = itemView.findViewById(R.id.course_end_date);
            button = itemView.findViewById(R.id.button_more_info);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(allCourses.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    int position = getAdapterPosition();
                    if(longItemClickListener != null && position != RecyclerView.NO_POSITION){
                        longItemClickListener.onLongItemClick(allCourses.get(position));
                        Bundle bundle = new Bundle();
                        bundle.putInt("course_id", allCourses.get(position).getCourse_id());
                        AppCompatActivity activity = (AppCompatActivity)  view.getContext();
                        AddEditCourseFragment addEditCourseFragment = new AddEditCourseFragment();
                        addEditCourseFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.scroll_container,
                                addEditCourseFragment).addToBackStack(null).commit();
                    }
                    return true;
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(Entities.Course course);
    }

    public interface OnLongItemClickListener{
        void onLongItemClick(Entities.Course course);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener longItemClickListener){
        this.longItemClickListener = longItemClickListener;
    }

    public Entities.Course getCourseAt(int position){
        return allCourses.get(position);
    }
}
