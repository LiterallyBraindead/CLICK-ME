package com.wguprojects.testingroomnestedrelationships;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermHolder> {

    private List<Entities.Term> allTerms = new ArrayList<>();
    private OnItemClickListener listener;
    private OnLongItemClickListener longItemClickListener;

    @NonNull
    @Override
    public TermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_item, parent, false);

        return new TermHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermHolder holder, int position) {
        Entities.Term currentTerm = allTerms.get(position);
        Random random = new Random();

        holder.itemView.setBackgroundColor(Color.argb(255, random.nextInt(256),
                random.nextInt(256), random.nextInt(256)));
        holder.textViewTitle.setText(currentTerm.getTerm_title());
        holder.textViewStart.setText(String.valueOf(currentTerm.getTerm_start_date().plusMonths(1)));
        holder.textViewEnd.setText(String.valueOf(currentTerm.getTerm_end_date().plusMonths(1)));
    }

    @Override
    public int getItemCount() {
        return allTerms.size();
    }

    public void setTerms(List<Entities.Term> allTerms){
        this.allTerms = allTerms;
        notifyDataSetChanged();
    }

    class TermHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewStart;
        private TextView textViewEnd;

        public TermHolder(View itemView){
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.term_title);
            textViewStart = itemView.findViewById(R.id.term_start_date);
            textViewEnd = itemView.findViewById(R.id.term_end_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(allTerms.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (longItemClickListener != null && position != RecyclerView.NO_POSITION){
                        longItemClickListener.onLongItemClick(allTerms.get(position));
                        Bundle bundle = new Bundle();
                        bundle.getInt("term_id", allTerms.get(position).getTerm_id());

                        AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                        AddEditTermFragment addEditTermFragment = new AddEditTermFragment();
                        addEditTermFragment.setArguments(bundle);
                        appCompatActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_scroll_container, addEditTermFragment)
                                .addToBackStack(null).commit();
                    }
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Entities.Term term);
    }

    public interface OnLongItemClickListener{
        void onLongItemClick(Entities.Term term);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setOnLongItemClickListener(TermAdapter.OnLongItemClickListener longItemClickListener){
        this.longItemClickListener = longItemClickListener;
    }

    public Entities.Term getTermAt(int position){
        return allTerms.get(position);
    }
}
