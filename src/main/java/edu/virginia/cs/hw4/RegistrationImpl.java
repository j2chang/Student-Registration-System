package edu.virginia.cs.hw4;

import java.time.DayOfWeek;
import java.util.List;

public class RegistrationImpl implements Registration {
    //TODO: Implement class
    CourseCatalog courseCatalog;

    @Override
    public CourseCatalog getCourseCatalog() {
        return courseCatalog;
    }
    @Override
    public void setCourseCatalog(CourseCatalog courseCatalog) {
        this.courseCatalog = courseCatalog;
    }
    @Override
    public boolean isEnrollmentFull(Course course) {
        if(course.getCurrentEnrollmentSize() >= course.getEnrollmentCap()){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public boolean isWaitListFull(Course course) {
        return course.getCurrentWaitListSize() >= course.getWaitListCap();
    }
    @Override
    public Course.EnrollmentStatus getEnrollmentStatus(Course course) {
        return course.getEnrollmentStatus();
    }
    @Override
    public boolean areCoursesConflicted(Course first, Course second) {
        int firstMeetingHour = first.getMeetingStartTimeHour();
        int firstMeetingMinute = first.getMeetingStartTimeMinute();
        int firstMeetingStart = firstMeetingHour * 60 + firstMeetingMinute;
        int firstMeetingEnd = firstMeetingStart + first.getMeetingDurationMinutes();
        int secondMeetingHour = second.getMeetingStartTimeHour();
        int secondMeetingMinute = second.getMeetingStartTimeMinute();
        int secondMeetingStart = secondMeetingHour * 60 + secondMeetingMinute;
        int secondMeetingEnd = secondMeetingStart + second.getMeetingDurationMinutes();
        List <DayOfWeek> firstDays = first.getMeetingDays();
        List <DayOfWeek> secondDays = second.getMeetingDays();
        for (DayOfWeek day: firstDays) {
            if (secondDays.contains(day)) {
                if (firstMeetingStart < secondMeetingStart && secondMeetingStart < firstMeetingEnd
                    || secondMeetingStart < firstMeetingStart && firstMeetingStart < secondMeetingEnd) {
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public boolean hasConflictWithStudentSchedule(Course course, Student student) {
        return course.isStudentEnrolled(student);
    }

    @Override
    public boolean hasStudentMeetsPrerequisites(Student student, List<Prerequisite> prerequisites) {
        for (Prerequisite prereq: prerequisites) {
            if (!student.meetsPrerequisite(prereq)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public RegistrationResult registerStudentForCourse(Student student, Course course) {
        if (isEnrollmentFull(course) && isWaitListFull(course)){
            return RegistrationResult.COURSE_FULL;
        }
        if (!hasStudentMeetsPrerequisites(student, course.getPrerequisites())){
            return RegistrationResult.PREREQUISITE_NOT_MET;
        }
        if (hasConflictWithStudentSchedule(course, student)){
            return RegistrationResult.SCHEDULE_CONFLICT;
        }
        switch (course.getEnrollmentStatus()) {
            case OPEN:
                course.addStudentToEnrolled(student);
                if (isEnrollmentFull(course)){
                    course.setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
                }
                return RegistrationResult.ENROLLED;
            case WAIT_LIST:
                course.addStudentToWaitList(student);
                if (isWaitListFull(course)){
                    course.setEnrollmentStatus(Course.EnrollmentStatus.CLOSED);
                }
                return RegistrationResult.WAIT_LISTED;
            default:
                return RegistrationResult.COURSE_CLOSED;
        }
    }

    @Override
    public void dropCourse(Student student, Course course) {
        if (course.isStudentEnrolled(student)) {
            course.removeStudentFromEnrolled(student);
            if (course.getEnrollmentStatus() == Course.EnrollmentStatus.WAIT_LIST) {
                Student newStudent = course.getFirstStudentOnWaitList();
                course.addStudentToEnrolled(newStudent);
            }
        }
        else if (course.isStudentWaitListed(student)) {
            course.removeStudentFromWaitList(student);
        }
        else {
            throw new IllegalArgumentException("Student is not in the course");
        }
        if (course.getEnrollmentStatus() == Course.EnrollmentStatus.CLOSED) {
            course.setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
        }
        else if (course.isWaitListEmpty() && course.getCurrentEnrollmentSize() < course.getEnrollmentCap()) {
            course.setEnrollmentStatus(Course.EnrollmentStatus.OPEN);
        }
    }
}
