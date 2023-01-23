package com.wguprojects.testingroomnestedrelationships;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

public class AllDao {

    @Dao
    public interface  ScheduleDao{

        @Query("SELECT * FROM schedule_table")
        LiveData<List<Entities.Schedule>> getAllSchedule();

        @Insert
        void insert(Entities.Schedule schedule);

        @Update
        void update(Entities.Schedule schedule);

        @Delete
        void delete(Entities.Schedule schedule);

    }

    @Dao
    public interface TermDao{

        @Query("SELECT * FROM term_table")
        LiveData<List<Entities.Term>> getAllTerms();

        @Insert
        void insert(Entities.Term term);

        @Update
        void update(Entities.Term term);

        @Insert
        void insertAllTerms(Entities.Term... users);

        @Delete
        void delete(Entities.Term term);

    }
    @Dao
    public interface CourseDao{

        @Query("SELECT * FROM course_table")
        LiveData<List<Entities.Course>> getAllCourses();

        @Insert
        void insertCourse(Entities.Course course);

        @Update
        void updateCourse(Entities.Course course);

        @Insert
        void insertAllCourses(Entities.Course... courses);

        @Delete
        void deleteCourse(Entities.Course course);

    }

    @Dao
    public interface InstructorDao{

        @Query("SELECT * FROM instructor_table")
        LiveData<List<Entities.Instructor>> getAllInstructors();

        @Insert
        void insertAllInstructor(Entities.Instructor... instructor);

        @Insert
        void insertInstructor(Entities.Instructor instructor);

        @Update
        void updateInstructor(Entities.Instructor instructor);

        @Delete
        void deleteInstructor(Entities.Instructor instructor);

    }

    @Dao
    public interface AssessmentDao{

        @Query("SELECT * FROM assessment_table")
        LiveData<List<Entities.Assessment>> getAllAssessment();

        @Update
        void updateAssessment(Entities.Assessment assessment);

        @Delete
        void deleteAssessment(Entities.Assessment assessment);

        @Insert
        void insertAssessment(Entities.Assessment assessment);

        @Insert
        void insertAllAssessment(Entities.Assessment... assessments);
    }

    @Dao
    public interface RelationalDao{
        @Transaction
        @Query("SELECT * FROM term_table WHERE term_id = :term_id")
        LiveData<List<RelationalEntities.TermWithCourses>> getTermsWithCourses(int term_id);

        @Transaction
        @Query("SELECT * FROM course_table WHERE course_id = :course_id")
        LiveData<List<RelationalEntities.CourseWithAssessments>> getCourseWithAssessments(int course_id);

//        @Transaction
//        @Query("SELECT * FROM course_table")
//        public List<RelationalEntities.InstructorAndCourses> getCoursesAndInstructors();
//
//        @Transaction
//        @Query("SELECT * FROM course_table")
//        public List<RelationalEntities.CourseWithAssessments> getCoursesWithAssessments();

    }
}
