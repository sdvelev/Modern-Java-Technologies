package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Student;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CodePostGraderTest {

    @Test
    void testCreateAssistantsSuccessfully() {

        CodePostGrader codePostGrader = new CodePostGrader(6);

        codePostGrader.finalizeGrading();

        Assertions.assertEquals(6, codePostGrader.getAssistants().size(),
            "Five assistants are expected to be created");
    }

    @Test
    void testGetSubmittedAssignmentsCountSuccessfully() {

        CodePostGrader codePostGrader = new CodePostGrader(5);

        Thread student1 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student1.start();
        Thread student2 = new Thread(new Student(62538, "George", codePostGrader));
        student2.start();
        Thread student3 = new Thread(new Student(62539, "Mark", codePostGrader));
        student3.start();
        Thread student4 = new Thread(new Student(62540, "Amy", codePostGrader));
        student4.start();
        Thread student5 = new Thread(new Student(62541, "Nikola", codePostGrader));
        student5.start();

        Thread student6 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student6.start();
        Thread student7 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student7.start();
        Thread student8 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student8.start();
        Thread student9 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student9.start();
        Thread student10 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student10.start();

        Thread student11 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student11.start();
        Thread student12 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student12.start();
        Thread student13 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student13.start();
        Thread student14 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student14.start();
        Thread student15 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student15.start();

        Thread student16 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student16.start();
        Thread student17 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student17.start();
        Thread student18 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student18.start();
        Thread student19 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student19.start();
        Thread student20 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student20.start();

        Thread student21 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student21.start();
        Thread student22 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student22.start();
        Thread student23 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student23.start();
        Thread student24 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student24.start();
        Thread student25 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student25.start();

        Thread student26 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student26.start();
        Thread student27 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student27.start();
        Thread student28 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student28.start();
        Thread student29 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student29.start();
        Thread student30 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student30.start();

        Thread student31 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student31.start();
        Thread student32 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student32.start();
        Thread student33 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student33.start();
        Thread student34 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student34.start();
        Thread student35 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student35.start();

        Thread student36 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student36.start();
        Thread student37 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student37.start();
        Thread student38 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student38.start();
        Thread student39 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student39.start();
        Thread student40 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student40.start();

        Thread student41 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student41.start();
        Thread student42 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student42.start();
        Thread student43 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student43.start();
        Thread student44 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student44.start();
        Thread student45 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student45.start();

        Thread student46 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student46.start();
        Thread student47 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student47.start();
        Thread student48 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student48.start();
        Thread student49 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student49.start();
        Thread student50 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student50.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        codePostGrader.finalizeGrading();

        Assertions.assertEquals(50, codePostGrader.getSubmittedAssignmentsCount(),
            "The number of submitted assignments are not the same as expected");
    }
}
