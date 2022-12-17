package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Assistant;
import bg.sofia.uni.fmi.mjt.grading.simulator.Student;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.AssignmentType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class CodePostGrader implements AdminGradingAPI {

    private final int numberOfAssistants;
    private Queue<Assignment> assignmentQueue;
    private final List<Assistant> assistantList;

    private boolean isFinalized;
    private AtomicInteger numberOfSubmittedAssignments;

    public CodePostGrader(int numberOfAssistants) {

        this.numberOfAssistants = numberOfAssistants;
        this.assignmentQueue = new ArrayDeque<>();
        this.assistantList = new ArrayList<>(numberOfAssistants);
        this.numberOfSubmittedAssignments = new AtomicInteger();
        this.numberOfSubmittedAssignments.set(0);
        this.isFinalized = false;

        this.createAssistants();
    }


    /**
     * Retrieves an assignment to be graded from the set of submitted but still ungraded assignments.
     * If there are several ungraded assignments, which particular one to return is undefined.
     * Getting an assignment removes it from the set of ungraded assignments.
     *
     * @return an assignment to be graded.
     */
    @Override
    public synchronized Assignment getAssignment() {

        while (!this.getIsFinalized() && this.assignmentQueue.isEmpty()) {

            try {

                this.wait();
            }
            catch (InterruptedException e) {

                e.printStackTrace();
            }

        }
        return this.assignmentQueue.poll();
    }

    /**
     * Returns the total number of assignments submitted.
     *
     * @return the total number of assignments submitted.
     */
    @Override
    public int getSubmittedAssignmentsCount() {

        return this.numberOfSubmittedAssignments.get();
    }

    public boolean getIsFinalized() {

        return this.isFinalized;
    }

    /**
     * Notifies the assistants to finalize grading. All already submitted assignments must be graded.
     */
    @Override
    public synchronized void finalizeGrading() {

        this.isFinalized = true;
        this.notifyAll();

     /*   for (Assistant assistant : this.getAssistants()) {
            synchronized (assistant) {
                assistant.notify();
            }
        }*/

    }

    /**
     * Returns a list of the course assistants, in undefined order.
     *
     * @return a list of the assistants
     */
    @Override
    public List<Assistant> getAssistants() {

        return this.assistantList;
    }

    /**
     * Submits a new {@link Assignment} for grading.
     *
     * @param assignment which is submitted for grading
     */
    @Override
    public synchronized void submitAssignment(Assignment assignment) {

        if (!this.isFinalized) {

            this.numberOfSubmittedAssignments.incrementAndGet();
            this.assignmentQueue.add(assignment);
        }

        this.notifyAll();

     /*   for (Assistant assistant : this.getAssistants()) {
            synchronized (assistant) {
                assistant.notify();
            }
        }*/

    }

    private void createAssistants() {

        for (int i = 0; i < this.numberOfAssistants; i++) {
            Assistant toAdd = new Assistant("Assistant-" + (i + 1), this);
            this.assistantList.add(toAdd);
            toAdd.start();
        }

    }

    public static void main(String[] args) {

        CodePostGrader codePostGrader = new CodePostGrader(5);

        Thread student1 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student1.start();
        Thread student2 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student2.start();
        Thread student3 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student3.start();
        Thread student4 = new Thread(new Student(62537, "Ivan", codePostGrader));
        student4.start();
        Thread student5 = new Thread(new Student(62537, "Ivan", codePostGrader));
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
            Thread.sleep(11000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        codePostGrader.finalizeGrading();

        System.out.println("Submitted assignments: " + codePostGrader.getSubmittedAssignmentsCount());
    }

}
