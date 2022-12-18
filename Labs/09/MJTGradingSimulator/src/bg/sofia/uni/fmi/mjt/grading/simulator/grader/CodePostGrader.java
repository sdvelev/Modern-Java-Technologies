package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Assistant;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class CodePostGrader implements AdminGradingAPI {

    private final int numberOfAssistants;
    private final Queue<Assignment> assignmentQueue;
    private final List<Assistant> assistantList;

    private boolean isFinalized;
    private final AtomicInteger numberOfSubmittedAssignments;

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
            } catch (InterruptedException e) {

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
    }

    private void createAssistants() {

        for (int i = 0; i < this.numberOfAssistants; i++) {
            Assistant toAdd = new Assistant("Assistant-" + (i + 1), this);
            this.assistantList.add(toAdd);
            toAdd.start();
        }
    }
}
