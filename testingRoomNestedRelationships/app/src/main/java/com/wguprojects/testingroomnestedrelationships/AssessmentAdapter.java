package com.wguprojects.testingroomnestedrelationships;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentHolder> {

    private List<Entities.Assessment> allAssessment;
    private OnLongItemClickListener longItemClickListener;

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_item, parent, false);
        return new AssessmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentHolder holder, int position) {
        Entities.Assessment currentAssessment = allAssessment.get(position);

        Random random = new Random();
        holder.itemView.setBackgroundColor(Color.argb(255, random.nextInt(256),
                random.nextInt(256), random.nextInt(256)));

        holder.title.setText(currentAssessment.getAssessment_title());
        holder.type.setText(currentAssessment.getAssessment_type());
        holder.startDateTime.setText(currentAssessment.getAssessment_start_date().toString());
        holder.endDateTime.setText(currentAssessment.getAssessment_end_date().toString());


    }

    @Override
    public int getItemCount() {
        if (allAssessment != null)
            return allAssessment.size();
        else
            return 0;
    }

    public void setAllAssessment(List<RelationalEntities.CourseWithAssessments> allAssessment){
        this.allAssessment = allAssessment.get(0).assessments;
        notifyDataSetChanged();
    }

    class AssessmentHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView type;
        private TextView startDateTime;
        private TextView endDateTime;


        public AssessmentHolder(@NonNull View itemView) {
            super(itemView);

            AssessmentActivity assessmentActivity = new AssessmentActivity();

            title = itemView.findViewById(R.id.assessment_title);
            type = itemView.findViewById(R.id.assessment_type);
            startDateTime = itemView.findViewById(R.id.assessment_start);
            endDateTime = itemView.findViewById(R.id.assessment_end);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (longItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        longItemClickListener.onLongItemClick(allAssessment.get(position));
                        System.out.println("Checking long click working");
                        System.out.println("Adapter position is: " + position);

                        Bundle bundle = new Bundle();
                        bundle.putInt("assessment_id",allAssessment.get(position).getAssessment_id());

                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        AssessmentEditFragment assessmentEditFragment = new AssessmentEditFragment();
                        assessmentEditFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.assessment_scroll,
                                assessmentEditFragment).addToBackStack(null).commit();


                    }
                    return false;
                }
            });
        }
    }

    public interface OnLongItemClickListener{
        void onLongItemClick(Entities.Assessment assessment);
    }

    public void setOnLongClickListener(OnLongItemClickListener listener){
        this.longItemClickListener = listener;
    }

    public Entities.Assessment getAssessmentAt(int position){
        return allAssessment.get(position);
    }
}
