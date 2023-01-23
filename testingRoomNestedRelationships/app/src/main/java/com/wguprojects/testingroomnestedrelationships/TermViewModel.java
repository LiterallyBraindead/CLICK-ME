package com.wguprojects.testingroomnestedrelationships;

import android.app.Application;
import android.media.MediaCasException;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<Entities.Schedule>> allSchedules;
    private LiveData<List<Entities.Term>> allTerms;
    private LiveData<List<Entities.Course>> allCourses;
    private LiveData<List<Entities.Instructor>> allInstructor;
    private LiveData<List<Entities.Assessment>> allAssessments;

    private LiveData<List<RelationalEntities.TermWithCourses>> allTermWithCourses;
    private LiveData<List<RelationalEntities.CourseWithAssessments>> allCourseWithAssessments;


    public TermViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
        allSchedules = repository.getAllSchedules();
        allTerms = repository.getAllTerms();
        allCourses = repository.getAllCourses();
        allInstructor = repository.getAllInstructors();
        allAssessments = repository.getAllAssessments();

        allTermWithCourses = repository.getAllTermWithCourses();
        allCourseWithAssessments = repository.getAllCourseWithAssessments();

    }

    public LiveData<List<Entities.Schedule>> getAllSchedules(){
        return allSchedules;
    }
    public void insertSchedule(Entities.Schedule schedule){
        repository.insertSchedule(schedule);
    }
    public void updateSchedule(Entities.Schedule schedule){
        repository.updateSchedule(schedule);
    }
    public void deleteSchedule(Entities.Schedule schedule){
        repository.deleteSchedule(schedule);
    }


    public LiveData<List<Entities.Term>> getAllTerms(){
        return allTerms;
    }
    public void insertTerm(Entities.Term term){
        repository.insertTerm(term);
    }
    public void updateTerm(Entities.Term term){
        repository.updateTerm(term);
    }
    public void deleteTerm(Entities.Term term){
        repository.deleteTerm(term);
    }

    public LiveData<List<Entities.Course>> getAllCourses(){
        return allCourses;
    }

    public void insertCourse(Entities.Course course){
        repository.insertCourse(course);
    }
    public void updateCourse(Entities.Course course){
        repository.updateCourse(course);
    }
    public void deleteCourse(Entities.Course course){
        repository.deleteCourse(course);
    }

    public LiveData<List<Entities.Instructor>> getAllInstructor(){
        return allInstructor;
    }
    public void insertInstructor(Entities.Instructor instructor){
        repository.insertInstructor(instructor);
    }
    public void updateInstructor(Entities.Instructor instructor){
        repository.updateInstructor(instructor);
    }
    public void deleteInstructor(Entities.Instructor instructor){
        repository.deleteInstructor(instructor);
    }

    public LiveData<List<Entities.Assessment>> getAllAssessments() {
        return allAssessments;
    }
    public void insertAssessment(Entities.Assessment assessment){
        repository.insertAssessment(assessment);
    }
    public void updateAssessment(Entities.Assessment assessment){
        repository.updateAssessment(assessment);
    }
    public void deleteAssessment(Entities.Assessment assessment){
        repository.deleteAssessment(assessment);
    }


    public LiveData<List<RelationalEntities.TermWithCourses>> getAllTermWithCourses(){
        return  allTermWithCourses;
    }
    public LiveData<List<RelationalEntities.CourseWithAssessments>> getAllCourseWithAssessments(){
        return allCourseWithAssessments;
    }



}
