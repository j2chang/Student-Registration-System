package edu.virginia.cs.hw4;
import jdk.jshell.spi.SPIResolutionException;
import org.junit.jupiter.api.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class RegistrationTest {
    private RegistrationImpl reg;
    private Course mockCourse;
    private Course mockCourse2;
    private Course mockCourse3;
    private Student mockStudent;
    private Student mockStudent2;

    private List<Prerequisite> testprerequisites;
    private Prerequisite prerequisite1;
    private Prerequisite prerequisite2;
    private Prerequisite prerequisite3;
    private CourseCatalog testCatalog;
    private Transcript testTranscript;

    @BeforeEach
    public void setup(){
        reg = new RegistrationImpl();
        mockCourse = mock(Course.class);
        mockCourse2 = mock(Course.class);
        mockCourse3 = mock(Course.class);
        mockStudent = mock(Student.class);
        mockStudent2 = mock(Student.class);
        testTranscript = new Transcript(mockStudent);
        testTranscript.courseHistory.put(mockCourse, Grade.A);
        testTranscript.courseHistory.put(mockCourse2, Grade.A);
        prerequisite1 = new Prerequisite(mockCourse, Grade.C);
        prerequisite2 = new Prerequisite(mockCourse2, Grade.B);
        testprerequisites = List.of(prerequisite1,prerequisite2);
        testCatalog = new CourseCatalog();
        testCatalog.addCourse(mockCourse);
        testCatalog.addCourse(mockCourse2);

    }

    @Test
    public void testsetCourseCatalog(){
        reg.setCourseCatalog(testCatalog);
        assertIterableEquals(testCatalog.courseList, reg.courseCatalog.courseList);
        assertTrue(reg.courseCatalog.courseList.contains(mockCourse));
        assertTrue(reg.courseCatalog.courseList.contains(mockCourse2));
    }
    @Test
    public void testgetCourseCatalog(){
        reg.setCourseCatalog(testCatalog);
        CourseCatalog cc = reg.getCourseCatalog();
        assertEquals(testCatalog, cc);
    }
    @Test
    public void testisEnrollmentFull(){
        when(mockCourse.getCurrentEnrollmentSize()).thenReturn(400);
        when(mockCourse.getEnrollmentCap()).thenReturn(400);
        assertTrue(reg.isEnrollmentFull(mockCourse));
    }
    @Test
    public void testisEnrollmentFullfalse(){
        when(mockCourse.getCurrentEnrollmentSize()).thenReturn(200);
        when(mockCourse.getEnrollmentCap()).thenReturn(400);
        assertFalse(reg.isEnrollmentFull(mockCourse));
    }
    @Test
    public void testisWaitListFull(){
        when(mockCourse.getCurrentWaitListSize()).thenReturn(40);
        when(mockCourse.getWaitListCap()).thenReturn(40);
        assertTrue(reg.isWaitListFull(mockCourse));
    }
    @Test
    public void testisWaitListFullfalse(){
        when(mockCourse.getCurrentWaitListSize()).thenReturn(35);
        when(mockCourse.getWaitListCap()).thenReturn(40);
        assertFalse(reg.isWaitListFull(mockCourse));
    }
    @Test
    public void testgetEnrollmentStatusopen(){
        when(mockCourse.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.OPEN);
        assertEquals(Course.EnrollmentStatus.OPEN,reg.getEnrollmentStatus(mockCourse));
    }
    @Test
    public void testgetEnrollmentStatusclosed(){
        when(mockCourse.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.CLOSED);
        assertEquals(Course.EnrollmentStatus.CLOSED,reg.getEnrollmentStatus(mockCourse));
    }
    @Test
    public void testgetEnrollmentStatuswaitlist(){
        when(mockCourse.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.WAIT_LIST);
        assertEquals(Course.EnrollmentStatus.WAIT_LIST,reg.getEnrollmentStatus(mockCourse));
    }
    @Test
    public void testareCoursesConflictedcase1(){
        when(mockCourse.getMeetingStartTimeHour()).thenReturn(12);
        when(mockCourse.getMeetingStartTimeMinute()).thenReturn(30);
        when(mockCourse.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(13);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        assertTrue(reg.areCoursesConflicted(mockCourse,mockCourse2));
    }
    @Test
    public void testareCoursesConflictedcase2(){
        when(mockCourse.getMeetingStartTimeHour()).thenReturn(12);
        when(mockCourse.getMeetingStartTimeMinute()).thenReturn(30);
        when(mockCourse.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(11);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(120);
        when(mockCourse.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        assertTrue(reg.areCoursesConflicted(mockCourse,mockCourse2));
    }
    @Test
    public void testareCoursesConflictedfalse1(){
        when(mockCourse.getMeetingStartTimeHour()).thenReturn(12);
        when(mockCourse.getMeetingStartTimeMinute()).thenReturn(30);
        when(mockCourse.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(11);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(120);
        when(mockCourse.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY));
        assertFalse(reg.areCoursesConflicted(mockCourse,mockCourse2));
    }
    @Test
    public void testareCoursesConflictedfalse2(){
        when(mockCourse.getMeetingStartTimeHour()).thenReturn(12);
        when(mockCourse.getMeetingStartTimeMinute()).thenReturn(30);
        when(mockCourse.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(15);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        assertFalse(reg.areCoursesConflicted(mockCourse,mockCourse2));
    }
    @Test
    public void testareCoursesConflictedfalse3(){
        when(mockCourse.getMeetingStartTimeHour()).thenReturn(12);
        when(mockCourse.getMeetingStartTimeMinute()).thenReturn(30);
        when(mockCourse.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(9);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        assertFalse(reg.areCoursesConflicted(mockCourse,mockCourse2));
    }
    @Test
    public void testareCoursesConflictedfalse4(){
        when(mockCourse.getMeetingStartTimeHour()).thenReturn(10);
        when(mockCourse.getMeetingStartTimeMinute()).thenReturn(30);
        when(mockCourse.getMeetingDurationMinutes()).thenReturn(30);
        when(mockCourse2.getMeetingStartTimeHour()).thenReturn(11);
        when(mockCourse2.getMeetingStartTimeMinute()).thenReturn(0);
        when(mockCourse2.getMeetingDurationMinutes()).thenReturn(50);
        when(mockCourse.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        when(mockCourse2.getMeetingDays()).thenReturn(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));
        assertFalse(reg.areCoursesConflicted(mockCourse,mockCourse2));
    }
    @Test
    public void testhasConflictWithStudentSchedule(){
        when(mockCourse.isStudentEnrolled(mockStudent)).thenReturn(true);
        assertTrue(reg.hasConflictWithStudentSchedule(mockCourse,mockStudent));
    }
    @Test
    public void testhasConflictWithStudentSchedulefail(){
        when(mockCourse.isStudentEnrolled(mockStudent)).thenReturn(false);
        assertFalse(reg.hasConflictWithStudentSchedule(mockCourse,mockStudent));
    }
    @Test
    public void testhasStudentMeetsPrerequisites(){
        when(mockStudent.meetsPrerequisite(prerequisite1)).thenReturn(true);
        when(mockStudent.meetsPrerequisite(prerequisite2)).thenReturn(true);
        assertTrue(reg.hasStudentMeetsPrerequisites(mockStudent, testprerequisites));
    }
    @Test
    public void testhasStudentMeetsPrerequisitesfalse(){
        when(mockStudent.meetsPrerequisite(prerequisite1)).thenReturn(false);
        when(mockStudent.meetsPrerequisite(prerequisite2)).thenReturn(true);
        assertFalse(reg.hasStudentMeetsPrerequisites(mockStudent, testprerequisites));
    }
    @Test
    public void testregisterStudentForCourseFull(){
        assertEquals(RegistrationResult.COURSE_FULL, reg.registerStudentForCourse(mockStudent,mockCourse));
    }
    @Test
    public void testregisterStudentForCoursePreNotMet(){
        when(mockCourse3.getCurrentEnrollmentSize()).thenReturn(4);
        when(mockCourse3.getEnrollmentCap()).thenReturn(400);
        when(mockCourse3.getCurrentWaitListSize()).thenReturn(0);
        when(mockCourse3.getWaitListCap()).thenReturn(100);
        when(mockCourse3.getPrerequisites()).thenReturn(testprerequisites);
        when(reg.hasStudentMeetsPrerequisites(mockStudent, mockCourse3.getPrerequisites())).thenReturn(false);
        assertEquals(RegistrationResult.PREREQUISITE_NOT_MET, reg.registerStudentForCourse(mockStudent, mockCourse3));
    }
    @Test
    public void testregisterStudentForCourseConflict(){
        when(mockCourse3.getCurrentEnrollmentSize()).thenReturn(4);
        when(mockCourse3.getEnrollmentCap()).thenReturn(400);
        when(mockCourse3.getCurrentWaitListSize()).thenReturn(0);
        when(mockCourse3.getWaitListCap()).thenReturn(100);
        when(mockCourse3.getPrerequisites()).thenReturn(testprerequisites);
        when(mockStudent.meetsPrerequisite(prerequisite1)).thenReturn(true);
        when(mockStudent.meetsPrerequisite(prerequisite2)).thenReturn(true);
        when(reg.hasConflictWithStudentSchedule(mockCourse3, mockStudent)).thenReturn(true);
        assertEquals(RegistrationResult.SCHEDULE_CONFLICT, reg.registerStudentForCourse(mockStudent, mockCourse3));
    }
    @Test
    public void testregisterStudentForCourseEnrollVeryOpen(){
        when(mockCourse3.getCurrentEnrollmentSize()).thenReturn(4);
        when(mockCourse3.getEnrollmentCap()).thenReturn(400);
        when(mockCourse3.getCurrentWaitListSize()).thenReturn(0);
        when(mockCourse3.getWaitListCap()).thenReturn(100);
        when(mockCourse3.getPrerequisites()).thenReturn(testprerequisites);
        when(mockStudent.meetsPrerequisite(prerequisite1)).thenReturn(true);
        when(mockStudent.meetsPrerequisite(prerequisite2)).thenReturn(true);
        when(reg.hasConflictWithStudentSchedule(mockCourse3, mockStudent)).thenReturn(false);
        when(mockCourse3.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.OPEN);
        assertEquals(RegistrationResult.ENROLLED, reg.registerStudentForCourse(mockStudent, mockCourse3));
        verify(mockCourse3).addStudentToEnrolled(mockStudent);
    }
    @Test
    public void testregisterStudentForCourseEnrollLastSpot(){
        when(mockCourse3.getCurrentEnrollmentSize()).thenReturn(400);
        when(mockCourse3.getEnrollmentCap()).thenReturn(400);
        when(mockCourse3.getCurrentWaitListSize()).thenReturn(0);
        when(mockCourse3.getWaitListCap()).thenReturn(100);
        when(mockCourse3.getPrerequisites()).thenReturn(testprerequisites);
        when(mockStudent.meetsPrerequisite(prerequisite1)).thenReturn(true);
        when(mockStudent.meetsPrerequisite(prerequisite2)).thenReturn(true);
        when(reg.hasConflictWithStudentSchedule(mockCourse3, mockStudent)).thenReturn(false);
        when(mockCourse3.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.OPEN);
        assertEquals(RegistrationResult.ENROLLED, reg.registerStudentForCourse(mockStudent, mockCourse3));
        verify(mockCourse3).addStudentToEnrolled(mockStudent);
        verify(mockCourse3).setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
    }
    @Test
    public void testregisterStudentForCourseEnrollWaitList(){
        when(mockCourse3.getCurrentEnrollmentSize()).thenReturn(400);
        when(mockCourse3.getEnrollmentCap()).thenReturn(400);
        when(mockCourse3.getCurrentWaitListSize()).thenReturn(0);
        when(mockCourse3.getWaitListCap()).thenReturn(100);
        when(mockCourse3.getPrerequisites()).thenReturn(testprerequisites);
        when(mockStudent.meetsPrerequisite(prerequisite1)).thenReturn(true);
        when(mockStudent.meetsPrerequisite(prerequisite2)).thenReturn(true);
        when(reg.hasConflictWithStudentSchedule(mockCourse3, mockStudent)).thenReturn(false);
        when(mockCourse3.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.WAIT_LIST);
        assertEquals(RegistrationResult.WAIT_LISTED, reg.registerStudentForCourse(mockStudent, mockCourse3));
        verify(mockCourse3).addStudentToWaitList(mockStudent);
    }
    @Test
    public void testregisterStudentForCourseEnrollWaitListLastSpot(){
        when(mockCourse3.getCurrentEnrollmentSize()).thenReturn(399);
        when(mockCourse3.getEnrollmentCap()).thenReturn(400);
        when(mockCourse3.getCurrentWaitListSize()).thenReturn(100);
        when(mockCourse3.getWaitListCap()).thenReturn(100);
        when(mockCourse3.getPrerequisites()).thenReturn(testprerequisites);
        when(mockStudent.meetsPrerequisite(prerequisite1)).thenReturn(true);
        when(mockStudent.meetsPrerequisite(prerequisite2)).thenReturn(true);
        when(reg.hasConflictWithStudentSchedule(mockCourse3, mockStudent)).thenReturn(false);
        when(mockCourse3.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.WAIT_LIST);
        assertEquals(RegistrationResult.WAIT_LISTED, reg.registerStudentForCourse(mockStudent, mockCourse3));
        verify(mockCourse3).addStudentToWaitList(mockStudent);
        verify(mockCourse3).setEnrollmentStatus(Course.EnrollmentStatus.CLOSED);
    }
    @Test
    public void testregisterStudentForCourseEnrollWClosed(){
        when(mockCourse3.getCurrentEnrollmentSize()).thenReturn(399);
        when(mockCourse3.getEnrollmentCap()).thenReturn(400);
        when(mockCourse3.getCurrentWaitListSize()).thenReturn(100);
        when(mockCourse3.getWaitListCap()).thenReturn(100);
        when(mockCourse3.getPrerequisites()).thenReturn(testprerequisites);
        when(mockStudent.meetsPrerequisite(prerequisite1)).thenReturn(true);
        when(mockStudent.meetsPrerequisite(prerequisite2)).thenReturn(true);
        when(reg.hasConflictWithStudentSchedule(mockCourse3, mockStudent)).thenReturn(false);
        when(mockCourse3.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.CLOSED);
        assertEquals(RegistrationResult.COURSE_CLOSED, reg.registerStudentForCourse(mockStudent, mockCourse3));
        verify(mockCourse3, never()).addStudentToWaitList(mockStudent);
        verify(mockCourse3, never()).addStudentToEnrolled(mockStudent);
    }
    @Test
    public void testdropCourseSimpleCheckForAdd(){
        when(mockCourse3.isStudentEnrolled(mockStudent)).thenReturn(true);
        reg.dropCourse(mockStudent,mockCourse3);
        verify(mockCourse3).removeStudentFromEnrolled(mockStudent);
    }
    @Test
    public void testdropCourseCheckForWaitlist(){
        when(mockCourse3.isStudentEnrolled(mockStudent)).thenReturn(true);
        when(mockCourse3.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.WAIT_LIST);
        when(mockCourse3.getFirstStudentOnWaitList()).thenReturn(mockStudent2);
        reg.dropCourse(mockStudent,mockCourse3);
        verify(mockCourse3).addStudentToEnrolled(mockStudent2);
        verify(mockCourse3).removeStudentFromEnrolled(mockStudent);
    }
    @Test
    public void testdropCourseCheckFromWaitList(){
        when(mockCourse3.isStudentWaitListed(mockStudent)).thenReturn(true);
        reg.dropCourse(mockStudent,mockCourse3);
        verify(mockCourse3).removeStudentFromWaitList(mockStudent);
    }

    @Test
    public void testdropCourseNotValid(){
        when(mockCourse3.isStudentEnrolled(mockStudent)).thenReturn(false);
        when(mockCourse3.isStudentWaitListed(mockStudent)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () ->
                reg.dropCourse(mockStudent,mockCourse3));
    }
    @Test
    public void testdropCourseChangeToWaitList(){
        when(mockCourse3.getEnrollmentStatus()).thenReturn(Course.EnrollmentStatus.CLOSED);
        when(mockCourse3.isStudentEnrolled(mockStudent)).thenReturn(true);
        reg.dropCourse(mockStudent,mockCourse3);
        verify(mockCourse3).setEnrollmentStatus(Course.EnrollmentStatus.WAIT_LIST);
    }
    @Test
    public void testdropCourseChangeToOpen(){
        when(mockCourse3.getCurrentEnrollmentSize()).thenReturn(399);
        when(mockCourse3.getEnrollmentCap()).thenReturn(400);
        when(mockCourse3.isWaitListEmpty()).thenReturn(true);
        when(mockCourse3.isStudentEnrolled(mockStudent)).thenReturn(true);
        reg.dropCourse(mockStudent,mockCourse3);
        verify(mockCourse3).setEnrollmentStatus(Course.EnrollmentStatus.OPEN);
    }
    @Test
    public void testdropCourseChangeRemoveOnlyPersonInWaitList(){
        when(mockCourse3.getCurrentEnrollmentSize()).thenReturn(400);
        when(mockCourse3.getEnrollmentCap()).thenReturn(400);
        when(mockCourse3.isWaitListEmpty()).thenReturn(true);
        when(mockCourse3.isStudentEnrolled(mockStudent)).thenReturn(true);
        reg.dropCourse(mockStudent,mockCourse3);
        verify(mockCourse3, times(0)).setEnrollmentStatus(Course.EnrollmentStatus.OPEN);
    }
}
