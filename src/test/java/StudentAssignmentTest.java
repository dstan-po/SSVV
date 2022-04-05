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

public class StudentAssignmentTest {
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

    private Boolean testAssignmentExistence(String id, String description, int deadline, int startLine) {
        int res = service.saveTema(id, description, deadline, startLine);
        return res == 1;
    }

    @Test
    public void addAssignment_Success_AddExistingAssignment_Test() {
        service.saveTema("test_id_add", "test_desc", 2, 1);
        assert !testAssignmentExistence("test_id_add", "test_desc", 2, 1);
    }


    @Test
    public void addAssignment_Success_AddNonExistentAssignment_Test() {
        assert testAssignmentExistence("test_id_add", "test_desc", 2, 1);
    }

    @Test
    public void addAssignment_Success_AddExistingAndWriteToFile_Test() {
        service.saveTema("test_write", "test_write_desc", 2, 1);
        assert !testAssignmentExistence("test_write", "test_write_desc", 2, 1);
    }

    @Test
    public void addAssignment_Success_AddNonExistingAndNotWriteToFile_Test() {
        assert testAssignmentExistence("test_not_written", "test_not_written", 2, 1);
    }

    @Test
    public void addAssignment_Fail_AddInvalidIdEmpty_Test() {
        Assertions.assertThrowsExactly(ValidationException.class, () -> testAssignmentExistence("", "test_desc", 1, 2));
    }

    @Test
    public void addAssignment_Fail_AddInvalidIdNull_Test() {
        Assertions.assertThrowsExactly(ValidationException.class, () -> testAssignmentExistence(null, "test_desc", 1, 2));
    }

    @Test
    public void addAssignment_Fail_AddInvalidDescriptionNull_Test() {
        Assertions.assertThrowsExactly(ValidationException.class, () -> testAssignmentExistence("test_id", null, 1, 2));
    }

    @Test
    public void addAssignment_Fail_AddInvalidDeadline_Test() {
        Assertions.assertThrowsExactly(ValidationException.class, () -> testAssignmentExistence("test_id", "test_desc", 1, 2));
    }

    @Test
    public void addAssignment_Fail_AddInvalidStartLine_Test() {
        Assertions.assertThrowsExactly(ValidationException.class, () -> testAssignmentExistence("test_id", "test_desc", 1, -1));
    }


    @BeforeEach
    public void resetFiles() {
        writeEmptyXMLFile("test_teme.xml", "Teme");
        setup();
    }
}
