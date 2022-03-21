import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.Validator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentTest {

    public static Service service;

    public void setup() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();


        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "test_studenti.xml");
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, "test_teme.xml");
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
    public void addStudent_Fail_Test() {
        Iterable<Student> initialStudentList = service.findAllStudents();

        service.saveStudent("2822", "Ionel", 50);
        Iterable<Student> newStudentList = service.findAllStudents();

        assert initialStudentList.equals(newStudentList);
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