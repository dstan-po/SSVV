import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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

public class StudentIntegrationTest {
    public static Service service;

    @BeforeEach
    public void setup() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> homeworkValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();


        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "test_studenti.xml");
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(homeworkValidator, "test_teme.xml");
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "test_note.xml");

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
        writeEmptyXMLFile("test_studenti.xml", "Entitati");
        writeEmptyXMLFile("test_teme.xml", "Teme");
        writeEmptyXMLFile("test_note.xml", "Entitati");

        service.saveStudent("before_student", "nume", 937);
        service.saveTema("before_tema", "descriere", 2, 1);
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

    private Boolean testStudentExistence(String id, String name, Integer studentId) {
        Student newStudent = new Student(id, name, studentId);
        Iterable<Student> newStudentList = service.findAllStudents();

        for (Student student : newStudentList)
            if (student.equals(newStudent))
                return true;
        return false;
    }

    private Boolean testAssignmentExistence(String idTema, String descriere, int deadline, int startline) {
        Tema newTema = new Tema(idTema, descriere, deadline, startline);
        Iterable<Tema> newTemaList = service.findAllTeme();

        for (Tema tema : newTemaList)
            if (tema.equals(newTema))
                return true;
        return false;
    }

    private Boolean testGradeExistence(Pair<String, String> idNota, double nota, int saptamanaPredare, String feedback) {
        Nota newNota = new Nota(idNota, nota, saptamanaPredare, feedback);
        Iterable<Nota> newNotaList = service.findAllNote();

        for (Nota notaFound : newNotaList)
            if (notaFound.equals(newNota))
                return true;
        return false;
    }

    @Test
    public void addAssignment_Success_Test() {
        service.saveTema("id", "descriere", 2, 1);
        assert testAssignmentExistence("id", "descriere", 2, 1);
    }

    @Test
    public void addStudent_Success_Test() {
        service.saveStudent("id", "nume", 937);
        assert testStudentExistence("id", "nume", 937);
    }

    @Test
    public void addGrade_Success_Test() {
        service.saveNota("before_student", "before_tema", 1, 1, "groaznic");
        assert testGradeExistence(new Pair("before_student", "before_tema"), 3.5, 1, "groaznic");
    }

    @Test
    public void integration_Test() {
        service.saveStudent("id", "nume", 937);
        service.saveTema("id_tema", "descriere", 2, 1);
        service.saveNota("id", "id_tema", 1, 1, "groaznic");
        assert testGradeExistence(new Pair("id", "id_tema"), 3.5, 1, "groaznic");
    }

}
