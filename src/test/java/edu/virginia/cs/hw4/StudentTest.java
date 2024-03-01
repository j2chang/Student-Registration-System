package edu.virginia.cs.hw4;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class StudentTest {

    private Course mockCourse;
    private Prerequisite testPrereq;
    private Transcript testTranscript;
    private Student student;

    @BeforeEach
    public void setup(){
        mockCourse = mock(Course.class);
        testTranscript = new Transcript(student);
        testPrereq = new Prerequisite(mockCourse, Grade.B);
        student = new Student(1, "Aaron", "wwr7nu@virginia.edu", testTranscript);

    }

    @Test
    public void testAddCourseGrade(){
        student.addCourseGrade(mockCourse, Grade.A);
        assertTrue(testTranscript.courseHistory.containsKey(mockCourse));
        assertEquals(testTranscript.courseHistory.get(mockCourse), Grade.A);
    }
    @Test
    public void testHasStudentTakenCourse(){
        student.addCourseGrade(mockCourse, Grade.A);
        assertTrue(student.hasStudentTakenCourse(mockCourse));
    }
    @Test
    public void testHasStudentTakenCourseFalse(){
        Course mockCourse2 = mock(Course.class);
        student.addCourseGrade(mockCourse, Grade.B);
        assertFalse(student.hasStudentTakenCourse(mockCourse2));
    }
    @Test
    public void testGetCourseGrade(){
        student.addCourseGrade(mockCourse, Grade.C);
        assertEquals(student.getCourseGrade(mockCourse), Grade.C);
    }
    @Test
    public void testGetCourseGradeException(){
        assertThrows(IllegalArgumentException.class, () ->
                student.getCourseGrade(mockCourse));
    }
    @Test
    public void testMeetsPrerequisite(){
        when(mockCourse.getDepartment()).thenReturn("CS");
        when(mockCourse.getCatalogNumber()).thenReturn(3140);
        when(testPrereq.course.getDepartment()).thenReturn("CS");
        when(testPrereq.course.getCatalogNumber()).thenReturn(3140);
        student.addCourseGrade(mockCourse, Grade.A);
        assertTrue(student.meetsPrerequisite(testPrereq));
    }
    @Test
    public void testMeetsPrerequisiteFail(){
        assertFalse(student.meetsPrerequisite(testPrereq));
    }
    @Test
    public void testGetGPA(){
        when(mockCourse.getCreditHours()).thenReturn(3);
        student.addCourseGrade(mockCourse, Grade.A);
        assertEquals(4, student.getGPA());
    }
    @Test
    public void testGetGPAException(){
        assertThrows(IllegalStateException.class, () ->
                student.getGPA());
    }
}