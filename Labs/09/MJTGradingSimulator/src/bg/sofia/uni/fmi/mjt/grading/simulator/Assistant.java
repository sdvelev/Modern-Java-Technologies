package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.AdminGradingAPI;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.CodePostGrader;

import java.util.concurrent.atomic.AtomicInteger;

public class Assistant extends Thread {

    private final String name;
    private CodePostGrader grader;

    private AtomicInteger numberOfGradedAssignments;

    public Assistant(String name, AdminGradingAPI grader) {

        this.name = name;
        this.grader = (CodePostGrader) grader;
        this.numberOfGradedAssignments = new AtomicInteger();
        this.numberOfGradedAssignments.set(0);
    }

    @Override
    public void run() {

        Assignment toCheckAssignment = null;

        while ((toCheckAssignment = this.grader.getAssignment()) != null) {

            try {

                Thread.sleep(toCheckAssignment.type().getGradingTime());
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            this.numberOfGradedAssignments.incrementAndGet();

        }

        System.out.println(this.name + " " + this.numberOfGradedAssignments.get());

    }
        /*while (true) {


            Assignment toCheckAssignment = this.grader.getAssignment();

            if (toCheckAssignment == null) {

                if (this.grader.getIsFinalized()) {
                    break;
                } else {

                    try {

                        this.wait();
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                }
            } else {

                try {

                    Thread.sleep(toCheckAssignment.type().getGradingTime());
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }

                this.numberOfGradedAssignments.incrementAndGet();
            }
        }

        System.out.println(this.name + " " + this.numberOfGradedAssignments.get());

    }
*/


/*

        while (true) {


            if (this.grader.getIsFinalized()) {

                while (true) {

                    Assignment toCheckAssignment = this.grader.getAssignment();

                    if (toCheckAssignment != null) {

                        try {

                            Thread.sleep(toCheckAssignment.type().getGradingTime());
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }

                        this.numberOfGradedAssignments.incrementAndGet();

                        //System.out.println("Work checked! There are " + numberOfGradedAssignments.get() +
                        " graded assignments!");
                    } else {
                        break;
                    }

                }
            }

            Assignment toCheckAssignment = this.grader.getAssignment();

            if (toCheckAssignment == null) {
                try {

                    this.wait();
                }
                catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
            else {

                try {

                    Thread.sleep(toCheckAssignment.type().getGradingTime());
                }
                catch (InterruptedException e) {

                    e.printStackTrace();
                }

                this.numberOfGradedAssignments.incrementAndGet();

                //System.out.println("Work checked! There are " + numberOfGradedAssignments.get() +
                " graded assignments!");
            }
        }

       // System.out.println(this.name + " " + this.numberOfGradedAssignments);
    }*/

    public int getNumberOfGradedAssignments() {

        return this.numberOfGradedAssignments.get();
    }

}