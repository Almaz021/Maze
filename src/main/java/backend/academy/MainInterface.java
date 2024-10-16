package backend.academy;

import backend.academy.settings.Settings;
import java.io.PrintWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainInterface {
    @Getter private String currMessage;
    private final PrintWriter writer;

    public void helloMessage() {
        currMessage = "Hello, User! Welcome to the Maze generation!";
        printMessage(currMessage);
    }

    public void chooseDimension(String dimensionType, int min, int max) {
        currMessage = "Choose a " + dimensionType + "! Enter an odd number between " + min + " and " + max
            + " or other to select random";
        printMessage(currMessage);
    }

    public void chooseHeight() {
        chooseDimension("height", Settings.MIN_HEIGHT, Settings.MAX_HEIGHT);
    }

    public void chooseWidth() {
        chooseDimension("width", Settings.MIN_WIDTH, Settings.MAX_WIDTH);
    }

    public void chooseGenerator() {
        currMessage = """
            Select generator:
            1. RecursiveBacktrackingGenerator
            2. PrimGenerator
            Type number of generator you want to choose or other symbol to choose random""";
        printMessage(currMessage);
    }

    public void chooseSolver() {
        currMessage = """
            Select solver:
            1. BFSSolver
            2. DFSSolver
            Type number of solver you want to choose or other symbol to choose random""";
        printMessage(currMessage);
    }

    public void choosePointCoordinates() {
        currMessage = "Enter two numbers separated by a space starting from (1) to (Maze size - 2). For example:10 25";
        printMessage(currMessage);
    }

    public void chosenSize() {
        currMessage = "Great! Now you need to choose generator for your Maze!";
        printMessage(currMessage);
    }

    public void chosenGenerator() {
        currMessage = "Good! Now you need to choose solver for your Maze!";
        printMessage(currMessage);
    }

    public void chosenSolver() {
        currMessage = "Cool! Now you need to enter two points between which you want to find the path!";
        printMessage(currMessage);
    }

    public void incorrectCoordinates() {
        currMessage = "Entered coordinates are out of range! Try again!";
        printMessage(currMessage);
    }

    public void wrongPoint() {
        currMessage = "This point is not a Passage! Try again!";
        printMessage(currMessage);
    }

    public void calculatePath() {
        currMessage = "Calculating the path...\n";
        printMessage(currMessage);
    }

    public void showMaze(StringBuilder maze) {
        currMessage = maze.toString();
        printMessage(currMessage);
    }

    private void printMessage(String msg) {
        writer.println(msg);
    }
}
