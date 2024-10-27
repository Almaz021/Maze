package backend.academy.services;

import backend.academy.MainInterface;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Type;
import backend.academy.factories.GeneratorFactory;
import backend.academy.factories.ModifierFactory;
import backend.academy.factories.SolverFactory;
import backend.academy.interfaces.Generator;
import backend.academy.interfaces.Modifier;
import backend.academy.interfaces.Solver;
import backend.academy.renderer.BaseRenderer;
import backend.academy.settings.Settings;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

/**
 * StartService is responsible for orchestrating the maze generation,
 * modification, and solving processes, interacting with the user through
 * the MainInterface.
 */
@RequiredArgsConstructor
public class StartService {
    private static final List<Type> PASSABLE_TYPES = List.of(Type.NORMAL, Type.ICE, Type.SAND);
    private final BufferedReader reader;
    private final BaseRenderer renderer;
    private final SecureRandom random;
    private final MainInterface mainInterface;
    private Maze maze;

    /**
     * Starts the service, prompting the user for input and executing
     * maze generation, modification, and solving as required.
     *
     * @throws IOException if an input/output error occurs
     */
    public void start() throws IOException {
        mainInterface.helloMessage();

        // getting size of the Maze
        int width = getMazeWidth();
        int height = getMazeHeight();
        mainInterface.chosenSize(height, width);

        // getting Generator and Solver
        Generator generator = getMazeGenerator();
        Solver solver = getMazeSolver();

        // generating Maze
        generateMaze(generator, height, width);

        // request for modification and Maze modifying
        if (isModificationRequested()) {
            modifyMaze(height, width);
        }

        // getting start and end points
        Coordinate firstPoint = getPointCoordinates(height, width);
        Coordinate secondPoint = getPointCoordinates(height, width);

        // calculating and showing path
        calculateAndShowPath(solver, firstPoint, secondPoint);
        mainInterface.finish();

    }

    private int getMazeWidth() throws IOException {
        mainInterface.chooseWidth();
        String w = getInput();
        return checkSize(w, Settings.MIN_WIDTH, Settings.MAX_WIDTH);
    }

    private int getMazeHeight() throws IOException {
        mainInterface.chooseHeight();
        String h = getInput();
        return checkSize(h, Settings.MIN_HEIGHT, Settings.MAX_HEIGHT);
    }

    private Generator getMazeGenerator() throws IOException {
        mainInterface.chooseGenerator();
        String g = getInput();
        Generator generator = selectGenerator(g);
        mainInterface.chosenGenerator(generator);
        return generator;
    }

    private Solver getMazeSolver() throws IOException {
        mainInterface.chooseSolver();
        String s = getInput();
        Solver solver = selectSolver(s);
        mainInterface.chosenSolver(solver);
        return solver;
    }

    private void generateMaze(Generator generator, int height, int width) {
        mainInterface.generateMaze();
        maze = generator.generate(height, width);
        StringBuilder mazeString = renderer.render(maze);
        mainInterface.showMaze(mazeString);
    }

    private boolean isModificationRequested() throws IOException {
        mainInterface.requestForModification();
        String answer = getInput();
        return "YES".equalsIgnoreCase(answer);
    }

    private void modifyMaze(int height, int width) throws IOException {
        mainInterface.chooseModifier();
        String m = getInput();
        Modifier modifier = selectModifier(m);

        mainInterface.chosenModifier(modifier);
        mainInterface.modifyMaze();

        maze = modifier.modify(height, width);
        StringBuilder mazeString = renderer.render(maze);

        mainInterface.showMaze(mazeString);
    }

    /**
     * Prompts the user for coordinates of a point in the maze and returns the coordinates.
     *
     * @param height the height of the maze
     * @param width the width of the maze
     * @return the selected Coordinate
     * @throws IOException if an input/output error occurs
     */
    private Coordinate getPointCoordinates(int height, int width) throws IOException {
        mainInterface.choosePoint();
        mainInterface.choosePointCoordinates();
        String coords;
        do {
            coords = getInput();
        } while (checkCoordinates(coords, height, width));

        int firstPointX = Integer.parseInt(coords.split(" ")[0]);
        int firstPointY = Integer.parseInt(coords.split(" ")[1]);
        mainInterface.chosenPoint(firstPointX, firstPointY);
        return new Coordinate(firstPointY, firstPointX);
    }

    private void calculateAndShowPath(Solver solver, Coordinate firstPoint, Coordinate secondPoint) {
        mainInterface.calculatePath();
        List<Coordinate> path;
        path = solver.solve(maze, firstPoint, secondPoint);
        StringBuilder mazeString = renderer.render(maze, path);
        mainInterface.showMaze(mazeString);
    }

    /**
     * Reads a line of input from the user.
     *
     * @return the user input as a String
     * @throws IOException if an input/output error occurs
     */
    private String getInput() throws IOException {
        String input = reader.readLine();
        return input == null ? "" : input;
    }

    /**
     * Validates the size input by the user, ensuring it is within the specified range
     * and is an odd number. If not valid, it selects a random size.
     *
     * @param size the input size as a String
     * @param min the minimum acceptable size
     * @param max the maximum acceptable size
     * @return the validated size
     */
    public int checkSize(String size, int min, int max) {
        try {
            int s = Integer.parseInt(size);
            if (s >= min && s <= max && s % 2 != 0) {
                return s;
            } else {
                mainInterface.chooseRandom();
                return selectRandomSize(min, max);
            }
        } catch (NumberFormatException e) {
            mainInterface.chooseRandom();
            return selectRandomSize(min, max);
        }
    }

    /**
     * Validates the coordinates input by the user, checking if they are within bounds
     * and correspond to a passable cell in the maze.
     *
     * @param coordinates the input coordinates as a String
     * @param height the height of the maze
     * @param width the width of the maze
     * @return true if the coordinates are invalid, false otherwise
     */
    public boolean checkCoordinates(String coordinates, int height, int width) {
        try {
            String[] parts = coordinates.split(" ");
            if (parts.length != 2) {
                mainInterface.onlyNumbers();
                return true;
            }
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            boolean inBounds = (0 <= x && x < width && 0 <= y && y < height);

            if (inBounds) {
                if (checkIsPassage(y, x)) {
                    return false;
                } else {
                    mainInterface.wrongPoint();
                }
            } else {
                mainInterface.incorrectCoordinates();
            }

            return true;
        } catch (NumberFormatException e) {
            mainInterface.onlyNumbers();
            return true;
        }
    }

    private boolean checkIsPassage(int y, int x) {
        return PASSABLE_TYPES.contains(maze.grid()[y][x].type());
    }

    private int selectRandomSize(int min, int max) {
        int[] range = IntStream.rangeClosed(min, max).filter(n -> n % 2 != 0).toArray();
        return range[random.nextInt(range.length)];
    }

    private Generator selectGenerator(String generator) {
        return new GeneratorFactory(random).selectGenerator(generator);
    }

    private Solver selectSolver(String solver) {
        return new SolverFactory(random).selectSolver(solver);
    }

    private Modifier selectModifier(String modifier) {
        return new ModifierFactory(maze, random).selectModifier(modifier);
    }
}
