package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.AssignmentType;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.StudentGradingAPI;

import java.util.Random;

public class Student implements Runnable {

    private static final int MAX_ASSIGNMENT_CREATION_TIME = 1001;
    private static final int MAX_ASSIGNMENT_TYPE = 4;
    private static final int LAB_OPTION = 0;
    private static final int PLAYGROUND_OPTION = 1;
    private static final int HOMEWORK_OPTION = 2;
    private static final int PROJECT_OPTION = 3;
    private static final Random RND = new Random();

    private final int fn;
    private final String name;
    private final StudentGradingAPI studentGradingAPI;

    public Student(int fn, String name, StudentGradingAPI studentGradingAPI) {

        this.fn = fn;
        this.name = name;
        this.studentGradingAPI = studentGradingAPI;
    }

    @Override
    public void run() {

        int waitingTime = RND.nextInt(MAX_ASSIGNMENT_CREATION_TIME);

        int assignmentType = RND.nextInt(MAX_ASSIGNMENT_TYPE);

        AssignmentType toAddAssignmentType = switch (assignmentType) {

            case LAB_OPTION -> AssignmentType.LAB;
            case PLAYGROUND_OPTION -> AssignmentType.PLAYGROUND;
            case HOMEWORK_OPTION -> AssignmentType.HOMEWORK;
            case PROJECT_OPTION -> AssignmentType.PROJECT;
            default -> null;
        };

        try {

            Thread.sleep(waitingTime);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

        Assignment toAddAssignment = new Assignment(this.getFn(), this.getName(), toAddAssignmentType);
        this.getGrader().submitAssignment(toAddAssignment);
    }

    public int getFn() {

        return fn;
    }

    public String getName() {

        return name;
    }

    public StudentGradingAPI getGrader() {

        return studentGradingAPI;
    }
}