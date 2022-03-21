import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Iterator;

public class StudentTest {
    public static Service service;

    public StudentTest() {
    }

    @Before
    public void setup() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();
        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "test_studenti.xml");
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, "test_teme.xml");
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "test_note.xml");
        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @Test
    public void Test_AddStudent_Success_Test() {
        Student newStudent = new Student("2822", "Ionel", 32);
        service.saveStudent("2822", "Ionel", 222);
        Iterable<Student> newStudentList = service.findAllStudents();
        Iterator var3 = newStudentList.iterator();

        Student student;
        do {
            if (!var3.hasNext()) {
                assert false;

                return;
            }

            student = (Student) var3.next();
        } while (!student.equals(newStudent));

    }

    @Test
    public void Test_AddStudent_Fail_Test() {
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
        } catch (IOException var5) {
            var5.printStackTrace();
        }
    }

    @BeforeEach
    public void resetFiles() {
        this.writeEmptyXMLFile("test_studenti.xml", "Entitati");
    }
}