package backend.academy;

import backend.academy.interfaces.Generator;
import backend.academy.interfaces.Modifier;
import backend.academy.interfaces.Solver;
import backend.academy.settings.Settings;
import java.io.PrintWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * MainInterface handles user interaction by displaying messages and prompts
 * related to maze generation, solving, and modification.
 */
@RequiredArgsConstructor
public class MainInterface {
    @Getter private String currMessage;
    private final PrintWriter writer;

    public void helloMessage() {
        currMessage = "Hello, User! Welcome to the Maze generation!";
        printMessage(currMessage);
    }

    public void chooseDimension(String dimensionType, int min, int max) {
        currMessage = "\nChoose a " + dimensionType + "! Enter an odd number between " + min + " and " + max
            + " or other symbols to select random";
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
            Type number of generator you want to choose or other symbols to choose random""";
        printMessage(currMessage);
    }

    public void chooseSolver() {
        currMessage = """
            Select solver:
            1. BFSSolver
            2. DFSSolver
            Type number of solver you want to choose or other symbols to choose random""";
        printMessage(currMessage);
    }

    public void chooseModifier() {
        currMessage = """
            \nSelect modifier:
            1. NonIdealMazeModifier
            Type number of modifier you want to choose or other symbols to choose random""";
        printMessage(currMessage);
    }

    public void choosePointCoordinates() {
        currMessage =
            "Enter col and row separated by one space starting from (1) to (Maze size - 2). For example:10 25";
        printMessage(currMessage);
    }

    public void choosePoint() {
        currMessage = "Cool! Now you need to enter two points between which you want to find the path!\n";
        printMessage(currMessage);
    }

    public void chosenSize(int height, int width) {
        currMessage = "\nWidth: " + width + " Height: " + height + "\n"
            + "\nGreat! Now you need to choose generator for your Maze!\n";
        printMessage(currMessage);
    }

    public void chosenGenerator(Generator generator) {
        currMessage = "\nSelected generator: " + generator.toString() + "\n"
            + "\nGood! Now you need to choose solver for your Maze!\n";
        printMessage(currMessage);
    }

    public void chosenSolver(Solver solver) {
        currMessage = "\nSelected solver: " + solver.toString() + "\n";
        printMessage(currMessage);
    }

    public void chosenModifier(Modifier modifier) {
        currMessage = "\nSelected modifier: " + modifier.toString() + "\n";
        printMessage(currMessage);
    }

    public void chosenPoint(int x, int y) {
        currMessage = "\nChosen point: x = " + x + " y = " + y + "\n";
        printMessage(currMessage);
    }

    public void chooseRandom() {
        currMessage = "Wrong! Number will be selected automatically";
        printMessage(currMessage);
    }

    public void requestForModification() {
        currMessage = "Do you want to modify the maze? Write YES or NO (Default is NO)";
        printMessage(currMessage);
    }

    public void onlyNumbers() {
        currMessage = "You need to enter only numbers separated by one space!";
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

    public void generateMaze() {
        currMessage = "Nice! Generating Maze...\n";
        printMessage(currMessage);
    }

    public void modifyMaze() {
        currMessage = "Modifying Maze...\n";
        printMessage(currMessage);
    }

    public void showMaze(StringBuilder maze) {
        currMessage = maze.toString();
        printMessage(currMessage);
    }

    public void finish() {
        currMessage = "Work Finished!";
        printMessage(currMessage);
    }

    private void printMessage(String msg) {
        writer.println(msg);
    }
}
