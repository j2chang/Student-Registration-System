package edu.virginia.cs.hw4;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Transcript {
    Student student;
    Map<Course, Grade> courseHistory;

    public Transcript(Student student) {
        this.student = student;
        courseHistory = new HashMap<>();
    }

    public void addCourseGrade(Course course, Grade grade){
        courseHistory.put(course, grade);
    }

    public boolean containsCourse(Course course) {
        return courseHistory.containsKey(course);
    }

    public Set<Map.Entry<Course, Grade>> getCourseHistory(){
        return courseHistory.entrySet();
    }

    public boolean isEmpty(){
        return courseHistory.isEmpty();
    }

    public Set<Course> getCourses(){
        return courseHistory.keySet();
    }

    public Grade getCourseGrade(Course course){
        return courseHistory.get(course);
    }
}
