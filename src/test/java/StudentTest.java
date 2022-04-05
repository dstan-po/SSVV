import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class StudentTest {

    public static Service service;

    public void setup() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> homeworkValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();


        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "test_studenti.xml");
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(homeworkValidator, "test_teme.xml");
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "test_note.xml");

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    private Boolean testStudentExistence(String id, String name, Integer studentId) {
        Student newStudent = new Student(id, name, studentId);
        service.saveStudent(id, name, studentId);
        Iterable<Student> newStudentList = service.findAllStudents();

        for (Student student : newStudentList) {
            if (student.equals(newStudent))
                return true;
        }
        return false;
    }

    /**
     * BVA of the group of the student
     * Invalid (min -1): 110
     * Valid (min, +min, -max, max): 111, 112, 936, 937
     * Invalid (max +1): 938
     */

    @Test
    public void addStudent_Success_LowerEdgeCase_Test() {
        assert testStudentExistence("2822", "Ionel", 111);
    }

    @Test
    public void addStudent_Success_LowerBoundaryCase_Test() {
        assert testStudentExistence("2822", "Ionel", 112);
    }

    @Test
    public void addStudent_Success_HigherEdgeCase_Test() {
        assert testStudentExistence("2822", "Ionel", 937);
    }

    @Test
    public void addStudent_Success_HigherBoundaryCase_Test() {
        assert testStudentExistence("2822", "Ionel", 936);
    }

    @Test
    public void addStudent_Fail_OutsideHigherEdgeCase_Test() {
        Assertions.assertThrowsExactly(ValidationException.class, () -> {
            testStudentExistence("2822", "Ionel", 938);
        });
    }

    @Test
    public void addStudent_Fail_OutsideLowerEdgeCase_Test() {
        Assertions.assertThrowsExactly(ValidationException.class, () -> {
            testStudentExistence("2822", "Ionel", 110);
        });
    }

    /**
     * BVA of the name of the student
     * Invalid: null, or empty string ""
     * Valid: Any string longer than 1 char
     */

    @Test
    public void addStudent_Fail_NullNameException_Test() {
        Assertions.assertThrowsExactly(ValidationException.class, () -> {
            testStudentExistence("2822", null, 111);
        });
    }

    @Test
    public void addStudent_Fail_EmptyNameException_Test() {
        Assertions.assertThrowsExactly(ValidationException.class, () -> {
            testStudentExistence("2822", "", 111);
        });
    }

    //TODO: EXCEL HELP

    @Test
    public void addStudent_Success_CorrectName_Test() {
        assert testStudentExistence("2822", "a", 937);
    }

    /**
     * BVA of the id of the student
     * Invalid: null, or empty string ""
     * Valid: Any string longer than 1 char
     */

    @Test
    public void addStudent_Fail_NullIdException_Test() {
        Assertions.assertThrowsExactly(ValidationException.class, () -> {
            testStudentExistence(null, "Ionel", 111);
        });
    }

    @Test
    public void addStudent_Fail_EmptyIdException_Test() {
        Assertions.assertThrowsExactly(ValidationException.class, () -> {
            testStudentExistence("", "Ionel", 111);
        });
    }

    @Test
    public void addStudent_Success_CorrectId_Test() {
        assert testStudentExistence("a", "a", 937);
    }

    private void writeEmptyXMLFile(String filenameWithExtension, String mainEntityBracket) {
        try {
            File myObj = new File(filenameWithExtension);

            if (!myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter(filenameWithExtension);
                myWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<" + mainEntityBracket + ">\n</" + mainEntityBracket + ">");
                myWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void resetFiles() {
        setup();
        writeEmptyXMLFile("test_studenti.xml", "Entitati");
    }
}