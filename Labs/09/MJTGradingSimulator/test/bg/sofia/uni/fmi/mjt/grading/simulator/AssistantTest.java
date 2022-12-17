package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.grader.CodePostGrader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssistantTest {

    @Test
    void testGetNumberOfGradedAssignmentsCorrect() {

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

        try {
            Thread.sleep(6000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        codePostGrader.finalizeGrading();

        int totalCheckedAssignments = 0;

        for (Assistant assistant : codePostGrader.getAssistants()) {

            totalCheckedAssignments += assistant.getNumberOfGradedAssignments();
        }

        Assertions.assertEquals(20, codePostGrader.getSubmittedAssignmentsCount(),
            "The number of checked assignments by each assistant is not the same as expected");
    }
}
