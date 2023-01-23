package com.wguprojects.testingroomnestedrelationships;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import javax.security.auth.callback.Callback;

public class RelationalEntities {

    public static class TermWithCourses{
        @Embedded public Entities.Term term;
        @Relation(
                parentColumn = "term_id",
                entityColumn =  "associated_term_id"
        )
        public List<Entities.Course> courses;
    }

    public static class InstructorAndCourses{
        @Embedded public Entities.Instructor instructor;
        @Relation(
                parentColumn = "instructor_id",
                entityColumn = "associated_instructor_id"
        )
        public List<Entities.Course> courses;
    }

    public static class CourseWithAssessments{
        @Embedded public Entities.Course course;
        @Relation(
                parentColumn = "course_id",
                entityColumn = "associated_course_id"
        )
        public List<Entities.Assessment> assessments;
    }
}
